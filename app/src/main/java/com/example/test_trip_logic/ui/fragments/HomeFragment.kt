package com.example.test_trip_logic.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_trip_logic.R
import com.example.test_trip_logic.data.repository.TripRepository
import com.example.test_trip_logic.data.view_model.TripViewModel
import com.example.test_trip_logic.data.view_model.TripViewModelFactory
import com.example.test_trip_logic.databinding.FragmentHomeBinding
import com.example.test_trip_logic.ui.adapter.TripAdapter

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val tripViewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(TripRepository(requireContext()))
    }


    lateinit var tripAdapter: TripAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tripViewModel.trips.observe(viewLifecycleOwner) { trips ->
            tripAdapter = TripAdapter(trips) { trip ->
                val action = HomeFragmentDirections
                    .actionHomeFragmentToDetailsFragment(trip)
                findNavController().navigate(action)
            }
            binding.rvTrips.layoutManager= LinearLayoutManager(requireContext())

            binding.rvTrips.adapter = tripAdapter


        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}