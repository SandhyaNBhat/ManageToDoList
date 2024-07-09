package com.spcreations.managetodolist

import android.R
import android.widget.Spinner;
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_12H
import com.spcreations.managetodolist.data.TaskCategory
import com.spcreations.managetodolist.data.Todo
import com.spcreations.managetodolist.databinding.ActivityAddTasksBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.properties.Delegates

class AddTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTasksBinding
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val sdtf = SimpleDateFormat("dd/MM/yyyy HH:mm")
    private lateinit var date: String
    private lateinit var time: String
    private var dateTimeInLong: Long = 0
    private lateinit var dateAndTime: String
    private lateinit var dateTimeInDateFormat: Date
    private lateinit var viewModel: TodoViewModel

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent
    private var editCatId: Int? = null
    lateinit var categoryAdapter: ArrayAdapter<String>
    private var editMode: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSaveTask.isEnabled = false
        binding.btnSaveTask.isVisible = false


        //calling action bar
        var actionBar = supportActionBar
        // showing the back button in action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        viewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        //Populate the values for categories spinner from the database
        //Create an array adapter using the string array and default spinner layout


        getCategoryValues()

        val i = intent

        val editTaskName = i.getStringExtra("task_name")
        val editTaskCategory = i.getLongExtra("task_category", 0)
        Log.d("TAG", "taskCategory val " + editTaskCategory)

        val editDueDate = i.getLongExtra("task_due_date", 0)
        Log.d("TAG", "editDueDate val " + editDueDate)
        val editTaskId = i.getLongExtra("task_Id", 0)

        //Check if the activity is opened in insert mode or edit mode

        if (editTaskName != null) {
            binding.name.setText(editTaskName)
            editCatId = (editTaskCategory - 1).toInt()
            Log.d("TAG", "cat id " + editCatId)
            getCategoryValues()
            binding.categoriesSpinner.setSelection(editCatId!!, true)
            binding.duedate.setText(editDueDate.let { convertLongToDateTime(it) })
            binding.btnSaveTask.isVisible = true
            binding.btnSaveTask.isEnabled = true

            binding.btnAddTask.isEnabled = false
            binding.btnAddTask.isVisible = false

            editMode = true



        }


        //Open Add category dialog
        binding.btnAddCategory.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }

        //Date picker
        binding.duedate.setOnClickListener {
            val today = MaterialDatePicker.todayInUtcMilliseconds()
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = today
            calendar[Calendar.YEAR] = calendar.get(Calendar.YEAR)
            val startDate = calendar.timeInMillis

            val constraints: CalendarConstraints = CalendarConstraints.Builder()
                .setOpenAt(startDate)
                .setStart(startDate)
                .build()


            val datePickerBuilder: MaterialDatePicker.Builder<Long> = MaterialDatePicker
                .Builder
                .datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText("Select Date")
                .setCalendarConstraints(constraints)

            val datePicker = datePickerBuilder.build()
            datePicker.show(supportFragmentManager, "DATE_PICKER")

            datePicker.addOnPositiveButtonClickListener {
                date = sdf.format(it)

                //Time picker

                val timePickerBuilder: MaterialTimePicker.Builder = MaterialTimePicker
                    .Builder()
                    .setTitleText("Select Time")
                    .setHour(2)
                    .setMinute(30)
                    .setInputMode(MaterialTimePicker.INPUT_MODE_KEYBOARD)

                val timePicker = timePickerBuilder.build()

                timePicker.show(supportFragmentManager, "TIME_PICKER")

                timePicker.addOnPositiveButtonClickListener {
                    time = timeConverter(
                        hour = timePicker.hour,
                        minute = timePicker.minute
                    )
                    var dateTime = date + ' ' + time
                    binding.duedate.setText(dateTime)

                    dateAndTime = date + " " + timePicker.hour + ":" + timePicker.minute
                    dateTimeInDateFormat = sdtf.parse(dateAndTime)
                    dateTimeInLong = dateTimeInDateFormat.time

                    Log.d("TAG", "dateTime In string " + dateAndTime)
                    Log.d("TAG", "dateTime In long " + date)


                }
            }
        }

        //Add tasks to the database table
        binding.btnAddTask.setOnClickListener {
            val taskName = binding.name.editableText.toString()
            val taskCategory = binding.categoriesSpinner.selectedItemId
            val taskDueDate = dateTimeInLong

            Log.d("Tag", "Task name " + taskName)
            Log.d("Tag", "Selected Task Category" + taskCategory)

            val todoTaskItem = Todo(0, taskName, false, taskDueDate, taskCategory + 1)
            viewModel.insert(todoTaskItem)

            callAlarmManager(taskName, taskDueDate)

            moveBackToParentActivity()

        }

        //Update tasks to the database table
        binding.btnSaveTask.setOnClickListener {
            val taskName = binding.name.editableText.toString()
            val taskCategory = binding.categoriesSpinner.selectedItemId
            val taskDueDate = if (dateTimeInLong.equals(0)) editDueDate else dateTimeInLong

            Log.d("Tag", "Task name  " + taskName)
            Log.d("Tag", "Selected Task Category" + taskCategory)
            Log.d("Tag", "Selected taskDueDate" + taskDueDate)

            val updatedTaskItem = Todo(editTaskId, taskName, false, taskDueDate, taskCategory + 1)
            viewModel.update(updatedTaskItem)

            callAlarmManager(taskName, taskDueDate)

            moveBackToParentActivity()
        }


        //Navigate back to previous screen on cancel button click
        binding.btnCancelTask.setOnClickListener {
            moveBackToParentActivity()
        }

    }





    private fun getCategoryValues() {
        viewModel.categories.observe(this, Observer {
            categoryAdapter =
                ArrayAdapter(this@AddTasksActivity, R.layout.simple_spinner_item, it)
            categoryAdapter.setDropDownViewResource(R.layout.simple_dropdown_item_1line)
            categoryAdapter.notifyDataSetChanged()

            binding.categoriesSpinner.adapter = categoryAdapter
        })
    }


    private fun moveBackToParentActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private fun callAlarmManager(taskName: String, taskDueDate: Long) {

        Log.d("TAG", "Inside Alarm Manager for the task " + taskName + "task Id " + taskId)
        val Id: Int = System.currentTimeMillis().toInt()
        Log.d("TAG ", "Id -" + Id)
        alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("TAG ", "Alarm Manager is invoked")
        alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            intent.putExtra("Task_Name", taskName)
            intent.putExtra("Id", Id)
            PendingIntent.getBroadcast(this, Id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        Log.d("TAG ", "Alarm Intent is set")

        alarmMgr?.set(
            AlarmManager.RTC_WAKEUP,
            taskDueDate,
            alarmIntent
        )


        Log.d("TAG ", "Alarm Manager is set")

    }





}