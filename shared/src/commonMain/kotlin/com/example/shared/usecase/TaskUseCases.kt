package com.example.shared.usecase

import com.example.shared.model.*
import com.example.shared.repository.TaskRepository

class TaskUseCases(
    private val repository: TaskRepository
) {
    suspend fun getTasks(): List<Task> = repository.getAllTasks()

    suspend fun addTask(task: Task) = repository.addTask(task)

    suspend fun updateTask(task: Task) = repository.updateTask(task)

    suspend fun deleteTask(id: String) = repository.deleteTask(id)

    suspend fun search(query: String): List<Task> = repository.searchTasks(query)

    suspend fun filter(
        priority: Priority? = null,
        status: TaskStatus? = null
    ): List<Task> = repository.filterTasks(priority, status)

    suspend fun sortByDate(): List<Task> = repository.sortByDate()

    suspend fun sortByPriority(): List<Task> = repository.sortByPriority()

    suspend fun sortByStatus(): List<Task> = repository.sortByStatus()
}
