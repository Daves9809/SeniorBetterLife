package com.example.seniorbetterlife.ui.senior.pillReminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seniorbetterlife.MainActivity
import com.example.seniorbetterlife.MyApplication
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.databinding.FragmentAddDrugBinding
import com.example.seniorbetterlife.util.Constants


class AddDrugFragment : Fragment() {

    private var _binding: FragmentAddDrugBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PillReminderViewModel by activityViewModels()
    private val listOfMedicamentsTest: MutableList<Medicament> = mutableListOf()
    private lateinit var adapter: AddDrugAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddDrugBinding.inflate(inflater, container, false)


        setAdapter()
        observeData()
        onClickListeners()
        backButtonPressed()
        createNotificationChannel()

        val notification = NotificationCompat.Builder(requireContext(),Constants.CHANNEL_PILL_REMIND_ID)
            .setContentTitle("Awesome notification")
            .setContentText("This is the content text")
            .setSmallIcon(R.drawable.medicine)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = NotificationManagerCompat.from(requireContext())

        return binding.root
    }

    private fun backButtonPressed() {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)

                }
            }
            )
    }

    private fun onClickListeners() {
        binding.fabAddTaskDrug.setOnClickListener {
            findNavController().navigate(R.id.action_addDrugFragment_to_drugNameAndDoseFragment)
        }
    }

    private fun setAdapter() {
        val recyclerView = binding.recyclerViewDrug
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AddDrugAdapter()
        recyclerView.adapter = adapter

    }

    private fun observeData() {
        viewModel.medicaments.observe(viewLifecycleOwner, Observer { medicaments ->
            adapter.setData(medicaments)
            setAdapterClickListeners(medicaments)
            saveDataToFirebase(medicaments)

        })
    }

    private fun saveDataToFirebase(medicaments: List<Medicament>) {
        viewModel.saveMedicamentsToFirebase(medicaments)
    }

    fun setAdapterClickListeners(medicaments: List<Medicament>){
        adapter.setOnItemClickListener(object : AddDrugAdapter.onItemClickListener{
            override fun onDeleteClickListener(position: Int) {
                viewModel.deleteMedicament(medicaments[position])
            }
        })
    }

    fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(Constants.CHANNEL_PILL_REMIND_ID,Constants.CHANNEL_PILL_REMIND_NAME,
                NotificationManager.IMPORTANCE_HIGH).apply {
                    lightColor = Color.GREEN // blink with led
                enableLights(true)
            }
            val notificationManager = MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}