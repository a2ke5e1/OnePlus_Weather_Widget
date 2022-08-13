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
        val locationPreferences = context.getSharedPreferences("location_pref", Context.MODE_PRIVATE)
        val lat = locationPreferences.getString("location_pref_lat", null)
        val long = locationPreferences.getString("location_pref_long", null)

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 2000, 10f
        ) {

            val edit = locationPreferences.edit()
            edit.putString("location_pref_lat", it.latitude.toString()).apply()
            edit.putString("location_pref_long", it.longitude.toString()).apply()

            Log.d("Long", "${it.longitude}")
            Log.d("Lat", "${it.latitude}")
        }

        Log.d("data_long", "${long}")
        Log.d("data_lat", "${lat}")

        WeatherService(context).updateWeatherWidget(
            lat = lat!!,
            lon = long!!,
            remoteViews = views,
            appWidgetId, appWidgetManager
        )

        // appWidgetManager.updateAppWidget(appWidgetId, views)

        val alarmHandler = AlarmHandler(context)
        alarmHandler.cancelAlarmManager()
        alarmHandler.setAlarmManager()
    }
}

