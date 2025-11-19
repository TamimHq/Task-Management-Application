package com.example.task_management

import android.app.Application
import com.example.task_management.di.AppModule

class TaskApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppModule.init(this)
    }
}
