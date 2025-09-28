package com.example.test_trip_logic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.test_trip_logic.R
import com.example.test_trip_logic.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)


        binding.tvTripDate.text = args.trip.time
        binding.tvTripLat.text = args.trip.lat.toString()
        binding.tvTripName.text = args.trip.name
        binding.tvTripLng.text = args.trip.lng.toString()

        binding.fabMap.setOnClickListener {
            val action = DetailsFragmentDirections.actionDetailsFragmentToMapsFragment(
                trip = args.trip,
            )
            findNavController().navigate(action)
        }
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
