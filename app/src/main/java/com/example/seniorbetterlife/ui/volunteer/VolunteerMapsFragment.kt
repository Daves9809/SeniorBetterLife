package com.example.seniorbetterlife.ui.volunteer

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.seniorbetterlife.MainViewModel
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.ui.senior.helpPart.model.UserTask

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VolunteerMapsFragment : Fragment(), GoogleMap.OnInfoWindowClickListener {

    private val viewModel: MainViewModel by activityViewModels()
    private val listOfUserTasks = mutableListOf<UserTask>()

    private lateinit var currentUser: User

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        viewModel.usersTasks.observe(viewLifecycleOwner, Observer { userTasks ->
            for (userTask in userTasks) {
                if (!userTask.finished)
                    listOfUserTasks.add(userTask)
                addMarker(googleMap, listOfUserTasks)
            }
        })
        googleMap.setOnInfoWindowClickListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_volunteer_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        viewModel.getAllUserEmails()
        observeData()
    }


    private fun observeData() {
        viewModel.userEmails.observe(viewLifecycleOwner, Observer { emails ->
            for (email in emails) {
                viewModel.getAllUsersTasks(email)
            }
        })
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null) {
                currentUser = user
            }
        })

    }

    fun addMarker(googleMap: GoogleMap, userTasks: List<UserTask>) {
        for (userTask in userTasks) {
            val location = LatLng(userTask.userAddress.lattitude, userTask.userAddress.longtitude)
            googleMap.addMarker(
                MarkerOptions().position(location).title("Zadanie").snippet(userTask.description)
            )
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        //create dialog
        val userTask = listOfUserTasks.filter { it.description == p0.snippet && it.userAddress.lattitude == p0.position.latitude }[0]
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Pomoc dla ${userTask.user.name + " " + userTask.user.surname}")
            .setMessage(
                "Adres: " + userTask.userAddress.address + "\n" +
                        "Opis: " + userTask.description + "\n" +
                        "Wiek: ${userTask.user.age}  \n" +
                        "Numer Telefonu: ${userTask.user.phoneNumber}"
            )
            .setNegativeButton("anuluuj") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("akceptuj") { dialog, which ->
                //set volunteer into userTask as people who declared help
                viewModel.updateUserTask(userTask,currentUser)
                dialog.dismiss()
            }
            .show()
    }
}