package utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mspp_project.R

/**
 * This object is responsible for managing notifications.
 */
object NotificationHelper {
    /**
     * Creates a notification channel if the Android version is Oreo or above.
     *
     * @param context The context in which this manager is operating.
     * @param channelId The ID for the notification channel.
     * @param channelName The name for the notification channel.
     */
    fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Sends a notification with the specified parameters.
     *
     * @param context The context in which this manager is operating.
     * @param channelId The ID for the notification channel.
     * @param title The title for the notification.
     * @param content The content for the notification.
     */
//    fun showNotification(context: Context, channelId: String, title: String, content: String) {
//        val builder = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.drawable.notification_icon)
//            .setContentTitle(title)
//            .setContentText(content)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        with(NotificationManagerCompat.from(context)) {
//            notify(0, builder.build())
//        }
//    }
}