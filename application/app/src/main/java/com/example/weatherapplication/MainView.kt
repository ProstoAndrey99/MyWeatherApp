package com.example.weatherapplication
import com.example.weatherapplication.forecast.ForecastItemViewModel
import com.example.weatherapplication.today.CurrentWeatherItemViewModel

interface MainView {
    fun showSpinner()
    fun hideSpinner()
    fun updateForecast(forecasts: MutableList<ForecastItemViewModel>)
    fun showErrorToast(errorType: ErrorTypes)
    fun updateWeather(weather : CurrentWeatherItemViewModel)
}
