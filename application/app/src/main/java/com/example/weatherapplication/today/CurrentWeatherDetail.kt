package com.example.weatherapplication.today


import com.example.weatherapplication.forecast.WeatherDescription
import com.google.gson.annotations.SerializedName


data class CurrentWeatherDetail(@SerializedName("temp") var temp : Double,
                                @SerializedName("weather") var description : List<WeatherDescription>,
                                @SerializedName("pressure") var pressure : Double,
                                @SerializedName("humidity") var humidity : Double)
