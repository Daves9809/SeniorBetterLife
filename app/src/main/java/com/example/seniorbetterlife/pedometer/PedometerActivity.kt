package com.example.seniorbetterlife.pedometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.seniorbetterlife.databinding.ActivityPedometerBinding

class PedometerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPedometerBinding
    val viewModel: PedometerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedometerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}