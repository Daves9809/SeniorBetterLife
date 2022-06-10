package com.example.seniorbetterlife.ui.senior.pillReminder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.databinding.ActivityMapsBinding
import com.example.seniorbetterlife.databinding.ActivityPillReminderBinding

class PillReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPillReminderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPillReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val navController = findNavController(R.id.fragmentContainerView) // przypisanie navControllera
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
    }
}