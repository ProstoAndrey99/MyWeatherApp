package com.example.weatherapplication.today


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapplication.R
import kotlinx.android.synthetic.main.today.view.*
import kotlinx.android.synthetic.main.forecast.view.weatherIcon



class CurrentWeatherAdapter() : RecyclerView.Adapter<CurrentWeatherAdapter.CurrentWeatherViewHolder>() {

    var currentWeatherList = mutableListOf<CurrentWeatherItemViewModel>()

    fun addCurrentWeather(item : CurrentWeatherItemViewModel){
        currentWeatherList.clear()
        currentWeatherList.add(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentWeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.today, parent, false)
        return CurrentWeatherViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: CurrentWeatherViewHolder, position: Int) {
        currentWeatherList[position].let {
            holder.bind(currentWeatherElement = it)
        }
    }

    override fun getItemCount(): Int {
        return currentWeatherList.size
    }

    class CurrentWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(currentWeatherElement : CurrentWeatherItemViewModel) {
            var sign: String = defineDegreeSign(currentWeatherElement.temp)
            itemView.temp.text = sign + "${currentWeatherElement.temp}°C"
            itemView.description.text = "${currentWeatherElement.description}"
            itemView.city.text = currentWeatherElement.city
            itemView.humidity.text = "${currentWeatherElement.humidity}%"
            itemView.windSpeed.text = "${currentWeatherElement.windSpeed} м/с"

            var alphDirect = currentWeatherElement.windDeg
            if (alphDirect == 90)
                itemView.windDirection.text = "В"
            else if (alphDirect == 180)
                itemView.windDirection.text = "Ю"
            else if (alphDirect == 270)
                itemView.windDirection.text = "З"
            else if (alphDirect == 0 or 360)
                itemView.windDirection.text = "C"
            else if (alphDirect in 1..89)
                itemView.windDirection.text = "С-В"
            else if (alphDirect in 91..179)
                itemView.windDirection.text = "Ю-В"
            else if (alphDirect in 181..269)
                itemView.windDirection.text = "Ю-З"
            else if (alphDirect in 271..359)
                itemView.windDirection.text = "С-З"
            else
                itemView.windDirection.text = "неизвестно"

            itemView.pressure.text = "${currentWeatherElement.pressure} hPa"

            itemView.clouds.text = "${currentWeatherElement.clouds}%"


            Glide.with(itemView.context)
                .load("http://openweathermap.org/img/wn/${currentWeatherElement.icon}@2x.png")
                .into(itemView.weatherIcon)
        }

        private fun defineDegreeSign(temp : Int): String {
            val sign: String
            if (temp > 0) {
                sign = "+"
            } else sign = ""
            return sign
        }

    }

}