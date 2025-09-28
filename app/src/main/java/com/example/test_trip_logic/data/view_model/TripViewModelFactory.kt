package com.example.test_trip_logic.data.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.test_trip_logic.data.repository.TripRepository

class TripViewModelFactory (private val repo: TripRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}