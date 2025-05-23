package com.example.supporthealth.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import com.example.supporthealth.R
import com.example.supporthealth.main.domain.api.StepDao
import com.example.supporthealth.main.domain.models.StepEntity
import com.example.supporthealth.profile.details.domain.api.interactor.UserDetailsInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StepCounterService: Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var stepsAtReset: Int = 0
    private val PRESSURE_PER_FLOOR = 0.12f

    private var basePressure: Float? = null
    private var floors: Int = 0

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val stepDao: StepDao by inject()
    private val detailsInteractor: UserDetailsInteractor by inject()

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        ensureTodayRecordExists()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(
                1,
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH
            )
        } else {
            startForeground(1, createNotification())
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val prefs = getSharedPreferences("steps", Context.MODE_PRIVATE)
            val current = event.values[0].toInt()

            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val savedDate = prefs.getString("step_counter_date", null)
            var initial = prefs.getInt("step_counter_initial", -1)

            if (savedDate == null || savedDate != today || initial == -1) {
                prefs.edit()
                    .putInt("step_counter_initial", current)
                    .putString("step_counter_date", today)
                    .apply()
                initial = current
            }

            stepsAtReset = initial
            val steps = current - stepsAtReset
            saveStepsToDb(steps.coerceAtLeast(0))
        }

        if (event?.sensor?.type == Sensor.TYPE_PRESSURE) {
            val currentPressure = event.values[0]
            if (basePressure == null) basePressure = currentPressure
            val diff = basePressure!! - currentPressure
            val approxFloors = (diff / PRESSURE_PER_FLOOR).toInt()
            floors += approxFloors
        }
    }

    private fun saveStepsToDb(steps: Int) {
        serviceScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val todayEntity = stepDao.getStepsByDate(today)

            val target = detailsInteractor.getUserDetails()!!.targetActivity
            val calories = calcCalories(steps)
            val distant = calcDistance(steps)
            val time = calcTimeMinutesInt(steps)
            val floors = floors

            if (todayEntity != null) {
                val updated = todayEntity.copy(
                    steps = steps,
                    calories = calories,
                    distant = distant,
                    time = time,
                    floors = floors
                )
                stepDao.update(updated)
            } else {
                val newEntity = StepEntity(
                    date = today,
                    steps = steps,
                    target = target,
                    calories = calories,
                    distant = distant,
                    time = time,
                    floors = floors
                )
                stepDao.insert(newEntity)
            }
        }
    }

    private fun ensureTodayRecordExists() {
        serviceScope.launch {
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val todayEntity = stepDao.getStepsByDate(today)
            if (todayEntity == null) {
                val target = detailsInteractor.getUserDetails()!!.targetActivity
                val newEntity = StepEntity(
                    date = today,
                    steps = 0,
                    target = target,
                    calories = 0,
                    distant = 0f,
                    time = 0,
                    floors = 0
                )
                stepDao.insert(newEntity)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun createNotification(): Notification {
        val channelId = "step_counter_channel"
        val channel = NotificationChannel(
            channelId,
            "Step Counter Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        return Notification.Builder(this, channelId)
            .setContentTitle("Шагомер")
            .setContentText("Счётчик шагов работает")
            .setSmallIcon(R.drawable.ic_activity)
            .build()
    }

    private fun calcCalories(steps: Int, weightKg: Int = detailsInteractor.getUserDetails()!!.weight): Int {
        val caloriesPerStep = 0.00057f * weightKg
        return (steps * caloriesPerStep).toInt()
    }

    private fun calcDistance(steps: Int, stepLengthMeters: Float = 0.75f): Float {
        return steps * stepLengthMeters
    }

    private fun calcTimeMinutesInt(steps: Int, stepsPerMinute: Int = 100): Int {
        return steps / stepsPerMinute
    }
}