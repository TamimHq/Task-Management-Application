package com.example.task_management.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.shared.model.Priority
import com.example.shared.model.Task
import com.example.shared.model.TaskStatus
import com.example.shared.viewmodel.TaskViewModel
import com.example.shared.util.isDateValid
import com.example.shared.util.toEpochMillis
import kotlinx.datetime.*
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    viewModel: TaskViewModel,
    onTaskSaved: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.LOW) }
    var status by remember { mutableStateOf(TaskStatus.TODO) }
    var dueDateMillis by remember { mutableStateOf<Long?>(null) }

    // TEMP message holder for snackbar
    var snackbarMessage by remember { mutableStateOf<String?>(null) }

    // Trigger snackbar when message updates
    snackbarMessage?.let { msg ->
        LaunchedEffect(msg) {
            snackbarHostState.showSnackbar(msg)
            snackbarMessage = null
        }
    }

    val context = LocalContext.current
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Back") }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
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
                            .toLocalDateTime(TimeZone.currentSystemDefault()).date
                        "Due: $d"
                    } ?: "Pick Due Date (optional)"
                )
            }

            Button(
                onClick = {
                    if (title.isBlank()) {
                        snackbarMessage = "Title is required"
                        return@Button
                    }

                    val task = Task(
                        id = Random.nextInt().toString(),
                        title = title,
                        description = description.ifBlank { null },
                        priority = priority,
                        status = status,
                        dueDate = dueDateMillis,
                        createdAt = Clock.System.now().toEpochMilliseconds()
                    )

                    viewModel.addTask(task)
                    onTaskSaved()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Task")
            }
        }
    }
}

/* ---------- DROPDOWNS ---------- */

@Composable
fun PriorityDropdown(selected: Priority, onSelect: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Priority: ${selected.name}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Priority.values().forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun StatusDropdown(selected: TaskStatus, onSelect: (TaskStatus) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Status: ${selected.name}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            TaskStatus.values().forEach { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    onClick = {
                        onSelect(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
