package com.a2krocks.widget.services
import android.content.Intent
import android.app.PendingIntent
import android.app.AlarmManager
import android.content.Context
import java.util.*

class AlarmHandler(private val context: Context) {


    fun setAlarmManager() {
        val sender = PendingIntent.getBroadcast(context, 2, Intent(context, OneplusWidgetService::class.java), PendingIntent.FLAG_IMMUTABLE)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //get current time and add 1.5 seconds
        val c = Calendar.getInstance()
        val l = c.timeInMillis + 1500

        //set the alarm for 10 seconds in the future
        am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, l, sender)
    }

    fun cancelAlarmManager() {
        val sender: PendingIntent = PendingIntent.getBroadcast(context, 2, Intent(context, OneplusWidgetService::class.java), PendingIntent.FLAG_IMMUTABLE)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(sender)
    }


}