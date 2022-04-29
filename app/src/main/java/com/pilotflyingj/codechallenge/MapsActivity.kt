package com.pilotflyingj.codechallenge

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.pilotflyingj.codechallenge.network.models.ApiSite
import com.pilotflyingj.codechallenge.network.models.SpaceAvailability
import com.pilotflyingj.codechallenge.viewmodel.MapsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private val mapsViewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        (supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // TODO: center camera on the entire USA
        // TODO: subscribe to live data for view model so that markers get added
        // TODO: make sure rotation works
        mapsViewModel.locations().observe(this) { locations ->

            for (location in locations) {
                val currentLocationLatLng = LatLng(location.latitude, location.longitude)
                val color = if (location.storeName == "Flying J") {
                    39.901f
                } else {
                    351.544f
                }
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(currentLocationLatLng)
                        .title(location.storeName)
                        .snippet(location.address)
                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                )
                marker?.tag = locations.indexOf(location)
            }

            //default camera to middle of USA
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(39.8097343, -98.5556199)))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(3.5f))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // Get store id from the marker.
        Log.d("Test", "CLICKED")
        val id = marker.tag as? Int
        lateinit var location: ApiSite
        lateinit var spaceAvailability: SpaceAvailability
        mapsViewModel.locations().observe(this) { locations ->
            if (id != null) {
                location = locations[id]
                spaceAvailability = location.spaceAvailability[0]
            }
        }

        marker.showInfoWindow()
        Toast.makeText(
            this,
            "${marker.title}'s store number is ${location.storeNum}. Available spaces: ${spaceAvailability.available}.",
            Toast.LENGTH_SHORT
        ).show()

        return false
    }
}