package com.example.seniorbetterlife.helpPart

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.example.seniorbetterlife.databinding.FragmentListOfActiveTasksBinding
import com.example.seniorbetterlife.helpPart.model.UserAddress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng


class ListOfActiveTasksFragment : Fragment() {

    private var _binding: FragmentListOfActiveTasksBinding? = null
    private val binding get() = _binding!!

    lateinit var client: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListOfActiveTasksBinding.inflate(inflater,container,false)

        val fab = binding.fabAddTask
        fab.setOnClickListener {
            val dialog = AddTaskDialogFragment()

            dialog.show(childFragmentManager,"addTaskDialog")

        }

        //initialize location client
        client = LocationServices.getFusedLocationProviderClient(activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getLastKnownLocation()

    }

    private fun getLastKnownLocation() {
        checkLocationPermission()
        client.lastLocation.addOnCompleteListener {
            val latitude: Double? = it.result?.latitude
            val longitude: Double? = it.result?.longitude
            val pos = LatLng(latitude!!, longitude!!)
            val geoCoder = Geocoder(requireContext())
            val matches = geoCoder.getFromLocation(latitude, longitude, 1)
            val userAddress: UserAddress = getUserAddress(matches)
            val formattedUserAddress = formatUserAddress(userAddress)
            Toast.makeText(context,"CurrentLocation: $formattedUserAddress", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatUserAddress(address: UserAddress): String {
        return("${address.postalAddres}, ${address.locality}, ${address.street}")
    }

    private fun getUserAddress(geoLocation: List<Address>): UserAddress {
        val postalCode = geoLocation[0].postalCode
        val locality = geoLocation[0].locality
        val street = geoLocation[0].thoroughfare
        return(UserAddress(postalCode,locality,street))
    }


    private fun checkLocationPermission(): Boolean {
        var state = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                state = true
            } else {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1
                )
            }

        } else state = true
        return state
    }

    companion object {
        const val DEFAULT_ZOOM = 14.0F
    }

}