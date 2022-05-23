package com.example.seniorbetterlife.helpPart

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.databinding.ActivityHelpMapBinding
import com.google.android.gms.location.*

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil

class HelpActivity : AppCompatActivity(), OnMapReadyCallback, AddTaskDialogFragment.DialogListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityHelpMapBinding
    lateinit var client: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fab = binding.fabAddTask
        //popup window(new Dialog) -> add new task
        fab.setOnClickListener {
            var dialog = AddTaskDialogFragment()

            dialog.show(supportFragmentManager,"addTaskDialog")
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync(this)
        client = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        checkLocationPermission()
        mMap = googleMap
        client.lastLocation.addOnCompleteListener {
            val latitude: Double? = it.result?.latitude
            val longitude: Double? = it.result?.longitude
            val pos = LatLng(latitude!!, longitude!!)
            val geoCoder = Geocoder(this)
            val matches = geoCoder.getFromLocation(latitude, longitude, 1)
            Log.d("CurrentLocation", matches[0].toString())
            addMarker(googleMap, pos)
            zoomToCurrentLocation(pos)
            addPolyline(pos)
        }

    }
    private fun addMarker(
        googleMap: GoogleMap,
        pos: LatLng
    ) {
        googleMap.addMarker(MarkerOptions().position(pos).title("My location"))
    }
    private fun zoomToCurrentLocation(pos: LatLng) {
        mMap.apply {
            animateCamera(CameraUpdateFactory.newLatLngZoom(pos, DEFAULT_ZOOM))
        }
    }
    private fun addPolyline(pos: LatLng) {
        val east: LatLng = SphericalUtil.computeOffset(pos, 500.0, 90.0)
        val south: LatLng = SphericalUtil.computeOffset(pos, 500.0, 180.0)
        val west: LatLng = SphericalUtil.computeOffset(pos, 500.0, 270.0)
        val north: LatLng = SphericalUtil.computeOffset(pos, 500.0, 360.0)
        mMap.addPolyline(
            PolylineOptions().add(pos)
                .add(pos)
                .add(east)
                .add(south)
                .add(west)
                .add(north)
                .width(6f)
                .color(Color.GREEN)
        )
    }

    //download data from dialog and create new task
    override fun onDialogPositiveClick(kindOfHelp: String, phoneNumber: String) {
        Toast.makeText(applicationContext,"$kindOfHelp, $phoneNumber",Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
    }

    private fun checkLocationPermission(): Boolean {
        var state = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                state = true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1000
                )
            }

        } else state = true
        return state
    }

    companion object {
        const val DEFAULT_ZOOM = 14.0F
    }

}