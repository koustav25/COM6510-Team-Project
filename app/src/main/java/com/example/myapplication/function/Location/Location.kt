package com.example.myapplication.function.Location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.function.Notification.Notification
import com.example.myapplication.todoViewModels.LocationViewModel
import com.example.myapplication.todoViewModels.TodoViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

object Location {
    //get the latitude and longitude and then open the google map

    @Composable
    fun GetLocation(context: Context):Coordinate? {
        var coordinate by remember { mutableStateOf<Coordinate?>(null) }
        val locationViewModel = viewModel<LocationViewModel>()
        GeoLocationService.locationViewModel = locationViewModel
        if (!hasPermission(context)){
            requestFineLocationPermission(context)
        }else{
            val context = LocalContext.current
            val locationManager =
                context.getSystemService(ComponentActivity.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                IconButton(onClick ={
                    val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    val lat = location?.latitude.toString()
                    val lon = location?.longitude.toString()
                    coordinate = Coordinate(lat,lon)
                    //OpenMaps(lat,lon,context)

                    Log.d("location","location ${coordinate}")
                }){
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .size(10.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Outlined.Place, contentDescription = "Location")
                        Text("Location", fontSize = 8.sp)
                    }

                }
            }
        }
        Log.d("coordinate","location ${coordinate}")
  return coordinate
    }

    //get the GPS permission
    val GPS_LOCATION_PERMISSION_REQUEST = 1
    //A pop-up window will appear,allowing you tp choose the permission
    fun requestFineLocationPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity, // Ensure you have the appropriate context type
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            GPS_LOCATION_PERMISSION_REQUEST
        )
    }
    //judge whether app has the permission of GPS
    fun hasPermission(context: Context): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(
                    context.applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
                )
    }
    fun OpenMaps(lat: String,lon: String,context: Context){
        val intentUri = Uri.parse("geo:$lat,$lon")
        val intent = Intent(Intent.ACTION_VIEW,intentUri)
        intent.setPackage("com.google.android.apps.maps")
        context.startActivity(intent)
    }

 @SuppressLint("MissingPermission")
 @Composable
 fun Resume(context: Context){
     val locationViewModel : LocationViewModel = viewModel()
     val locationManager =
         context.getSystemService(ComponentActivity.LOCATION_SERVICE) as LocationManager
     if (!hasPermission(context)){
         requestFineLocationPermission(context)
     }else{
         val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
         if(location!=null){
             GeoLocationService.updateLatestLocation(location)
             if(locationViewModel.isEmpty()){
                 val currentDateTime = LocalDateTime.now()
                 val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                 Notification.SetNotification(currentDateTime.format(formatter) ,context, "device is away")
             }
         }
         locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0.0f,GeoLocationService)
         Log.d("location","$locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0.0f,GeoLocationService)")
     }
 }

}