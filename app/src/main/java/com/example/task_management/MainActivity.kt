package com.example.task_management

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.task_management.di.AppModule
import com.example.task_management.di.SharedViewModelProvider
import com.example.task_management.navigation.AppNavGraph
import com.example.task_management.viewmodel.AndroidTaskViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<AndroidTaskViewModel> { SharedViewModelProvider() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppModule.init(applicationContext)

        setContent {
            val navController = rememberNavController()
            AppNavGraph(
                navController = navController,
                viewModel = viewModel.sharedViewModel
            )
        }
    }
}
