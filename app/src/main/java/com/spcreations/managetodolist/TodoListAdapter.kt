package com.spcreations.managetodolist

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.spcreations.managetodolist.data.TodoListItem
import com.spcreations.managetodolist.databinding.TodolistItemsBinding
import java.text.SimpleDateFormat
import java.util.Date

class TodoListAdapter(private var todoList: List<TodoListItem>):
    RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    var onCheckBoxClick : ((String) -> Unit)? = null
    var onClickListener : ((TodoListItem) ->Unit)? = null
    var onCheckBoxUnClick : ((String) -> Unit)? = null

      inner class ViewHolder(val binding:TodolistItemsBinding,clickAtPosition:(Int)->Unit):RecyclerView.ViewHolder(binding.root){

          init{
              itemView.setOnClickListener {
                 clickAtPosition(adapterPosition) }

              }

       fun bind(item:TodoListItem){

           binding.taskId.text = item.taskId.toString()
            binding.taskName.text = item.taskName
            binding.category.text = item.category
            binding.duedate.text= item.dueDate?.let { convertLongToDateTime(it) }
            val sdf  =SimpleDateFormat("dd/MM/yyyy HH:mm")
            val currentDateandTime = sdf.format(Date())

           Log.d("TAGADAPTER","item due date "+item.dueDate)
           Log.d("TAGADAPTER","system date "+System.currentTimeMillis())

             if (item.dueDate!! <System.currentTimeMillis()) binding.duedate.setTextColor(Color.RED)

           Log.d("TAG","converted date field "+item.dueDate?.let{ convertLongToDateTime(it) })

            binding.checkbox.setOnClickListener {
                onCheckBoxClick?.invoke(item.taskName)
            }

            if(binding.checkbox.isChecked == false){
                onCheckBoxUnClick?.invoke(item.taskName)
            }

        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodolistItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        val vh = ViewHolder(binding){
            onClickListener?.let { it1 -> it1(todoList[it]) }
        }
        return vh
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val item = todoList[position]
        holder.bind(item)


    }

    override fun getItemCount()=todoList.size

    fun setItems(newItems : List<TodoListItem>){
        todoList = newItems
        notifyDataSetChanged()

    }



}