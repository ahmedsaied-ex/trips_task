package com.example.test_trip_logic.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.test_trip_logic.MyOpject
import com.example.test_trip_logic.R
import com.example.test_trip_logic.data.repository.TripRepository
import com.example.test_trip_logic.data.view_model.TripViewModel
import com.example.test_trip_logic.data.view_model.TripViewModelFactory
import com.example.test_trip_logic.databinding.FragmentMapsBinding
import com.example.test_trip_logic.ui.MyBottomSheet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.getValue

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                enableUserLocationOnMap()
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_LONG).show()
            }
        }

    val args: MapsFragmentArgs by navArgs()
    private val tripViewModel: TripViewModel by activityViewModels {
        TripViewModelFactory(TripRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding.fabToggleEdit.setOnClickListener {
            val bottomSheet = MyBottomSheet()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)

        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        checkLocationPermission()
        val location = LatLng(args.trip.lat, args.trip.lng)
        googleMap.addMarker(MarkerOptions().position(location).title(args.trip.name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))

        googleMap.setOnMapLongClickListener { latLng ->

            if (MyOpject.isAdjustable) {
                googleMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))

                Toast.makeText(
                    requireContext(),
                    "New location: ${latLng.latitude}, ${latLng.longitude}",
                    Toast.LENGTH_SHORT
                ).show()

                tripViewModel.updateTripLocation(
                    id = args.trip.id,
                    newLat = latLng.latitude,
                    newLng = latLng.longitude
                )

            } else {
                Toast.makeText(requireContext(), "can't be edideted ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                enableUserLocationOnMap()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("We need access to your location to show your current position on the map.")
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("OK") { _, _ ->
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                    .show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun enableUserLocationOnMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }
    }

    override fun onDestroyView() {
        Log.d("OnDestroyView", "onDestroyView: ")
        MyOpject.isAdjustable=false
        super.onDestroyView()
        _binding = null
    }
}
