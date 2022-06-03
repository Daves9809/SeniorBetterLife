package com.example.seniorbetterlife.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.seniorbetterlife.R
import com.example.seniorbetterlife.data.access.MyDataDao
import com.example.seniorbetterlife.data.access.MyDatabase
import com.example.seniorbetterlife.data.model.DailySteps
import com.example.seniorbetterlife.data.repositories.RoomRepository
import com.example.seniorbetterlife.util.Constants
import com.example.seniorbetterlife.util.DateFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class PedometerService : Service(), SensorEventListener {

    private var dao: MyDataDao? = null
    private var roomRepository: RoomRepository? = null

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private var isUserRelogged: Boolean = false
    private var oldSteps: Int = 0
    private var stepsAfterLogin by Delegates.notNull<Int>()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent!!.action.equals(Constants.ACTION_START_FOREGROUND_ACTION)) {
            dao = MyDatabase.getDatabase(this).myDataDao()
            roomRepository = RoomRepository(dao!!)
            isUserRelogged = true
            startForegroundService()
            registerListener(true)
        } else if (intent.action.equals(Constants.ACTION_STOP_FOREGROUND_ACTION)) {
            stopForeground(true)
            isUserRelogged = false
            stopSelfResult(startId)
            registerListener(false)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun registerListener(turnOn: Boolean) {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (turnOn)
            stepCounterSensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
            }
        else
            stepCounterSensor?.let {
                sensorManager.unregisterListener(this)
            }
    }

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        sensorEvent ?: return
        // Data 1: According to official documentation, the first value of the `SensorEvent` value is the step count
        sensorEvent.values.firstOrNull()?.let { stepsSinceReboot ->
            val currentDate = DateFormatter.getDate()
            var currentSteps = 0
            if (isUserRelogged) {
                scope.launch {
                    stepsAfterLogin = stepsSinceReboot.toInt()
                    val listOfDailySteps = roomRepository!!.getDailySteps()
                    if (listOfDailySteps.isNotEmpty()) {
                        val dailySteps = listOfDailySteps.filter { it!!.day == currentDate }
                        if(dailySteps.isNotEmpty()){
                            val currentDailySteps = dailySteps[0]
                                Log.d(
                                    "PedometerService",
                                    "DailySteps from currentDay =  ${currentDailySteps!!.steps}"
                                )
                                oldSteps = currentDailySteps.steps

                        }else {
                            initializeNewRow(currentDate)
                        }
                    } else {
                        initializeNewRow(currentDate)
                    }
                }
                isUserRelogged = false
            } else {
                currentSteps = oldSteps + (stepsSinceReboot.toInt() - stepsAfterLogin)
                Log.d("PedometerService", "User was loggedIn")
                scope.launch {
                    roomRepository!!.updateSteps(DailySteps(currentDate , currentSteps))
                }
                notificationBuilder.setContentText("Today's count of steps: PedometerService").build()
                startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    private suspend fun initializeNewRow(currentDate: String) {
        Log.d("PedometerService", "New day initializing")
        oldSteps = 0
        roomRepository!!.addDailySteps(DailySteps(currentDate, 0))
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("TAG", "onAccuracyChanged: Sensor: $sensor; accuracy: $accuracy")
    }

    private fun startForegroundService() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        notificationBuilder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_directions_walk_24)
            .setContentTitle("SeniorBetterLife")
            .setContentText("Service is running")
        startForeground(Constants.NOTIFICATION_ID, notificationBuilder.build())

    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


}