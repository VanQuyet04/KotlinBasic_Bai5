package com.example.kotlinbasic_bai5

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private lateinit var taskRepository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskRepository = TaskRepository(contentResolver)

        setContent {
            TaskListScreen(taskRepository = taskRepository)
        }
    }

    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun TaskListScreen(taskRepository: TaskRepository) {

        var taskList by remember {
            mutableStateOf<List<Task>>(emptyList())
        }

        LaunchedEffect(key1 = Unit) {
            taskList = taskRepository.getAllTasks()
        }

        TaskList(taskList = taskList)
    }

    @Composable
    fun TaskList(taskList: List<Task>) {
        LazyColumn {
            items(taskList) { task ->
                TaskItem(task)
            }
        }
    }

    @Composable
    fun TaskItem(task: Task) {
        Card(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Name: ${task.name}",
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Datetime: ${task.datetime}",
                    textAlign = TextAlign.Start
                )
            }
        }
    }

}
