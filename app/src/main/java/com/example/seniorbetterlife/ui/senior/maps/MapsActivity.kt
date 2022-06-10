package com.example.seniorbetterlife.ui.senior.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.seniorbetterlife.R

import com.example.seniorbetterlife.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.navMapsFragment) // przypisanie navControllera
        setupActionBarWithNavController(navController)
    }
}