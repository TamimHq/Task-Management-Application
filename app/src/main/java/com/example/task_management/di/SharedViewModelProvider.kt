package com.example.task_management.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shared.viewmodel.TaskViewModel
import com.example.task_management.viewmodel.AndroidTaskViewModel

class SharedViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AndroidTaskViewModel::class.java)) {
            val sharedVM = TaskViewModel(AppModule.taskUseCases)
            return AndroidTaskViewModel(sharedVM) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
