package com.example.weatherapplication.today


data class CurrentWeatherItemViewModel (val temp : Int,
                                        val icon : String = "010d",
                                        val description : String = "Без описания",
                                        val city : String = "",
                                        val humidity : Int,
                                        val pressure : Int,
                                        val windSpeed : Int,
                                        val windDeg : Int,
                                        val clouds : Int)