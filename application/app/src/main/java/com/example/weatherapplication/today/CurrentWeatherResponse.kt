package com.example.weatherapplication.today

import com.example.weatherapplication.forecast.WeatherDescription
import com.google.gson.annotations.SerializedName


data class CurrentWeatherResponse (@SerializedName("main") var currentWeather : CurrentWeatherDetail,
                                   @SerializedName("weather") var weatherDescription: List<WeatherDescription>,
                                   @SerializedName("name") var cityName : String,
                                   @SerializedName("wind") var wind : WindDetail,
                                   @SerializedName("clouds") var clouds : CloudsDetail)


