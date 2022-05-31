package com.example.seniorbetterlife.senior.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide.with
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.databinding.FragmentProfileBinding
import com.example.seniorbetterlife.util.Resource
import com.google.firebase.storage.FirebaseStorage


class ProfileFragment : Fragment() {

    private val PROFILE_DEBUG = "PROFILE_DEBUG"

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    //test
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference

    // Create a reference with an initial file path and name
    val pathReference = storageRef.child("images")
    val reference = pathReference.child("lopata1.jpg")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val imageView = binding.ivSeniorJunior

        //test
        with(this /* context */)
            .load(reference)
            .into(imageView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserData()
        viewModel.updateSteps()
        observeViewModel()
        onClickListeners()
    }

    private fun onClickListeners() {
        binding.btnSave.setOnClickListener {
            requestPermissions(
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), 1
            )
        }
    }

    private fun observeViewModel() {
        viewModel.isUserDataAvailable.observe(viewLifecycleOwner, Observer { user ->
            bindUserData(user!!)
        })
        viewModel.steps.observe(viewLifecycleOwner) { steps ->
            binding.tvSteps.setText("Liczba kroków wykonana dzisiaj: $steps")
        }
        viewModel.userUpdateStatus.observe(viewLifecycleOwner, Observer {
            when (it) {
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
        Log.d(PROFILE_DEBUG, user.toString())
        binding.etName.setText(user.name)
        binding.etSurname.setText(user.surname)
        binding.etWiek.setText(user.age)
        binding.etPlec.setText(user.sex)
        binding.etNumber.setText(user.phoneNumber)
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
        viewModel.updateUser(
            User(
                name = name,
                surname = surname,
                age = age,
                sex = plec,
                phoneNumber = number
            )
        )
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}