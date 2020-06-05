package com.example.weatherapplication.forecast

import com.google.gson.annotations.SerializedName

data class ForecastWeatherResponse (@SerializedName("city") var city : City,
                                   @SerializedName("list") var forecast : List<ForecastDetail>,
                                   @SerializedName("weather") var weatherDescription: List<WeatherDescription>)


