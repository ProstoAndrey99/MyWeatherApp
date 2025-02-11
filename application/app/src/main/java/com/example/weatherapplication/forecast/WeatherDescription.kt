package com.example.weatherapplication.forecast

import com.google.gson.annotations.SerializedName

data class WeatherDescription(@SerializedName("main") var main : String,
                              @SerializedName("description") var description: String,
                              @SerializedName("icon") var icon: String)
