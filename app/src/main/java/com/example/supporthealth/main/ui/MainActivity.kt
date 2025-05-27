package com.example.supporthealth.main.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.supporthealth.R
import com.example.supporthealth.activity.main.ui.ActivityFragment
import com.example.supporthealth.app.StepCounterService
import com.example.supporthealth.chat.ui.ChatActivity
import com.example.supporthealth.databinding.ActivityMainBinding
import com.example.supporthealth.home.ui.HomeFragment
import com.example.supporthealth.nutrition.main.ui.NutritionFragment
import com.example.supporthealth.nutrition.search.ui.SearchFragmentArgs
import com.example.supporthealth.profile.main.ui.ProfileFragment
import com.example.supporthealth.stress.main.ui.StressFragment

class   MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestHealthPermission()

        val intent = Intent(this, StepCounterService::class.java)
        this.startForegroundService(intent)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        binding.navView.setupWithNavController(navController)

        binding.buttonChat.setOnClickListener {
            navController.navigate(R.id.action_mainActivity_to_chatActivity)
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment,
                R.id.detailsFragment,
                R.id.safetyFragment,
                R.id.aboutApplicationFragment,
                R.id.devicesFragment,
                R.id.eatingFragment,
                R.id.navigation_search,
                R.id.navigation_product,
                R.id.statisticActivityFragment-> {
                    binding.navView.isVisible = false
                    binding.buttonChat.isVisible = false
                }
                else -> {
                    binding.navView.isVisible = true
                    binding.buttonChat.isVisible = true
                }
            }
        }
    }

    private fun requestHealthPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    1
                )
            }
        }
    }
}