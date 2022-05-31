package com.example.seniorbetterlife.senior.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.senior.maps.MapsActivity
import com.example.seniorbetterlife.databinding.FragmentHomeBinding
import com.example.seniorbetterlife.senior.helpPart.HelpActivity
import com.example.seniorbetterlife.senior.helpPart.model.UserTask
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var currentUser: User
    private lateinit var userTasks: List<UserTask>

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
            val userTask = listOfUserTasks.filter { it?.finished == false }.filter { it?.volunteer != null }[0]
            if (userTask != null)
                createDialog(userTask)
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
    }

    private fun onClickListeners() {
        binding.mapLinearLayout.setOnClickListener {
            val intent = Intent(this.context, MapsActivity::class.java)
            startActivity(intent)
        }
        binding.helpLinearLayout.setOnClickListener {
            val intent = Intent(this.context, HelpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}