package com.a2krocks.widget.services

import com.a2krocks.widget.OneplusWidget



import android.content.Intent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.util.Log

class OneplusWidgetService : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //wake the device
        WakeLocker.acquire(context)

        //force widget update
        val widgetIntent = Intent(context, OneplusWidget::class.java)
        widgetIntent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context)
            .getAppWidgetIds(ComponentName(context, OneplusWidget::class.java))
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(widgetIntent)
        Log.d("WIDGET", "Widget set to update!")

        //go back to sleep
        WakeLocker.release()
    }
}