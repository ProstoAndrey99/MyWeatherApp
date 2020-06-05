package com.example.weatherapplication.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.R

import kotlinx.android.synthetic.main.forecast.view.*
import java.text.SimpleDateFormat
import java.util.*


class ForecastAdapter() : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    var forecastList = mutableListOf<ForecastItemViewModel>()

    fun addForecast(list: MutableList<ForecastItemViewModel>){
        forecastList.clear()
        forecastList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        forecastList[position].let {
            holder.bind(forecastElement = it)
        }
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(forecastElement : ForecastItemViewModel) {
            itemView.temperature.text = "${forecastElement.temp}°C"
            itemView.description.text =  "${forecastElement.description}"
            itemView.date.text = getDate(forecastElement.date+forecastElement.timezone)
            Glide.with(itemView.context)
                .load("http://openweathermap.org/img/wn/${forecastElement.icon}.png")
                .into(itemView.weatherIcon)
        }

        private fun  getDate(date: Long): String {
            val timeFormatter = SimpleDateFormat("HH:mm, EE")
            return timeFormatter.format(Date(date*1000L))
        }

        private fun  getDay(date: Long): String {
            val timeFormatter = SimpleDateFormat("EE")
            return timeFormatter.format(Date(date*1000L))
        }
    }
}