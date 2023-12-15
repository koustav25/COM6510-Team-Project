package com.example.myapplication.todoViewModels

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.todoDatabase.TodoDatabase
import com.example.myapplication.todoEntities.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class LocationViewModel (app: Application): AndroidViewModel(app) {
    private val context = getApplication<Application>().applicationContext
    private val dao = TodoDatabase.getDatabase(context).todoDao()

    // TODO synchronize/lock
    var tracking by mutableStateOf(false)
    var location:Location? by mutableStateOf<Location?>(null)
        private set
    var latitude by mutableStateOf<Double?>(null)
        private set
    var longitude by mutableStateOf<Double?>(null)
        private set

    private fun _setLocation(newLocation: Location?) {
        location = newLocation
        latitude = location?.latitude ?: null
        longitude = location?.longitude ?: null
        Log.d("latest Location","$location")

    }

    fun updateLocation(newLocation: Location) {
        _setLocation(newLocation)
    }

    fun invalidate() {
        _setLocation(null)
    }

    fun valid():Boolean {
        return (location != null)
    }

    fun isEmpty(latitude:Double,longitude:Double,id:Long): Boolean = runBlocking{
        val todosList = dao.getLatitudeAndLongitude(String.format("%.3f",latitude),String.format("%.3f",longitude),id).firstOrNull()
        todosList.isNullOrEmpty()
    }

}