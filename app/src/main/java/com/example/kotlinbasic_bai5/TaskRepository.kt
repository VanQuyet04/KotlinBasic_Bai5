package com.example.kotlinbasic_bai5

import android.content.ContentResolver
import android.util.Log

class TaskRepository(private val contentResolver: ContentResolver) {

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val uri = TaskProviderContract.CONTENT_URI

        // Query database
        val cursor = contentResolver.query(uri, null, null, null, null)

        cursor?.use { c ->
            while (c.moveToNext()) {
                val id = c.getLong(c.getColumnIndexOrThrow(TaskProviderContract.TaskColumns._ID))
                val name =
                    c.getString(c.getColumnIndexOrThrow(TaskProviderContract.TaskColumns.COLUMN_NAME))
                val datetime =
                    c.getString(c.getColumnIndexOrThrow(TaskProviderContract.TaskColumns.COLUMN_DATETIME))
                tasks.add(Task(id, name, datetime))
            }
        }

        return tasks
    }


}