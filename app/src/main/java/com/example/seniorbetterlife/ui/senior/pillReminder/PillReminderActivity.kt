package com.example.seniorbetterlife.ui.senior.pillReminder

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.databinding.ActivityPillReminderBinding

@Suppress("DEPRECATION")
class PillReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPillReminderBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPillReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setting navController and connecting with aciton Bar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val appBarCompatActivity = AppBarConfiguration(navController.graph)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupActionBarWithNavController(navController, appBarCompatActivity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === android.R.id.home) {
            //Title bar back press triggers onBackPressed()
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}