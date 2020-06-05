package com.example.weatherapplication.forecast
import com.google.gson.annotations.SerializedName

data class City(@SerializedName("name") var cityName : String,
                @SerializedName("country") var country : String,
                @SerializedName("timezone") var timezone : Long)
