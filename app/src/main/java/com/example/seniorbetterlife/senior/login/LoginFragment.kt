package com.example.seniorbetterlife.senior.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.seniorbetterlife.senior.main.BaseFragment
import com.example.seniorbetterlife.databinding.FragmentLoginBinding
import com.example.seniorbetterlife.util.Resource

class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val LOG_DEBUG = "LOG_DEBUG"
    //inicjacja referencji do elementow z xmla, aby nastepnie je zbindowac
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private val viewModel = RegLogViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()

        setupLoginClick()
        setupRegistrationClick()
        binding.pbLogin.isVisible = false

        viewModel.userSignUpStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    binding.pbLogin.isVisible = true
                }
                is Resource.Success -> {
                    binding.pbLogin.isVisible = false
                    Toast.makeText(this.context, "Login succesfully", Toast.LENGTH_SHORT).show()
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
        //bindowanie przyciskow do zmiennych
        tvRegister = binding.tvRegister
        btnLogin = binding.btnLogin
        editEmail = binding.editLoginEmail
        editPassword = binding.editPassword
    }

    private fun setupRegistrationClick() {
        //przenies do registration fragment
        tvRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegister().actionId)
        }
    }

    private fun setupLoginClick() {
        //dokonaj logowania
        btnLogin.setOnClickListener {
            val email = editEmail.text.trim().toString()
            val password = editPassword.text.trim().toString()
            viewModel.loginUser(email,password)
        }

    }

}