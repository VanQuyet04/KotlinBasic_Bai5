package com.example.kotlinbasic_bai5

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("NotConstructor")
class TaskListScreen : ComponentActivity() {

    private lateinit var taskRepository: TaskRepository
    private lateinit var taskList: MutableState<List<Task>>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskRepository = TaskRepository(contentResolver)
        taskList = mutableStateOf(emptyList())

        setContent {
            TaskListScreen(taskList = taskList.value)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTasks() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val tasks = taskRepository.getAllTasks()
            taskList.value = tasks
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun TaskListScreen(taskList: List<Task>) {
        Column {
            Text(
                text = "Task List",
                modifier = Modifier.padding(16.dp),
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            TaskList(taskList = taskList)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun TaskList(taskList: List<Task>) {
        LazyColumn {
            items(taskList) { task ->
                TaskItem(task)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = task.datetime, textAlign = TextAlign.Start
                )
            }
        }
    }
}
