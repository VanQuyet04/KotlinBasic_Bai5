package com.example.kotlinbasic_bai5

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import java.time.LocalDateTime
import java.time.Duration
import java.util.Calendar
import java.util.concurrent.TimeUnit

class TaskReminderWorker(appContext: Context, params: WorkerParameters) :
    Worker(appContext, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        sendNotification()
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification() {
        val taskRepository = TaskRepository(applicationContext.contentResolver)
        val tasks = taskRepository.getAllTasks()

        val now = LocalDateTime.now()
        val nextTask = tasks.filter {
            LocalDateTime.parse(it.datetime, Task.dateTimeFormatter).isAfter(now)
        }.minByOrNull {
            LocalDateTime.parse(it.datetime, Task.dateTimeFormatter)
        }

        if (nextTask != null) {
            val taskDateTime = LocalDateTime.parse(nextTask.datetime, Task.dateTimeFormatter)
            val duration = Duration.between(now, taskDateTime)
            val minutesUntilTask = duration.toMinutes()

            createNotificationChannel()

            val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Task Reminder")
                .setContentText("Next task '${nextTask.name}' in $minutesUntilTask minutes")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(applicationContext)) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Task Reminder"
            val descriptionText = "Channel for task reminder notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "task_reminder_channel"
        private const val NOTIFICATION_ID = 1

        fun scheduleReminder(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val taskReminderRequest = PeriodicWorkRequestBuilder<TaskReminderWorker>(
                repeatInterval = 1, // Repeat daily
                repeatIntervalTimeUnit = TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .setInitialDelay(calculateDelayToNext23_10(), TimeUnit.MILLISECONDS) // Delay đến 23:10
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "task_reminder",
                ExistingPeriodicWorkPolicy.REPLACE,
                taskReminderRequest
            )
        }

        private fun calculateDelayToNext23_10(): Long {
            val now = System.currentTimeMillis()
            val calendar = Calendar.getInstance().apply {
                timeInMillis = now
                set(Calendar.HOUR_OF_DAY, 18)
                set(Calendar.MINUTE, 1)
                set(Calendar.SECOND, 0)
                if (timeInMillis <= now) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            return calendar.timeInMillis - now
        }
    }
}
