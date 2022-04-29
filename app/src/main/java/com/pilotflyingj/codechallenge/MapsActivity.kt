package com.pilotflyingj.codechallenge

import android.content.ContentValues
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
import com.google.android.gms.maps.model.MarkerOptions
import com.pilotflyingj.codechallenge.network.models.ApiSite
import com.pilotflyingj.codechallenge.network.models.SpaceAvailability
import com.pilotflyingj.codechallenge.viewmodel.MapsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mapsViewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        (supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment)?.getMapAsync(this)
    }

    private val markerListener = GoogleMap.OnMarkerClickListener { marker ->
        lateinit var location: ApiSite
        lateinit var spaceAvailability: SpaceAvailability
        // Get store id from the marker.
        val id = marker.tag as? Int

        mapsViewModel.locations().observe(this) { locations ->
            if (id != null) {
                try {
                    location = locations[id]
                    spaceAvailability = location.spaceAvailability[0]

                    Toast.makeText(
                        this,
                        "${marker.title}'s store number is ${location.storeNum}. Available spaces: ${spaceAvailability.available}.",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Log.e(ContentValues.TAG, "${e.message}")
                }
            }
        }

        return@OnMarkerClickListener false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //default camera to middle of USA
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(39.8097343, -98.5556199)))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(3.5f))

        mapsViewModel.locations().observe(this) { locations ->

            for (location in locations) {
                val locationLatLng = LatLng(location.latitude, location.longitude)
                val color = if (location.storeName == "Flying J") {
                    39.901f
                } else {
                    351.544f
                }
                val marker = googleMap.addMarker(
                    MarkerOptions()
                        .position(locationLatLng)
                        .title(location.storeName)
                        .snippet(location.address)
                        .icon(BitmapDescriptorFactory.defaultMarker(color))
                )
                marker?.tag = locations.indexOf(location)
            }

            googleMap.setOnMarkerClickListener(markerListener)
        }
    }

}