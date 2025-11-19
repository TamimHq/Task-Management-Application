package com.example.shared.repository

import com.example.shared.model.*

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: String)

    suspend fun searchTasks(query: String): List<Task>
    suspend fun filterTasks(priority: Priority?, status: TaskStatus?): List<Task>

    suspend fun sortByDate(): List<Task>
    suspend fun sortByPriority(): List<Task>
    suspend fun sortByStatus(): List<Task>
}
