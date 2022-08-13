package com.a2krocks.widget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import com.a2krocks.widget.services.AlarmHandler
import com.a2krocks.widget.services.WeatherService

/**
 * Implementation of App Widget functionality.
 */
class OneplusWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDisabled(context: Context) {
        val alarmHandler = AlarmHandler(context)
        alarmHandler.cancelAlarmManager()
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        updateAppWidget(context, appWidgetManager, appWidgetId)
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    @SuppressLint("MissingPermission")
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.oneplus_widget)

        val pref = context.getSharedPreferences("test", Context.MODE_PRIVATE)
        val test = pref.getInt("test", 0)

            pref.edit().putInt("test", test + 1 ).commit()


        Log.d("Test", "test $test")

        appWidgetManager.updateAppWidget(appWidgetId, views)

        val alarmHandler = AlarmHandler(context)
        alarmHandler.cancelAlarmManager()
        alarmHandler.setAlarmManager()
    }
}

