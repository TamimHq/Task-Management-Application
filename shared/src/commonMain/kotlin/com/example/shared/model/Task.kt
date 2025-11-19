package com.example.shared.model

import kotlinx.datetime.*

data class Task(
    val id: String,
    val title: String,
    val description: String? = null,
    val priority: Priority = Priority.LOW,
    val status: TaskStatus = TaskStatus.TODO,
    val dueDate: Long? = null,  // epoch millis
    val createdAt: Long
) {
    val isDueToday: Boolean
        get() = dueDate?.let {
            val today = Clock.System.now()
                .toLocalDateTime(TimeZone.currentSystemDefault()).date

            val taskDate = Instant.fromEpochMilliseconds(it)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date

            taskDate == today
        } ?: false
}

enum class Priority { LOW, MEDIUM, HIGH }

enum class TaskStatus { TODO, IN_PROGRESS, DONE }
