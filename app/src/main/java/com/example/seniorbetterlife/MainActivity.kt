package com.example.seniorbetterlife

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.databinding.ActivityMainBinding
import com.example.seniorbetterlife.login.LoginActivity
import com.example.seniorbetterlife.services.PedometerService
import com.example.seniorbetterlife.util.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val MAIN_DEBUG = "LOG_DEBUG"

    private var sensorManager: SensorManager? = null

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    private lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        viewModel.loadUser()

        //observeData
        viewModel.user.observe(this, androidx.lifecycle.Observer {
            user = it!!
            setDataToRoom(it)
            configureNavigation(it)
        })

        //sensor configure
        sensorConfigure()

        //getPermissions
        getPermissions(this)

    }

    private fun setDataToRoom(user: User) {
        if(user.senior){
            val listOfDailySteps: List<DailySteps?> = user.dailySteps
            viewModel.setRoomDailySteps(listOfDailySteps)
        }
    }

    fun isServiceRunningInForeground(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (PedometerService::class.simpleName.equals(service.service.className)) {
                return true
            }
        }
        return false
    }

    @SuppressLint("CheckResult")
    private fun getPermissions(context: Context) {
        RxPermissions(this).request(android.Manifest.permission.ACTIVITY_RECOGNITION)
            .subscribe{ isGranted ->
                Log.d("TAG", "Is ACTIVITY_RECOGNITION permission granted: $isGranted")
            }
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

    private fun configureNavigation(user: User) {
        val isSenior= user.senior
        val navMenu = binding.bottomNavigationView // przypisanie menuNawigacji(dolny panel)
        val navController = findNavController(R.id.navFragment) // przypisanie navControllera
        //setting navController which depends on senior/volunteer
        if(isSenior) {
            navController.setGraph(R.navigation.senior_nav_graph)
            //start service if it's not running
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(!isServiceRunningInForeground()){
                    val startIntent = Intent(this@MainActivity, PedometerService::class.java)
                    startIntent.action = Constants.ACTION_START_FOREGROUND_ACTION
                    startService(startIntent)
                }
            }
        }
        else
            navController.setGraph(R.navigation.volunteer_bottom_menu_nav_graph)
        onBottomMenuClickItemListener(navMenu, navController, isSenior)
        setupActionBarWithNavController(navController) // przypisuje nazwy "labeli" do górnego paska
    }

    private fun onBottomMenuClickItemListener(navMenu: BottomNavigationView,navController: NavController, isSenior: Boolean) {
        if(isSenior)
            navMenu.setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.profileFragment -> {
                        navController.navigate(R.id.action_homeFragment_to_profileFragment)
                        true
                    }
                    R.id.homeFragment -> {
                        navController.navigate(R.id.action_profileFragment_to_homeFragment)
                        true
                    }
                    else -> false
                }
            }

        else
            navMenu.setOnItemSelectedListener { item ->
                when(item.itemId){
                    R.id.profileFragment -> {
                        navController.navigate(R.id.action_volunteerMapsFragment_to_profileFragment2)
                        true
                    }
                    R.id.homeFragment -> {
                        navController.navigate(R.id.action_profileFragment2_to_volunteerMapsFragment)
                        true
                    }
                    else -> false
                }
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

    private fun signOut() {
        signOutUser()
    }

    private fun signOutUser() {
        val intent = Intent(this.applicationContext, LoginActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK) // flag activity zabezpieczaja przed powrotem
        }
        //czyszczenie podręcznej bazy przy logowaniu

        //data save to Firebase
        viewModel.getDailySteps()
        viewModel.dailySteps.observe(this, androidx.lifecycle.Observer {
            viewModel.saveDailySteps(user,it)
            //clear all local data
            viewModel.clearLocalDatabase()
        })

        auth.signOut()
        //stop of service
        val stopService = Intent(this@MainActivity, PedometerService::class.java)
        stopService.action = Constants.ACTION_STOP_FOREGROUND_ACTION
        startService(stopService)

        startActivity(intent)
        finish()
    }

}