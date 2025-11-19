package com.example.task_management.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shared.model.Priority
import com.example.shared.model.Task
import com.example.shared.model.TaskStatus
import com.example.shared.viewmodel.TaskViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (Task) -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var priorityFilter by remember { mutableStateOf<Priority?>(null) }
    var statusFilter by remember { mutableStateOf<TaskStatus?>(null) }
    var sortMenuVisible by remember { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        viewModel.search(searchQuery)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTaskClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search tasks...") }
            )

            Spacer(Modifier.height(10.dp))

            // FILTERS
            Text("Filter by Priority")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(null, Priority.LOW, Priority.MEDIUM, Priority.HIGH).forEach { option ->
                    FilterChip(
                        selected = priorityFilter == option,
                        onClick = {
                            priorityFilter = option
                            viewModel.filterTasks(option, statusFilter)
                        },
                        label = { Text(option?.name ?: "All") }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text("Filter by Status")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(null, TaskStatus.TODO, TaskStatus.IN_PROGRESS, TaskStatus.DONE).forEach { option ->
                    FilterChip(
                        selected = statusFilter == option,
                        onClick = {
                            statusFilter = option
                            viewModel.filterTasks(priorityFilter, option)
                        },
                        label = { Text(option?.name ?: "All") }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // SORTING
            Box {
                Button(onClick = { sortMenuVisible = true }) {
                    Text("Sort Options")
                }
                DropdownMenu(
                    expanded = sortMenuVisible,
                    onDismissRequest = { sortMenuVisible = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("By Date") },
                        onClick = { viewModel.sortByDate(); sortMenuVisible = false }
                    )
                    DropdownMenuItem(
                        text = { Text("By Priority") },
                        onClick = { viewModel.sortByPriority(); sortMenuVisible = false }
                    )
                    DropdownMenuItem(
                        text = { Text("By Status") },
                        onClick = { viewModel.sortByStatus(); sortMenuVisible = false }
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // EMPTY STATE MESSAGE
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No tasks yet. Tap + to add one!")
                }
            } else {
                LazyColumn {
                    items(tasks) { task ->
                        TaskCard(
                            task = task,
                            onClick = { onEditTaskClick(task) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(task: Task, onClick: () -> Unit) {

    val dueDate = task.dueDate  // <-- Store locally to allow smart cast
    val now = Clock.System.now().toEpochMilliseconds()

    val backgroundColor = when {
        dueDate == null -> Color(0xFF4CAF50)  // No due date = Green
        dueDate < now -> Color(0xFFFF5252)   // Past deadline = Red
        (dueDate - now) <= 24L * 60 * 60 * 1000 -> Color(0xFFFFEB3B)  // Less than 24h = Yellow
        else -> Color(0xFF4CAF50)  // More than 1 day = Green
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(task.title, style = MaterialTheme.typography.titleMedium)

            task.description?.let {
                Text(it, style = MaterialTheme.typography.bodySmall)
            }

            Text("Priority: ${task.priority}")
            Text("Status: ${task.status}")

            dueDate?.let {
                val date = Instant.fromEpochMilliseconds(it)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date
                Text("Due: $date")
            }
        }
    }
}
