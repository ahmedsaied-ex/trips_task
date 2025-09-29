package com.example.test_trip_logic.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.test_trip_logic.data.model.TripModel
import com.example.test_trip_logic.databinding.ItemTripBinding

class TripViewHolder(
    private val binding: ItemTripBinding,
    private val onItemClick: (TripModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(trip: TripModel) {
        binding.tvTripName.text = trip.name
        binding.tvTripDate.text = trip.time
        binding.tvTripLat.text = trip.lat.toString()
        binding.tvTripLng.text = trip.lng.toString()


        binding.root.setOnClickListener {
            onItemClick(trip)
        }
    }
}