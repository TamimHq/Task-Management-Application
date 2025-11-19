package com.example.shared.viewmodel

import com.example.shared.model.Priority
import com.example.shared.model.Task
import com.example.shared.model.TaskStatus
import com.example.shared.usecase.TaskUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val useCases: TaskUseCases
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        loadAllTasks()
    }

    // 🔄 Load tasks into StateFlow
    fun loadAllTasks() {
        scope.launch {
            _tasks.value = useCases.getTasks()
        }
    }

    // ➕ Add new task
    fun addTask(task: Task) {
        scope.launch {
            useCases.addTask(task)
            loadAllTasks()
        }
    }

    // ✏ Update
    fun updateTask(task: Task) {
        scope.launch {
            useCases.updateTask(task)
            loadAllTasks()
        }
    }

    // 🗑 Delete
    fun deleteTask(id: String) {
        scope.launch {
            useCases.deleteTask(id)
            loadAllTasks()
        }
    }

    // 🔍 Search
    fun search(query: String) {
        scope.launch {
            _tasks.value = useCases.search(query)
        }
    }

    // 🎯 Filter
    fun filterTasks(priority: Priority?, status: TaskStatus?) {
        scope.launch {
            _tasks.value = useCases.filter(priority, status)
        }
    }

    // 📅 Sort by Due Date
    fun sortByDate() {
        scope.launch {
            _tasks.value = useCases.sortByDate()
        }
    }

    // 🔽 Sort by Priority
    fun sortByPriority() {
        scope.launch {
            _tasks.value = useCases.sortByPriority()
        }
    }

    // 🚦 Sort by Status
    fun sortByStatus() {
        scope.launch {
            _tasks.value = useCases.sortByStatus()
        }
    }
}
