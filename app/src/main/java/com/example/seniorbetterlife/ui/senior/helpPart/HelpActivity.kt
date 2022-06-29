package com.example.seniorbetterlife.ui.senior.helpPart

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.databinding.ActivityHelpBinding
import com.example.seniorbetterlife.ui.senior.helpPart.adapters.ViewPagerAdapter
import com.example.seniorbetterlife.ui.senior.helpPart.model.UserAddress
import com.example.seniorbetterlife.ui.senior.helpPart.model.UserTask
import com.example.seniorbetterlife.utils.DateFormatter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class HelpActivity : AppCompatActivity(), AddTaskDialogFragment.DialogListener {

    private lateinit var binding: ActivityHelpBinding
    private val viewModel: SharedViewModel by viewModels()

    lateinit var client: FusedLocationProviderClient
    private val TAG = this.javaClass.name

    private lateinit var user: User
    private lateinit var userAddressHelper: UserAddress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTabs()
        initializingVariables()

        //initialize location client
        client = LocationServices.getFusedLocationProviderClient(this)

        viewModel.getUserData()
        observeData()
        checkPermissions()

    }

    private fun initializingVariables() {
        userAddressHelper = UserAddress()
    }


    private fun observeData() {
        viewModel.isUserDataAvailable.observe(this, androidx.lifecycle.Observer { data ->
            user = data!!
        })
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(ListOfActiveTasksFragment(), "Aktywne")
        adapter.addFragment(ListOfCompletedTasksFragment(), "Ukończone")
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

    }

    override fun onDialogPositiveClick(kindOfHelp: String) {
        getLastKnownLocation(kindOfHelp)
        Toast.makeText(this, "Pomyślnie dodano nowe zadanie", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        Toast.makeText(this, "Zrezygnowano z dodania nowego zadania", Toast.LENGTH_SHORT).show()
    }

    private fun createUserTask(
        userAddress: UserAddress,
        user: User,
        date: String,
        description: String,
        isFinished: Boolean = false
    ): UserTask =
        UserTask(userAddress, user, date, description, isFinished, volunteer = null)


    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(kindOfHelp: String) {
        var userAddress: UserAddress? = null
        client.lastLocation.addOnCompleteListener {
            val latitude: Double? = it.result?.latitude
            val longitude: Double? = it.result?.longitude
            val geoCoder = Geocoder(this)
            Log.d("geocoder",geoCoder.toString())
            Log.d("geocoder","$latitude,$longitude")
            val matches = geoCoder.getFromLocation(latitude!!, longitude!!, 1)
            userAddress = userAddressHelper.getUserAddressFromGeoLocation(matches)
        }.addOnSuccessListener {
            val userTask = createUserTask(
                userAddress!!,
                user,
                DateFormatter.getDateWithTime(),
                description = kindOfHelp,
                isFinished = false
            )
            viewModel.addUserTask(userTask, DateFormatter.getDateWithTime())
        }
    }

    private fun checkPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Toast.makeText(this, "Precise location access granted.", Toast.LENGTH_SHORT)
                        .show()
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Toast.makeText(
                        this,
                        "Only approximate location access granted.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(this, "No location access granted.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //request permission for precise location
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

}