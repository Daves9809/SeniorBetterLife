package com.example.seniorbetterlife.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.databinding.FragmentProfileBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private val PROFILE_DEBUG = "PROFILE_DEBUG"

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel = ProfileViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            bindUserData(user)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            profileViewModel.updateSteps()
        }
        profileViewModel.steps.observe(viewLifecycleOwner) {steps ->
            binding.tvSteps.setText("Liczba kroków wykonana dzisiaj: $steps")
        }
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
        profileViewModel.updateUser(User(name = name, surname = surname, age = age, sex = plec, phoneNumber = number))
    }
}