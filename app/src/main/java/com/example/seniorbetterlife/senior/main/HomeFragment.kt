package com.example.seniorbetterlife.senior.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.senior.maps.MapsActivity
import com.example.seniorbetterlife.databinding.FragmentHomeBinding
import com.example.seniorbetterlife.senior.helpPart.HelpActivity
import com.example.seniorbetterlife.senior.helpPart.model.UserTask
import com.example.seniorbetterlife.util.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        onClickListeners()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadUser()
        observeData()
    }

    private fun observeData() {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            if (user != null)
                user.email?.let { viewModel.getUserTasks(it) }
        })
        viewModel.userTasks.observe(viewLifecycleOwner, Observer { listOfUserTasks ->
            if(listOfUserTasks.isNotEmpty() ){
                val userTasks = listOfUserTasks.filter { it?.finished == false }.filter { it!!.volunteer != null }
                if (userTasks.isNotEmpty()){
                    val userTask = userTasks[0]
                    if(userTask != null)
                        createDialog(userTask)
                }
            }
        })
    }

    private fun createDialog(userTask: UserTask) {
        val volunteer = userTask.volunteer
        if (volunteer != null)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Pomoc oferuje ${volunteer.name + " " + (volunteer.surname)}")
                .setMessage(
                            "Opis zadania: " + userTask.description + "\n" +
                            "Wiek: ${volunteer.age}  \n" +
                            "Numer Telefonu: ${volunteer.phoneNumber}"
                )
                .setNegativeButton("Odrzuć") { dialog, which ->
                    // set userTask's volunteer as null
                    viewModel.setUserTaskVolunteerToNull(userTask)
                    dialog.dismiss()
                }
                .setPositiveButton("Akceptuj") { dialog, which ->
                    // set userTask as finished
                    viewModel.setUserTaskToCompleted(userTask)
                    dialog.dismiss()
                }
                .show()
        Log.d("VOLUNTEER","$volunteer")
    }

    private fun onClickListeners() {
        binding.mapLinearLayout.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMapsActivity().actionId)
        }
        binding.helpLinearLayout.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHelpActivity().actionId)
        }
        binding.PedometerLinearLayout.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPedometerActivity().actionId)
        }
        binding.PillReminderLinearLayout.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToPillReminder().actionId)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}