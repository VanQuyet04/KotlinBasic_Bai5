package com.example.kotlinbasic_bai5

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@RequiresApi(Build.VERSION_CODES.O)
class TaskListScreen : ComponentActivity() {

    private lateinit var taskRepository: TaskRepository
    private var taskList by mutableStateOf<List<Task>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskRepository = TaskRepository(contentResolver)

        setContent {
            TaskListScreenContent()
            TaskReminderWorker.scheduleReminder(LocalContext.current)
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun loadTasks() {
        val tasks = taskRepository.getAllTasks()
        taskList = tasks
    }

    @Composable
    fun TaskListScreenContent() {
        Column {
            Text(
                text = "Task List",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            TaskList(taskList = taskList)
        }
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
                Text(
                    text = "Name: ${task.name}",
                    textAlign = TextAlign.Start
                )
                Text(
                    text = task.datetime,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
