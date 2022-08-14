package com.a2krocks.widget.services

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.a2krocks.widget.R
import com.a2krocks.widget.services.data.WeatherData
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import kotlin.math.roundToInt

class WeatherService(private val context: Context) {


    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val pref = context.getSharedPreferences("WeatherData", Context.MODE_PRIVATE)
    val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(WeatherInterface::class.java)


    /**
     *  It updates Widgets with Weather Data.
     *
     *  @param lat Latitude
     *  @param lon Longitude
     *  @param remoteViews RemoteViews
     *  @param appWidgetId App Widget Id
     *  @param appWidgetManager App Widget Manager
     *
     *  @author Apurv Ajay Kumar ( aakapurv@gmail.com)
     *
     */
    fun updateWeatherWidget(
        lat: String,
        lon: String,
        remoteViews: RemoteViews,
        appWidgetId: Int,
        appWidgetManager: AppWidgetManager
    ) {
        val data = retrofitBuilder.getData(
            lat, lon, context.getString(R.string.apiId)
        )
        data.enqueue(object : Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    if (weatherData != null) {
                        Toast.makeText(context, "Updated Weather Data", Toast.LENGTH_LONG).show()
                        updateWeatherView(remoteViews = remoteViews, weatherData = weatherData)
                    } else {
                        Toast.makeText(context, "Weather Data is null", Toast.LENGTH_LONG).show()
                        updateWeatherFromPreviousData(remoteViews = remoteViews)
                    }
                } else {
                    val errorBody = response.errorBody()
                    Toast.makeText(context, "Failed to load Weather Data", Toast.LENGTH_LONG).show()
                    Log.d("response.isUnsuccessful", errorBody.toString())
                    updateWeatherFromPreviousData(remoteViews = remoteViews)
                }
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Log.e("response.failure", t.stackTraceToString())
                Toast.makeText(context, "Failed to load Weather Data with throwable", Toast.LENGTH_LONG).show()
                updateWeatherFromPreviousData(remoteViews = remoteViews)
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }

        })
    }

    private fun updateWeatherFromPreviousData(remoteViews: RemoteViews) {
        val weatherDataJSON = pref.getString("WeatherData", null)
        weatherDataJSON.let {
            val weatherData = Gson().fromJson(weatherDataJSON, WeatherData::class.java)
            updateWeatherView(remoteViews = remoteViews, weatherData = weatherData)
        }
    }

    private fun setWeatherPref(weatherData: WeatherData) {
        val edit = pref.edit()
        edit.putString("WeatherData", Gson().toJson(weatherData))
        edit.apply()
    }

    private fun updateWeatherView(remoteViews: RemoteViews, weatherData: WeatherData) {
        setWeatherPref(weatherData = weatherData)
        val temperature = weatherData.main.temp
        val roundedTemperature = temperature.roundToInt()


        val temperatureFormatted = "${roundedTemperature}°"
        val weatherDescription = weatherData.weather[0].description.capitalizeWords()

        val spannable = SpannableString(temperatureFormatted)
        spannable.setSpan(
            ForegroundColorSpan(Color.WHITE),
            1,
            temperatureFormatted.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        remoteViews.setTextViewText(R.id.tempTextView, spannable)
        remoteViews.setTextViewText(R.id.descTextView, weatherDescription)
    }

    companion object {
        const val LOCATION_PREFERENCE = "location_pref"
        const val LOCATION_PREFERENCE_LATITUDE = "location_pref_lat"
        const val LOCATION_PREFERENCE_LONGITUDE = "location_pref_long"
    }

}

interface WeatherInterface {
    @GET("weather?&units=metric")
    fun getData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") apiKey: String
    ): Call<WeatherData>
}

@SuppressLint("DefaultLocale")
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { s ->
        s.lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }