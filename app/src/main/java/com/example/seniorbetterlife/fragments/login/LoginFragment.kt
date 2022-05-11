package com.example.seniorbetterlife.fragments.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.seniorbetterlife.fragments.BaseFragment
import com.example.seniorbetterlife.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragment() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentLoginBinding
    private val LOG_DEBUG = "LOG_DEBUG"
    //inicjacja referencji do elementow z xmla, aby nastepnie je zbindowac
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText

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
        //bindowanie przyciskow do zmiennych
        tvRegister = binding.tvRegister
        btnLogin = binding.btnLogin
        editEmail = binding.editLoginEmail
        editPassword = binding.editPassword

        setupLoginClick()
        setupRegistrationClick()
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
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener { authres ->
                if(authres.user != null) startApp()
            }.addOnFailureListener { exc ->
                Snackbar.make(requireView(),"Something is no yes",Snackbar.LENGTH_SHORT).show()
                Log.d(LOG_DEBUG, exc.message.toString())
            }
        }

    }

}