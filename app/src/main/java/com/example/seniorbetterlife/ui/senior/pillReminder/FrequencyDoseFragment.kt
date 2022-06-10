package com.example.seniorbetterlife.ui.senior.pillReminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.databinding.FragmentFrequencyDoseBinding


class FrequencyDoseFragment : Fragment() {

    private var _binding: FragmentFrequencyDoseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PillReminderViewModel by activityViewModels()

    private lateinit var radioGroup: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentFrequencyDoseBinding.inflate(inflater, container, false)

        bindViews()
        onClickListeners()
        return binding.root
    }

    private fun bindViews() {
        binding.btnNext.isEnabled = false
        radioGroup = binding.radioGroup
    }

    private fun onClickListeners() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_frequencyDoseFragment_to_scheduleDrugFragment)
        }
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener{ group, checkedId ->
            binding.btnNext.isEnabled = true
            viewModel.setFrequency(
                when(checkedId){
                    R.id.radio_button_1 -> 1
                    R.id.radio_button_2 -> 2
                    R.id.radio_button_3 -> 3
                    else -> 1 // setting to default
                }
            )
        })
    }

}