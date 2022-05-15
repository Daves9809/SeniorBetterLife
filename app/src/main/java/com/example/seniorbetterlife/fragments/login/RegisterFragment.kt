package com.example.seniorbetterlife.fragments.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.seniorbetterlife.fragments.BaseFragment
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.databinding.FragmentRegisterBinding
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.example.seniorbetterlife.profile.util.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : BaseFragment() {

    private val REG_DEBUG = "REG_DEBUG"
    private val viewModel = RegLogViewModel()

    private var _binding: FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var btnReg: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var repeatPassword: EditText
    private lateinit var ivSenior: LinearLayout
    private lateinit var ivJunior: LinearLayout

    private var isSenior: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        setupSignUpClick()

        binding.pbLogin.isVisible = false

        viewModel.userRegistrationStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    binding.pbLogin.isVisible = true
                }
                is Resource.Success -> {
                    binding.pbLogin.isVisible = false
                    Toast.makeText(this.context, "Register succesfully",Toast.LENGTH_SHORT).show()
                    startApp()
                }
                is Resource.Error -> {
                    binding.pbLogin.isVisible = false
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        }

    private fun bindViews() {
        //binding elements of view
        btnReg = binding.btnRegister
        email = binding.editRegEmail
        password = binding.editRegPassword
        repeatPassword = binding.editRegRepeat
        ivSenior = binding.IVsenior
        ivJunior = binding.IVjunior
        isSenior = isSeniorOrJunior()
        changeColor(isSenior)
    }


    private fun changeColor(isSen: Boolean) {
        if (isSen) {
            ivSenior.setBackgroundColor(resources.getColor(R.color.purple_500))
            ivJunior.setBackgroundColor(resources.getColor(R.color.white))
        } else {
            ivJunior.setBackgroundColor(resources.getColor(R.color.purple_500))
            ivSenior.setBackgroundColor(resources.getColor(R.color.white))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSignUpClick() {
        btnReg.setOnClickListener {
            val emaill = email.text.toString()
            val pass = password.text.toString()
            val repeatPass = repeatPassword.text.toString()
            viewModel.createUser(emaill,pass,repeatPass,isSenior)

        }
    }


    private fun isSeniorOrJunior(): Boolean {
        ivJunior.setOnClickListener {
            isSenior = false
            Log.d(REG_DEBUG, "User is junior")
            changeColor(isSenior)
        }
        ivSenior.setOnClickListener {
            isSenior = true
            Log.d(REG_DEBUG, "User is senior")
            changeColor(isSenior)
        }
        return isSenior

    }

}