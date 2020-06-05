package com.example.weatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeTabHost()
    }

    private fun initializeTabHost() {
        if (tabHost!= null) {
            tabHost.setup()
            var spec = tabHost.newTabSpec("Текущая погода")
            spec.setContent(R.id.todayTab)
            spec.setIndicator("Текущая погода")
            tabHost.addTab(spec)

            spec = tabHost.newTabSpec("5 дней")
            spec.setContent(R.id.forecastTab)
            spec.setIndicator("прогноз на 5 дней")
            tabHost.addTab(spec)
        }
    }
}
