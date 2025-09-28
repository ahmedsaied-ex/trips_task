package com.example.test_trip_logic.data.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test_trip_logic.data.model.TripModel
import com.example.test_trip_logic.data.repository.TripRepository

class TripViewModel(private val repo : TripRepository) : ViewModel() {
    private val _trips = MutableLiveData<List<TripModel>>()
    val trips: LiveData<List<TripModel>> = _trips

    private val _selectedTrip = MutableLiveData<TripModel?>()
    val selectedTripModel: LiveData<TripModel?> = _selectedTrip

    init {
        _trips.value = repo.loadTrips()
    }



    fun updateTripLocation(id: String, newLat: Double, newLng: Double) {
        val currentList = _trips.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == id }
        if (index != -1) {
            val updated = currentList[index].copy(lat = newLat, lng = newLng)
            currentList[index] = updated
            _trips.value = currentList
            _selectedTrip.value = if (_selectedTrip.value?.id == id) updated else _selectedTrip.value
            repo.saveTrips(currentList)
        }else {
            Log.e("TripViewModel", "TripModel with ID $id not found")
        }
    }

    fun getTripById(id: String): TripModel? {
        return _trips.value?.find { it.id == id }
    }

}