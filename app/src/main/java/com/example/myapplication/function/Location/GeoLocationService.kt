package com.example.myapplication.function.Location

import android.location.Location
import android.location.LocationListener
import android.util.Log
import com.example.myapplication.function.Notification.Notification
import com.example.myapplication.todoViewModels.LocationViewModel

object GeoLocationService: LocationListener {
    var locationViewModel: LocationViewModel? = null

    override fun onLocationChanged(newLocation: Location) {
        locationViewModel?.updateLocation( newLocation )
        Log.i("geolocation", "Location updated")
    }

    fun updateLatestLocation(latestLocation: Location) {
        locationViewModel?.updateLocation( latestLocation )

        Log.i("geolocation", "Location set to latest")
    }
}
