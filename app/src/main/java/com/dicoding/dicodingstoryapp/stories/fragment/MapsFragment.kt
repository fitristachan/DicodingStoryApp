package com.dicoding.dicodingstoryapp.stories.fragment

import android.content.res.Resources
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dicoding.dicodingstoryapp.R
import com.dicoding.dicodingstoryapp.databinding.FragmentMapsBinding
import com.dicoding.dicodingstoryapp.viewmodel.StoriesViewModel
import com.dicoding.dicodingstoryapp.viewmodel.StoriesViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding

    private val storiesViewModel by viewModels<StoriesViewModel> {
        StoriesViewModelFactory.getInstance(requireActivity().application)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        storiesViewModel.getStoriesWithLocation().observe(viewLifecycleOwner) { stories ->
            stories.listStory.forEach { story ->
                val lat = story.lat
                val lon = story.lon
                if (lat != null && lon != null) {
                    val latLng = LatLng(lat, lon)
                    googleMap.addMarker(
                        MarkerOptions().position(latLng).title(story.name)
                            .snippet(story.description)
                    )
                }
            }
        }
        val indonesiaCenter = LatLng(-2.5489, 118.0149)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesiaCenter, 5f))
        setMapStyle(googleMap)
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun setMapStyle(googleMap: GoogleMap) {
        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.map_style
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    companion object {
        private const val TAG = "MapsFragment"
    }
}