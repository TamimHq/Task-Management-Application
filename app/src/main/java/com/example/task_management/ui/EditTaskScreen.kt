package com.example.task_management.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.shared.viewmodel.TaskViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.example.shared.util.isDateValid
import com.example.shared.util.toEpochMillis



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: String,
    viewModel: TaskViewModel,
    onBack: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val existing = tasks.find { it.id == taskId }


    if (existing == null) {
        Text("Task not found")
        return
    }

    var title by remember { mutableStateOf(existing.title) }
    var description by remember { mutableStateOf(existing.description ?: "") }
    var priority by remember { mutableStateOf(existing.priority) }
    var status by remember { mutableStateOf(existing.status) }
    var dueDateMillis by remember { mutableStateOf<Long?>(existing.dueDate) }
    var dateError by remember { mutableStateOf<String?>(null) }

    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val context = LocalContext.current
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var confirmDelete by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Task") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            PriorityDropdown(selected = priority) { priority = it }

            StatusDropdown(selected = status) { status = it }

            Button(
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            dueDateMillis = toEpochMillis(year, month + 1, day)
                        },
                        today.year,
                        today.monthNumber - 1,
                        today.dayOfMonth
                    ).apply {
                        datePicker.minDate = System.currentTimeMillis() // prevents selecting past days
                    }.show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    dueDateMillis?.let {
                        val d = Instant.fromEpochMilliseconds(it)
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                        "Due: $d"
                    } ?: "Pick Due Date (optional)"
                )
            }

            Button(
                onClick = {
                    if (dueDateMillis != null) {
                        val selectedDate = Instant.fromEpochMilliseconds(dueDateMillis!!)
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date

                        if (!isDateValid(selectedDate, today)) {
                            dateError = "Due date cannot be in the past!"
                            return@Button
                        }
                    }

                    dateError = null // clear previous error

                    val updated = existing.copy(
                        title = title,
                        description = description.ifBlank { null },
                        priority = priority,
                        status = status,
                        dueDate = dueDateMillis
                    )

                    viewModel.updateTask(updated)
                    onBack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Changes")
            }

            dateError?.let {
                Text(text = it, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
            }


            Button(
                onClick = { confirmDelete = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete Task")
            }

            if (confirmDelete) {
                AlertDialog(
                    onDismissRequest = { confirmDelete = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteTask(existing.id)
                            onBack()
                        }) { Text("Delete", color = Color.Red) }
                    },
                    dismissButton = {
                        TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
                    },
                    title = { Text("Delete Task") },
                    text = { Text("Are you sure? This cannot be undone.") }
                )
            }

        }
    }
}
