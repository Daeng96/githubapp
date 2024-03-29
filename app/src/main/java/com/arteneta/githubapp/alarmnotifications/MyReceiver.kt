package com.arteneta.githubapp.alarmnotifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.arteneta.githubapp.view.MainActivity
import com.arteneta.githubapp.R
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
	private var alarmMgr: AlarmManager? = null
	private lateinit var alarmIntent: PendingIntent

	companion object {
		private const val ID_REPEAT = 101
	}

	@RequiresApi(Build.VERSION_CODES.M)
	override fun onReceive(context: Context, intent: Intent) {
		showAlarmNotification(context)
	}

	fun setRepeater(context: Context) {
		alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		alarmIntent = Intent(context, AlarmReceiver::class.java).let {
			PendingIntent.getBroadcast(context,
				ID_REPEAT, it, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
		}

		val calendar: Calendar = Calendar.getInstance().apply {
			this.set(Calendar.HOUR_OF_DAY, 9)
			this.set(Calendar.MINUTE, 0)
			this.set(Calendar.SECOND, 0)
		}

		val time =
			if (System.currentTimeMillis() >= calendar.timeInMillis) calendar.timeInMillis + AlarmManager.INTERVAL_DAY else calendar.timeInMillis

		alarmMgr?.setInexactRepeating(
			AlarmManager.RTC_WAKEUP,
			time,
			AlarmManager.INTERVAL_DAY,
			alarmIntent
		)
	}

	fun cancelRepeater(context: Context) {
		alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		alarmIntent = Intent(context, AlarmReceiver::class.java).let {
			PendingIntent.getBroadcast(context,
				ID_REPEAT, it, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
		}
		alarmMgr?.cancel(alarmIntent)
	}

	@RequiresApi(Build.VERSION_CODES.M)
	private fun showAlarmNotification(context: Context) {

		val chanelId = "Channel_1"
		val chanelName = "AlarmManager channel"

		val notifyIntent = Intent(context, MainActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		}
		val notifPending =
			PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)

		val notificationManagerCompat =
			context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
		val builder = NotificationCompat.Builder(context, chanelId)
			.setContentIntent(notifPending)
			.setSmallIcon(R.drawable.ic_alarm_24)
			.setContentTitle(context.getString(R.string.app_name))
			.setContentText(context.getString(R.string.msg_notification))
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setSound(alarmSound)
			.setAutoCancel(true)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				chanelId,
				chanelName,
				NotificationManager.IMPORTANCE_DEFAULT
			)
			builder.setChannelId(chanelId)
			notificationManagerCompat.createNotificationChannel(channel)
		}

		val notification = builder.build()
		notificationManagerCompat.notify(ID_REPEAT, notification)
	}

}