package com.a2krocks.widget.services

import android.annotation.SuppressLint
import android.content.Context
import com.a2krocks.widget.R
import com.a2krocks.widget.services.data.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

class WeatherService(val context: Context) {


    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"


    fun getWeather(location: String): Call<WeatherData> {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherInterface::class.java)
        val retrofitData = retrofitBuilder.getData(
            location, context.getString(R.string.apiId)
        )
        return retrofitData
    }

}

interface WeatherInterface {
    @GET("weather?&units=metric")
    fun getData(
        @Query("q") location: String,
        @Query("appid") apiKey: String
    ): Call<WeatherData>
}

@SuppressLint("DefaultLocale")
fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") {
        it.lowercase(Locale.getDefault())
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }