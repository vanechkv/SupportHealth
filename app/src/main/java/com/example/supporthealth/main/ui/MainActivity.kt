package com.example.supporthealth.main.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.supporthealth.R
import com.example.supporthealth.activity.ui.ActivityFragment
import com.example.supporthealth.databinding.ActivityMainBinding
import com.example.supporthealth.home.ui.HomeFragment
import com.example.supporthealth.nutrition.main.ui.NutritionFragment
import com.example.supporthealth.profile.main.ui.ProfileFragment
import com.example.supporthealth.stress.ui.StressFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailsFragment,
                R.id.detailsFragment,
                R.id.safetyFragment,
                R.id.aboutApplicationFragment,
                R.id.devicesFragment,
                R.id.eatingFragment,
                R.id.navigation_search,
                R.id.navigation_product -> {
                    binding.navView.isVisible = false
                }

                else -> {
                    binding.navView.isVisible = true
                }
            }
        }
    }
}