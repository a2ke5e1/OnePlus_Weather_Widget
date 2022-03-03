package com.a2krocks.widget.services

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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

class WeatherService(context: Context) {



    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(WeatherInterface::class.java)
    private val retrofitData = retrofitBuilder.getData("London", context.getString(R.string.apiId)  )


    fun getWeather(callback: (temp: Double?, desc: String?) -> Unit) {
        retrofitData.enqueue(object : Callback<WeatherData?> {
            override fun onResponse(call: Call<WeatherData?>, response: Response<WeatherData?>) {
                val responseBody = response.body()
                callback(responseBody?.main?.temp, responseBody?.weather?.get(0)?.description)
            }

            override fun onFailure(call: Call<WeatherData?>, t: Throwable) {
                callback(null, null)
            }
        })
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
    split(" ").joinToString(" ") { it.lowercase(Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }