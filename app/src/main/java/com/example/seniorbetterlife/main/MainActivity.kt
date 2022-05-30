package com.example.seniorbetterlife.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.databinding.ActivityMainBinding
import com.example.seniorbetterlife.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var auth: FirebaseAuth
    private val MAIN_DEBUG = "LOG_DEBUG"

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalSteps = 0
    private var previousTotalSteps = 0

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        //navigation configure
        configureNavigation()

        //sensor configure
        sensorConfigure()

    }


    private fun sensorConfigure() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                1);
        }
    }

    private fun configureNavigation() {
        val navMenu = binding.bottomNavigationView // przypisanie menuNawigacji(dolny panel)
        val navController = findNavController(R.id.navFragment) // przypisanie navControllera
        navMenu.setupWithNavController(navController) // "połączenie dolnej nawigacji z górną
        //val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,R.id.profileFragment))
        setupActionBarWithNavController(navController) // przypisuje nazwy "labeli" do górnego paska
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null){
            Toast.makeText(this,"No sensor detected on this device", Toast.LENGTH_SHORT).show()
        }else{
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.miPower -> signOut()
        }
        return true
    }


    /*
    wylogowanie użytkownika
     */
    private fun signOut() {
        auth.signOut()
        signOutUser()
    }

    private fun signOutUser() {
        val intent = Intent(this.applicationContext, LoginActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // flag activity zabezpieczaja przed powrotem
        }
        startActivity(intent)
        finish()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //reset daily steps
        if(isNewDay()) {
            previousTotalSteps = totalSteps
            startNewDay()
        }

        if(running){
            totalSteps = event!!.values[0].toInt()
            val currentSteps = totalSteps - previousTotalSteps
            saveData(currentSteps)
        }
    }

    private fun startNewDay() {
        val calendar = Calendar.getInstance()
        val today = calendar[Calendar.DAY_OF_YEAR]
        val sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("last_time_started", today)
        editor.apply()
    }

    //if new day comes in
    fun resetSteps() {

    }

    //saving to sharedPreferences
    private fun saveData(currentSteps: Int) {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("key1",currentSteps)
        editor.apply()
        Log.d(MAIN_DEBUG,"SavedSteps: $currentSteps")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    fun isNewDay(): Boolean {
        val sharedPref = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val lastTimeStarted = sharedPref.getInt("last_time_started",-1)
        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        return today != lastTimeStarted // jesli sie rozni, wskaze true
    }
}