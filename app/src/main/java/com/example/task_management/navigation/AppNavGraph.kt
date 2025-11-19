package com.example.task_management.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.task_management.ui.TaskListScreen
import com.example.task_management.ui.AddTaskScreen
import com.example.shared.viewmodel.TaskViewModel
import com.example.task_management.ui.EditTaskScreen

object Routes {
    const val TaskList = "task_list"
    const val AddTask = "add_task"
    const val EditTask = "edit_task/{taskId}"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.TaskList
    ) {

        // TASK LIST SCREEN
        composable(Routes.TaskList) {
            TaskListScreen(
                viewModel = viewModel,
                onAddTaskClick = { navController.navigate(Routes.AddTask) },
                onEditTaskClick = { task -> navController.navigate("edit_task/${task.id}") }
            )
        }

        // ADD TASK SCREEN
        composable(Routes.AddTask) {
            AddTaskScreen(
                viewModel = viewModel,
                onTaskSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // EDIT TASK SCREEN
        composable(Routes.EditTask) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")!!
            EditTaskScreen(
                taskId = taskId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
