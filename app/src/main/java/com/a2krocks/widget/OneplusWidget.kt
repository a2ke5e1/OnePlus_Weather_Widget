package com.a2krocks.widget

import android.Manifest
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.a2krocks.widget.services.AlarmHandler
import com.a2krocks.widget.services.WeatherService
import com.a2krocks.widget.services.capitalizeWords
import com.a2krocks.widget.services.data.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

/**
 * Implementation of App Widget functionality.
 */
class OneplusWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
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
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle?
    ) {
        Log.d("WidgetService", "Widget is Starting")
        updateAppWidget(context, appWidgetManager, appWidgetId)
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    private fun updateAppWidget(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.oneplus_widget)
        val locationPreferences = context.getSharedPreferences(WeatherService.LOCATION_PREFERENCE, Context.MODE_PRIVATE)
        val settingsPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val lat = locationPreferences.getString(WeatherService.LOCATION_PREFERENCE_LATITUDE, null)
        val long = locationPreferences.getString(WeatherService.LOCATION_PREFERENCE_LONGITUDE, null)
        val isLocationSaved = lat != null && long != null


        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 2000, 10f
        ) {

            val edit = locationPreferences.edit()
            edit.putString(WeatherService.LOCATION_PREFERENCE_LATITUDE, it.latitude.toString()).apply()
            edit.putString(WeatherService.LOCATION_PREFERENCE_LONGITUDE, it.longitude.toString()).apply()

            Log.d("location.get", "Got Location from GPS")
            Log.d("location.set.local", "Saved Location from GPS")
        }
        Log.d("location.get.local", "Got Location from Saved : $isLocationSaved")

        if (isLocationSaved) {
            val data = WeatherService().retrofitBuilder.getData(
                lat!!, long!!, context.getString(R.string.apiId)
            )
            data.enqueue(object : Callback<WeatherData> {
                override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                    Log.d("response", response.toString())
                    if (response.isSuccessful) {
                        val weatherData = response.body()
                        if (weatherData != null) {
                            val temperature = weatherData.main.temp
                            val roundedTemperature = temperature.roundToInt()


                            val temperatureFormatted = "${roundedTemperature}°"
                            val weatherDescription = weatherData.weather[0].description.capitalizeWords()

                            val spannable = SpannableString(temperatureFormatted)
                            var hsv = FloatArray(3)
                            val accentColor = ContextCompat.getColor(context, R.color.widget_text_color)
                            Color.colorToHSV(accentColor, hsv)

                            hsv[2] = 0.92f

                            val useOnePlusColor = !settingsPreferences.getBoolean("system_color", false)
                            val oneplusColor = ContextCompat.getColor(context, R.color.oneplus_red)

                            spannable.setSpan(
                                ForegroundColorSpan(
                                    if (useOnePlusColor) {
                                        oneplusColor
                                    } else {
                                        Color.HSVToColor(hsv)
                                    }
                                ), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            spannable.setSpan(
                                RelativeSizeSpan(0.9f),
                                temperatureFormatted.length - 1,
                                temperatureFormatted.length,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            views.setViewVisibility(R.id.tempTextView, View.VISIBLE)
                            views.setTextViewTextSize(R.id.descTextView, TypedValue.COMPLEX_UNIT_SP, 25f)

                            views.setTextViewText(R.id.tempTextView, spannable)
                            views.setTextViewText(R.id.descTextView, weatherDescription)

                        }
                        Log.d("response.weatherData.isNull", (weatherData == null).toString())
                        Log.d("response.isSuccessful", response.isSuccessful.toString())

                    } else {
                        val errorBody = response.errorBody()
                        Log.d("response.isUnsuccessful", errorBody.toString())
                    }

                    updateAndTriggerAppWidgetUpdate(context, appWidgetManager, appWidgetId, views)
                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    Log.e("response.failure", t.stackTraceToString())
                    updateAndTriggerAppWidgetUpdate(context, appWidgetManager, appWidgetId, views)
                }

            })

        }

        updateAndTriggerAppWidgetUpdate(context, appWidgetManager, appWidgetId, views)
    }

    private fun updateAndTriggerAppWidgetUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, views: RemoteViews) {
        appWidgetManager.updateAppWidget(appWidgetId, views)
        val alarmHandler = AlarmHandler(context)
        alarmHandler.cancelAlarmManager()
        alarmHandler.setAlarmManager()
    }
}

