

// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {

    val agp_version by extra("8.2.0")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:$agp_version")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}


