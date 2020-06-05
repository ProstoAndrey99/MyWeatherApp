package com.example.weatherapplication.forecast

import com.google.gson.annotations.SerializedName

data class ForecastMainData (@SerializedName("temp") var temperature: Double,
                             @SerializedName("pressure") var pressure : Double,
                             @SerializedName("humidity") var humidity : Double)
