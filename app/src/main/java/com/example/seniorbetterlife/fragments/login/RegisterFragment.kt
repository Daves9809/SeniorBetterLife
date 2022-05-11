package com.example.seniorbetterlife.fragments.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.seniorbetterlife.fragments.BaseFragment
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.User
import com.example.seniorbetterlife.databinding.FragmentRegisterBinding
import com.example.seniorbetterlife.data.repositories.FirebaseRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : BaseFragment() {

    private val auth = FirebaseAuth.getInstance()
    private val fbRepository = FirebaseRepository()
    private val REG_DEBUG = "REG_DEBUG"

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

        //binding elements of view
        btnReg = binding.btnRegister
        email = binding.editRegEmail
        password = binding.editRegPassword
        repeatPassword = binding.editRegRepeat
        ivSenior = binding.IVsenior
        ivJunior = binding.IVjunior

        isSenior = isSeniorOrJunior()
        changeColor(isSenior)
        setupSignUpClick()
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

            if (checkInput(emaill, pass, repeatPass))
                auth.createUserWithEmailAndPassword(emaill, pass)
                    .addOnSuccessListener { authRes -> // adding user to Authentication Firebase
                        if (authRes.user != null) {
                            fbRepository.addUser(
                                User(
                                    auth.currentUser?.uid,
                                    emaill,
                                    isSenior = isSenior
                                )
                            ) // adding user to firebaseStore
                            startApp() // starting mainActivity
                        }
                    }.addOnFailureListener { exc ->
                    Snackbar.make(
                        requireView(),
                        "Ups.. Coś nie gra, $emaill, $pass",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    Log.d(REG_DEBUG, exc.message.toString())
                }

        }
    }

    private fun checkInput(email: String, password: String, repeatedPassword: String): Boolean {
        if (password == repeatedPassword && password.toCharArray().count() >= 5 && email.contains(
                '@'
            )
        )
            return true
        else if (password != repeatedPassword) {
            Log.d(REG_DEBUG, "Hasło musi być takie samo")
            Snackbar.make(
                requireView(),
                "Hasło musi być takie samo",
                Snackbar.LENGTH_SHORT
            ).show()
        } else if (password.toCharArray().count() <= 5) {
            Log.d(REG_DEBUG, "Hasło musi składać się z 6 znaków")
            Snackbar.make(
                requireView(),
                "Hasło musi składać się z 6 znaków",
                Snackbar.LENGTH_SHORT
            ).show()
        } else if (!email.contains('@')){
            Log.d(REG_DEBUG, "Niepoprawny e-mail")
            Snackbar.make(
                requireView(),
                "Niepoprawny e-mail",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        return false
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