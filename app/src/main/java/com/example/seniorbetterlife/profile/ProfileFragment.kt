package com.example.seniorbetterlife.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.databinding.FragmentProfileBinding
import com.example.seniorbetterlife.profile.util.Resource
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private val PROFILE_DEBUG = "PROFILE_DEBUG"

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel = ProfileViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserData()
        viewModel.isUserDataAvailable.observe(viewLifecycleOwner, Observer { user ->
            bindUserData(user!!)
        })

        viewModel.updateSteps()
        viewModel.steps.observe(viewLifecycleOwner) { steps ->
            binding.tvSteps.setText("Liczba kroków wykonana dzisiaj: $steps")
        }


        viewModel.userUpdateStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    //add progressBar = true
                }
                is Resource.Success -> {
                    //add progressBar = false
                    Toast.makeText(this.context, "Update succesfully", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    //add progressBar = false
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun bindUserData(user: User) {
        Log.d(PROFILE_DEBUG,user.toString())
        binding.etName.setText(user.name)
        binding.etSurname.setText(user.surname)
        binding.etWiek.setText(user.age)
        binding.etPlec.setText(user.sex)
        binding.etNumber.setText(user.phoneNumber)
        binding.tvOcena.setText(user.rating.toString())
    }


    override fun onDestroyView() {
        saveData()
        super.onDestroyView()
        _binding = null
    }

    private fun saveData() {
            val name = binding.etName.text.toString()
            val surname = binding.etSurname.text.toString()
            val age = binding.etWiek.text.toString()
            val plec = binding.etPlec.text.toString()
            val number = binding.etNumber.text.toString()
            viewModel.updateUser(User(name = name, surname = surname, age = age, sex = plec, phoneNumber = number))
    }
}