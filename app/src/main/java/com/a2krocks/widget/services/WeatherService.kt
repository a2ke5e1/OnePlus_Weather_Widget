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