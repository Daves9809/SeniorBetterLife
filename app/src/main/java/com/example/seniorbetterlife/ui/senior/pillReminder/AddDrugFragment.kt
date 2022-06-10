package com.example.seniorbetterlife.ui.senior.pillReminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.databinding.FragmentAddDrugBinding
import com.example.seniorbetterlife.databinding.FragmentScheduleDrugBinding

class AddDrugFragment : Fragment() {

    private var _binding: FragmentAddDrugBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PillReminderViewModel by activityViewModels()
    private val listOfMedicamentsTest: MutableList<Medicament> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddDrugBinding.inflate(inflater, container, false)


        observeData()
        setAdapter()
        onClickListeners()

        return binding.root
    }

    private fun onClickListeners() {
        binding.fabAddTaskDrug.setOnClickListener {
            findNavController().navigate(R.id.action_addDrugFragment_to_drugNameAndDoseFragment)
        }
    }

    private fun setAdapter() {
        val recyclerView = binding.recyclerViewDrug
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AddDrugAdapter(listOfMedicamentsTest)
        recyclerView.adapter = adapter
    }

    private fun observeData() {
        viewModel.medicament.observe(viewLifecycleOwner, Observer {
            listOfMedicamentsTest.add(it)
        })
    }

}