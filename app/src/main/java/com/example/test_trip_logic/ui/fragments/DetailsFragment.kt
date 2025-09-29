package com.example.test_trip_logic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.test_trip_logic.R
import com.example.test_trip_logic.data.repository.TripRepository
import com.example.test_trip_logic.data.view_model.TripViewModel
import com.example.test_trip_logic.data.view_model.TripViewModelFactory
import com.example.test_trip_logic.databinding.FragmentDetailsBinding
import kotlin.getValue


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    val args: DetailsFragmentArgs by navArgs()
    private val tripViewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(TripRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val trip = tripViewModel.getTripById(args.trip.id)
            binding.tvTripDate.text = trip?.time ?:"time"
            binding.tvTripLat.text = trip?.lat.toString()
            binding.tvTripName.text = trip?.name
            binding.tvTripLng.text = trip?.lng.toString()


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
