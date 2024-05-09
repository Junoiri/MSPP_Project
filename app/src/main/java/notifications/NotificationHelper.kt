package notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.util.Log
import com.example.mspp_project.R


//TODO: make it work based on date of vaccination - reminders
class NotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "example_channel"
        private const val NOTIFICATION_ID = 1
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Example Channel"
            val descriptionText = "This is an example channel for notifications."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification() {
        val sharedPref = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val notificationsEnabled = sharedPref.getBoolean("notifications_enabled", true)

        if (!notificationsEnabled) {
            Log.i("activities.notifications.NotificationHelper", "Notifications are disabled")
            return
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Test notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, builder.build())
            }
        } else {
            Log.e("activities.notifications.NotificationHelper", "ACCESS_NOTIFICATION_POLICY permission not granted")
        }
    }
}