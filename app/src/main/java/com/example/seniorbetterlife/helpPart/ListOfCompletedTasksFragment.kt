package com.example.seniorbetterlife.helpPart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.databinding.FragmentHomeBinding
import com.example.seniorbetterlife.databinding.FragmentListOfCompletedTasksBinding


class ListOfCompletedTasksFragment : Fragment() {

    private var _binding: FragmentListOfCompletedTasksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListOfCompletedTasksBinding.inflate(inflater,container,false)

        return binding.root
    }

}