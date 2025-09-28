package com.example.test_trip_logic.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test_trip_logic.data.model.TripModel
import com.example.test_trip_logic.databinding.ItemTripBinding

class TripAdapter(
    private val trips: List<TripModel>,
    private val onItemClick: (TripModel) -> Unit
) : RecyclerView.Adapter<TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TripViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(trips[position])
    }

    override fun getItemCount() = trips.size
}