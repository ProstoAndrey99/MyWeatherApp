package com.example.weatherapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapplication.dagger.DaggerOpenWeatherAPIComponent
import com.example.weatherapplication.dagger.OpenWeatherAPIModule
import com.example.weatherapplication.forecast.ForecastAdapter
import com.example.weatherapplication.forecast.ForecastItemViewModel
import com.example.weatherapplication.today.CurrentWeatherAdapter
import com.example.weatherapplication.today.CurrentWeatherItemViewModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.today.*
import java.util.*

class MainActivity : AppCompatActivity(), MainView {

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient


    private val presenter = MainPresenter(this)
    private var cityName : String = "Москва"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDI()
        setContentView(R.layout.activity_main)
        initializeTabHost()
        initializeCurrentWeatherList()
        initializeForecastList()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()

        //getCurrentWeather(cityName)
        //getForecast(cityName)
    }

    fun butclick (view: View) {
        var result : String = ""
        result += "Температура: "+temp.text.toString()+"\n"
        result += "Описание: "+description.text.toString()+"\n"
        result += "Влажность: "+humidity.text.toString()+"\n"
        result += "Облачность: "+clouds.text.toString()+"\n"
        result += "Давдение: "+pressure.text.toString()+"\n"
        result += "Скорость ветра: "+windSpeed.text.toString()+"\n"
        result += "Направление ветра: "+windDirection.text.toString()+"\n"
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, result)
        startActivity(Intent.createChooser(shareIntent, "Поделиться"))
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {

                        val gcd = Geocoder(this, Locale.getDefault())
                        val addresses =
                            gcd.getFromLocation(location.latitude, location.longitude, 1)
                        if (addresses.size > 0) {
                            //cityName = addresses[0].locality
                            val ci = addresses[0].locality
                            getCurrentWeather(ci)
                            getForecast(ci)
                        } else {
                            //cityName = "Минск"
                            val ci = "Минск"
                            getCurrentWeather(ci)
                            getForecast(ci)
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            //findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
            //findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val city = cityName
        outState.putString("cityName", city)
        outState.putInt("currentTab", tabHost.currentTab)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val city : String? = savedInstanceState.getString("cityName")
        if  (city != null) {
            cityName = city
            tabHost.currentTab = savedInstanceState.getInt("currentTab")
            getCurrentWeather(city)
            getForecast(city)
        }
    }

    override fun showSpinner() {
        forecastList.visibility = View.GONE
        todayList.visibility = View.GONE
        loadSpin.visibility = View.VISIBLE
    }

    override fun hideSpinner() {
        forecastList.visibility = View.VISIBLE
        todayList.visibility = View.VISIBLE
        loadSpin.visibility = View.GONE
    }

    override fun updateForecast(forecasts: MutableList<ForecastItemViewModel>) {
        forecastList.adapter?.safeCast<ForecastAdapter>()?.addForecast(forecasts)
    }

    override fun showErrorToast(errorType: ErrorTypes) {
        when (errorType) {
            ErrorTypes.CALL_ERROR -> toast("Проблемы с соединением")
            ErrorTypes.NO_RESULT_FOUND -> toast("Город не найден")
            ErrorTypes.MISSING_API_KEY -> toast("Добавьте API_KEY в gradle.properties")
        }
        loadSpin.visibility = View.GONE
    }

    override fun updateWeather(weather: CurrentWeatherItemViewModel) {
        todayList.adapter?.safeCast<CurrentWeatherAdapter>()?.addCurrentWeather(weather)
    }

    private fun getForecast(query: String) = presenter.getForecastForFiveDays(query)

    private fun getCurrentWeather(query: String) = presenter.getCurrentWeather(query)

    inline fun <reified T> Any.safeCast() = this as? T

    fun Activity.toast(toastMessage: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, toastMessage, duration).show()
    }

    private fun injectDI() {
        DaggerOpenWeatherAPIComponent
            .builder()
            .openWeatherAPIModule(OpenWeatherAPIModule())
            .build()
            .inject(presenter)
    }

    private fun initializeCurrentWeatherList() {
        todayList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = CurrentWeatherAdapter()
        }
    }

    private fun initializeForecastList() {
        forecastList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ForecastAdapter()
        }
    }

}
