package org.monora.uprotocol.client.android.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import org.monora.uprotocol.client.android.R

/**
 * Created by: veli
 * Date: 4/28/17 2:00 AM
 */
class NotificationBackend(val context: Context) {
    val manager = NotificationManagerCompat.from(context)

    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val notificationSettings: Int
        get() {
            val makeSound = if (preferences.getBoolean("notification_sound", true))
                NotificationCompat.DEFAULT_SOUND else 0
            val vibrate = if (preferences.getBoolean("notification_vibrate", true))
                NotificationCompat.DEFAULT_VIBRATE else 0
            val light = if (preferences.getBoolean("notification_light", false))
                NotificationCompat.DEFAULT_LIGHTS else 0
            return makeSound or vibrate or light
        }

    fun buildDynamicNotification(notificationId: Int, channelId: String): DynamicNotification {
        return DynamicNotification(context, manager, channelId, notificationId)
    }

    fun cancel(notificationId: Int) {
        manager.cancel(notificationId)
    }

    companion object {
        private const val TAG = "NotificationBackend"

        const val NOTIFICATION_CHANNEL_HIGH = "tsHighPriority"

        const val NOTIFICATION_CHANNEL_LOW = "tsLowPriority"

        const val CHANNEL_INSTRUCTIVE = "instructiveNotifications"

        const val EXTRA_NOTIFICATION_ID = "notificationId"
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelHigh = NotificationChannel(
                NOTIFICATION_CHANNEL_HIGH,
                context.getString(R.string.high_priority_notifications), NotificationManager.IMPORTANCE_HIGH
            )
            channelHigh.enableLights(preferences.getBoolean("notification_light", false))
            channelHigh.enableVibration(preferences.getBoolean("notification_vibrate", false))
            manager.createNotificationChannel(channelHigh)
            val channelLow = NotificationChannel(
                NOTIFICATION_CHANNEL_LOW,
                context.getString(R.string.low_priority_notifications), NotificationManager.IMPORTANCE_LOW
            )
            manager.createNotificationChannel(channelLow)

            val channelInstructive = NotificationChannel(
                CHANNEL_INSTRUCTIVE,
                context.getString(R.string.notifications_instructive),
                NotificationManager.IMPORTANCE_MAX
            )
            manager.createNotificationChannel(channelInstructive)
        }
    }
}
