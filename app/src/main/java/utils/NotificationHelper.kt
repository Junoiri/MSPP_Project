package utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mspp_project.R

object NotificationHelper {
    fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

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