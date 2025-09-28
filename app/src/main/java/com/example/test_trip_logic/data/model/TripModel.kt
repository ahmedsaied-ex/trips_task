package com.example.test_trip_logic.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TripModel(
    val id: String,
    val name: String,
    val time: String,
    var lat: Double,
    var lng: Double
): Parcelable