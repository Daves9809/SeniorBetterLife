package com.example.seniorbetterlife.helpPart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.databinding.ActivityHelpMapBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class HelpActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityHelpMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fab = binding.fabAddTask
        fab.setOnClickListener { view ->
            Snackbar.make(view,"Here's a snackbar",Snackbar.LENGTH_LONG).show()
            var dialog = AddTaskDialogFragment()

            dialog.show(supportFragmentManager,"addTaskDialog")
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}