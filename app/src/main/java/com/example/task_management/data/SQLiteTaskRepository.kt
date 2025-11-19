package com.example.task_management.data

import android.content.ContentValues
import android.content.Context
import com.example.shared.model.*
import com.example.shared.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SQLiteTaskRepository(context: Context) : TaskRepository {

    private val db = TaskDatabaseHelper(context).writableDatabase

    override suspend fun getAllTasks(): List<Task> = withContext(Dispatchers.IO) {
        val cursor = db.rawQuery("SELECT * FROM tasks", null)
        val list = mutableListOf<Task>()

        cursor.use {
            while (cursor.moveToNext()) {
                list.add(
                    Task(
                        id = cursor.getString(0),
                        title = cursor.getString(1),
                        description = cursor.getString(2),
                        priority = Priority.valueOf(cursor.getString(3)),
                        status = TaskStatus.valueOf(cursor.getString(4)),
                        dueDate = if (!cursor.isNull(5)) cursor.getLong(5) else null,
                        createdAt = cursor.getLong(6)
                    )
                )
            }
        }
        list
    }

    override suspend fun addTask(task: Task) = withContext(Dispatchers.IO) {
        val cv = ContentValues().apply {
            put("id", task.id)
            put("title", task.title)
            put("description", task.description)
            put("priority", task.priority.name)
            put("status", task.status.name)
            put("dueDate", task.dueDate)
            put("createdAt", task.createdAt)
        }

        db.insert("tasks", null, cv)
        return@withContext Unit
    }

    override suspend fun updateTask(task: Task) = withContext(Dispatchers.IO) {
        val cv = ContentValues().apply {
            put("title", task.title)
            put("description", task.description)
            put("priority", task.priority.name)
            put("status", task.status.name)
            put("dueDate", task.dueDate)
        }

        db.update("tasks", cv, "id=?", arrayOf(task.id))
        return@withContext Unit
    }

    override suspend fun deleteTask(id: String) = withContext(Dispatchers.IO) {
        db.delete("tasks", "id=?", arrayOf(id))
        return@withContext Unit
    }

    override suspend fun searchTasks(query: String): List<Task> =
        getAllTasks().filter { it.title.contains(query, ignoreCase = true) }

    override suspend fun filterTasks(priority: Priority?, status: TaskStatus?) =
        getAllTasks().filter {
            (priority == null || it.priority == priority) &&
                    (status == null || it.status == status)
        }

    override suspend fun sortByDate(): List<Task> =
        getAllTasks().sortedBy { it.dueDate ?: Long.MAX_VALUE }

    override suspend fun sortByPriority(): List<Task> =
        getAllTasks().sortedBy { it.priority.ordinal }

    override suspend fun sortByStatus(): List<Task> =
        getAllTasks().sortedBy { it.status.ordinal }
}
