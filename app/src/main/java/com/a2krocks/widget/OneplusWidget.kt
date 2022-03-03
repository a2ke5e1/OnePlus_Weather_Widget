package com.a2krocks.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.RemoteViews
import com.a2krocks.widget.services.WeatherService
import com.a2krocks.widget.services.capitalizeWords
import java.util.*
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

        WeatherService(context).getWeather("Haldia") { temp, desc ->



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
            val views = RemoteViews(context.packageName, R.layout.oneplus_widget)
            views.setTextViewText(R.id.tempTextView, spannable)
            views.setTextViewText(R.id.descTextView, descText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }


    }
}

