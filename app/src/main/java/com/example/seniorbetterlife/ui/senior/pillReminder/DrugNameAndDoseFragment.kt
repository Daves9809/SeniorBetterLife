package com.example.seniorbetterlife.ui.senior.pillReminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.databinding.FragmentDrugNameAndDoseBinding
import com.example.seniorbetterlife.ui.senior.pillReminder.model.Dose

class DrugNameAndDoseFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentDrugNameAndDoseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PillReminderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrugNameAndDoseBinding.inflate(inflater, container, false)

        initializeSpinner()
        onClickListeners()

        return binding.root
    }

    private fun onClickListeners() {
        binding.btnNext.setOnClickListener {
            if (
                binding.etDrugName.text.isNotEmpty()) {
                viewModel.setName(binding.etDrugName.text.toString())
                findNavController().navigate(R.id.action_drugNameAndDoseFragment_to_frequencyDoseFragment)
            } else
                Toast.makeText(requireContext(), "Pola nie mogą byc puste", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun initializeSpinner() {
        val spinner: Spinner = binding.spinner
        spinner.onItemSelectedListener = this
        val categories = ArrayList<String>()
        categories.addAll(Dose.values().map { it.description })
        val dataAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, categories)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = dataAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        val dose = parent?.getItemAtPosition(pos).toString()
        viewModel.chooseDose(dose)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //Nothing selected
    }

}