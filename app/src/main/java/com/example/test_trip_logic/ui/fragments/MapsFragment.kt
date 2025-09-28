package com.example.test_trip_logic.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.test_trip_logic.R
import com.example.test_trip_logic.data.repository.TripRepository
import com.example.test_trip_logic.data.view_model.TripViewModel
import com.example.test_trip_logic.data.view_model.TripViewModelFactory

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.getValue

class MapsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGranted: Boolean ->
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
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        checkLocationPermission()
        val location = LatLng(args.trip.lat, args.trip.lng)
        googleMap.addMarker(MarkerOptions().position(location).title(args.trip.name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))

        googleMap.setOnMapLongClickListener { latLng ->

            googleMap.addMarker(MarkerOptions().position(latLng).title("New Marker"))

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12f))

            Toast.makeText(requireContext(), "New location: ${latLng.latitude}, ${latLng.longitude}", Toast.LENGTH_SHORT).show()

            tripViewModel.updateTripLocation(
                id = args.trip.id,
                newLat = latLng.latitude,
                newLng = latLng.longitude
            )

        }
    }

    private fun checkLocationPermission() {
        when {
            //1
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                enableUserLocationOnMap()
            }

            //2
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("We need access to your location to show your current position on the map.")
                    .setNegativeButton("Cancel") { dialog, which ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("OK") { dialog, which ->
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                    .show()
            }

            //3
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun enableUserLocationOnMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        }
    }


}