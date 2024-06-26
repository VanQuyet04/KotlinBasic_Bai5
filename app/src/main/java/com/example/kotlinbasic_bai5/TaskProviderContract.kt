package com.example.kotlinbasic_bai5
import android.net.Uri
import android.provider.BaseColumns

object TaskProviderContract {
    const val AUTHORITY = "com.example.task-provider"
    const val CONTENT_PATH = "tasks"
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$CONTENT_PATH")

    object TaskColumns : BaseColumns {
        const val _ID = BaseColumns._ID
        const val COLUMN_NAME = "name"
        const val COLUMN_DATETIME = "datetime"
    }
}
