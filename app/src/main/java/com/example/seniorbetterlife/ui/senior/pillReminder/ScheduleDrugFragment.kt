package com.example.seniorbetterlife.ui.senior.pillReminder

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.databinding.FragmentScheduleDrugBinding
import com.example.seniorbetterlife.ui.senior.pillReminder.model.Dose
import com.example.seniorbetterlife.ui.senior.pillReminder.model.DoseProperties
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class ScheduleDrugFragment : Fragment() {

    private var _binding: FragmentScheduleDrugBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PillReminderViewModel by activityViewModels()

    private var frequency by Delegates.notNull<Int>()
    private lateinit var dose: Dose
    val listOfDoseProperties: MutableList<DoseProperties> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScheduleDrugBinding.inflate(inflater, container, false)

        bindViews()
        setAdapter()
        setOnClickListeners()



        return binding.root
    }

    private fun setOnClickListeners() {
        binding.btnFinish.setOnClickListener {
            if(listOfDoseProperties.size == viewModel.frequency.value!!){
                val medicament = Medicament(viewModel.drugName.value!!,viewModel.dose.value!!,viewModel.frequency.value!!,listOfDoseProperties)
                viewModel.addMedicament(medicament)
                //add medicament to local Room database
                findNavController().navigate(R.id.action_scheduleDrugFragment_to_addDrugFragment)
            }else
                Toast.makeText(requireContext(),"Uzupełnij wszystkie dane",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter() {
        val recyclerView = binding.drugDoseRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = PillScheduleAdapter(frequency,dose)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : PillScheduleAdapter.onItemClickListener{
            @SuppressLint("SetTextI18n")
            override fun onChooseHourClick(position: Int, view: TextView) {
               popTimePicker(view)
            }

            override fun sendValues(
                position: Int,
                selectedSpinnerValue: String,
                selectedHour: String
            ) {
                addToListOfDoseProperties(position, selectedSpinnerValue, selectedHour)
            }

        })
    }

    private fun addToListOfDoseProperties(
        position: Int,
        selectedSpinnerValue: String,
        selectedHour: String
    ) {
        //safety if condition
        if (listOfDoseProperties.getOrNull(position) == null)
            listOfDoseProperties.add(
                position,
                DoseProperties(selectedSpinnerValue, selectedHour)
            )
        else {
            listOfDoseProperties.removeAt(position)
            listOfDoseProperties.add(
                position,
                DoseProperties(selectedSpinnerValue, selectedHour)
            )
        }
    }

    private fun popTimePicker(textView: TextView) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            textView.text = SimpleDateFormat("HH:mm").format(cal.time)
        }
        TimePickerDialog(requireContext(), timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }



    private fun bindViews() {
        //get da
        dose = viewModel.dose.value!!
        frequency = viewModel.frequency.value!!

        binding.tvDrugName.text = viewModel.drugName.value
        binding.tvWayOfDose.text = dose.description

        binding.tvFrequencyDescription.text  = when(frequency){
            1 -> "Raz dziennie"
            2 -> "Dwa razy dziennie"
            3 -> "Trzy razy dziennie"
            else -> "Error"
        }
    }

}