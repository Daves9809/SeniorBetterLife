package com.example.seniorbetterlife.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.databinding.FragmentProfileBinding
import com.example.seniorbetterlife.util.Resource


class ProfileFragment : Fragment(){

    private val PROFILE_DEBUG = "PROFILE_DEBUG"

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var image: ImageView
    private lateinit var btnSave: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var plec: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        viewModel.getUserData()
        viewModel.updateSteps()
        observeViewModel()
        onClickListeners()
    }

    private fun bindViews() {
        image = binding.ivSeniorJunior
        btnSave = binding.btnSave
        radioGroup = binding.radioGroup
    }

    private fun onClickListeners() {
        binding.btnSave.setOnClickListener {
            saveData()
        }
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            if(checkedId == R.id.radBtnMen)
                plec = binding.radBtnMen.text.toString()
            else
                plec = binding.radBtnWomen.text.toString()
        })
    }

    private fun observeViewModel() {
        viewModel.isUserDataAvailable.observe(viewLifecycleOwner, Observer { user ->
            bindUserData(user!!)
            if(user.senior)
                image.setImageResource(R.drawable.ic_senior_128)
            else
                image.setImageResource(R.drawable.ic_young_128)
        })
        viewModel.userUpdateStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    //add progressBar = true
                }
                is Resource.Success -> {
                    //add progressBar = false
                    Toast.makeText(this.context, "Dane zostały zaktualizowane pomyślnie", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    //add progressBar = false
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun bindUserData(user: User) {
        binding.etName.setText(user.name)
        binding.etSurname.setText(user.surname)
        binding.etWiek.setText(user.age)
        binding.etNumber.setText(user.phoneNumber)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveData() {
        val name = binding.etName.text.toString()
        val surname = binding.etSurname.text.toString()
        val age = binding.etWiek.text.toString()
        val plec = plec
        val number = binding.etNumber.text.toString()
        viewModel.updateUser(
            User(
                name = name,
                surname = surname,
                age = age,
                sex = plec,
                phoneNumber = number
            )
        )
        Toast.makeText(requireContext(),"Dane zostały zapisane pomyślnie",Toast.LENGTH_LONG).show()
    }


}