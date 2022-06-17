package com.example.seniorbetterlife.ui.loginRegister

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.seniorbetterlife.MyApplication
import com.example.seniorbetterlife.broadcastReceivers.AlarmReceiver
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.model.Medicament
import com.example.seniorbetterlife.data.model.User
import com.example.seniorbetterlife.ui.senior.home.BaseFragment
import com.example.seniorbetterlife.databinding.FragmentLoginBinding
import com.example.seniorbetterlife.utils.Constants
import com.example.seniorbetterlife.utils.DateFormatter
import com.example.seniorbetterlife.utils.IntentBuildHelper
import com.example.seniorbetterlife.utils.Resource
import java.util.*

class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val LOG_DEBUG = "LOG_DEBUG"
    //inicjacja referencji do elementow z xmla, aby nastepnie je zbindowac
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var alarmManager: AlarmManager
    private val viewModel : RegLogViewModel by activityViewModels()

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
        observeData()
        setupLoginClick()
        setupRegistrationClick()

    }

    private fun setDataToRoom(user: User) {
        if(user.senior){
            val listOfDailySteps: List<DailySteps?> = user.dailySteps
            viewModel.setRoomDailySteps(listOfDailySteps)
            viewModel.setRoomMedicaments(user.medicaments)
        }
    }

    override fun observeData() {
        binding.pbLogin.isVisible = false

        viewModel.userSignUpStatus.observe(viewLifecycleOwner, Observer {
        when(it){
            is Resource.Loading -> {
                binding.pbLogin.isVisible = true
            }
            is Resource.Success -> {
                binding.pbLogin.isVisible = false
                Toast.makeText(this.context, "Login succesfully", Toast.LENGTH_SHORT).show()
                loadUserData()
                initializeNotifications()
                startApp()
            }
            is Resource.Error -> {
                binding.pbLogin.isVisible = false
                Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    })
    }

    private fun loadUserData() {
        viewModel.loadUser()
        viewModel.user.observe(viewLifecycleOwner, Observer {
            setDataToRoom(it!!)
        })
    }

    private fun initializeNotifications() {
        createNotificationChannel()
        setUpNotifications()
    }

    private fun setUpNotifications() {
        var helpVar = 0
        viewModel.medicaments.observe(viewLifecycleOwner, Observer { medicaments ->
            if(helpVar ==0){
                createNotifications(medicaments)
                Log.d("SETUPNOTIFICATIONS","TEST")
            }
            helpVar++
        })
    }

    private fun createNotifications(medicaments: List<Medicament>) {
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        for (medicament in medicaments){
            for (dailyDose in medicament.dailyDoses) {
                val pendingIntent = IntentBuildHelper.createPendingIntent(medicament.notificationID,medicament.medicine,applicationContext = MyApplication.getContext())

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    DateFormatter.getTimeInMillis(dailyDose.hourOfTake),
                    AlarmManager.INTERVAL_DAY, pendingIntent
                )
                Log.d(
                    "CreateNotification",
                    "HourOfTakes = ${DateFormatter.getTimeInMillis(dailyDose.hourOfTake)}"
                )
            }
        }
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
    fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_PILL_REMIND_ID, Constants.CHANNEL_PILL_REMIND_NAME,
                NotificationManager.IMPORTANCE_HIGH).apply {
                lightColor = Color.GREEN // blink with led
                enableLights(true)
            }
            val notificationManager = MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}