package com.example.task_management.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shared.viewmodel.TaskViewModel

class AndroidTaskViewModel(
    val sharedViewModel: TaskViewModel
) : ViewModel()
