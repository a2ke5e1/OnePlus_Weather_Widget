package com.a2krocks.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.RemoteViews
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
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
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

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.oneplus_widget)
        WeatherService(context).getWeather("Haldia").enqueue(object : Callback<WeatherData?> {
            override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>) {
                if (response.isSuccessful) {

                    val temp = response.body()?.main?.temp
                    val desc = response.body()?.weather?.get(0)?.description

                    val tempText = "${temp?.roundToInt()}°"
                    val descText = desc.toString().capitalizeWords()


                    val spannable = SpannableString(tempText)
                    spannable.setSpan(
                        ForegroundColorSpan(Color.WHITE),
                        1,
                        tempText.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    // Construct the RemoteViews object
                    views.setTextViewText(R.id.tempTextView, spannable)
                    views.setTextViewText(R.id.descTextView, descText)

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                } else {
                    // Construct the RemoteViews object
                    views.setTextViewText(R.id.tempTextView, "Failed to load.")
                    views.setTextViewText(R.id.descTextView, "Failed to load.")

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }

            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                // Construct the RemoteViews object
                views.setTextViewText(R.id.tempTextView, "Failed to load.")
                views.setTextViewText(R.id.descTextView, "Failed to load.")

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        })


    }
}

