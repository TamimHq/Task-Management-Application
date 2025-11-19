package com.example.task_management.di

import android.content.Context
import com.example.shared.repository.TaskRepository
import com.example.shared.usecase.TaskUseCases
import com.example.task_management.data.SQLiteTaskRepository

object AppModule {

    lateinit var taskUseCases: TaskUseCases
        private set

    fun init(context: Context) {
        val repo: TaskRepository = SQLiteTaskRepository(context)
        taskUseCases = TaskUseCases(repo)
    }
}
