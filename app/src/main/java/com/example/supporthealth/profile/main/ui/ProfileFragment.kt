package com.example.supporthealth.profile.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentProfileBinding
import com.example.supporthealth.welcome.main.ui.WelcomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModel()

    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myDetails.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                findNavController().navigate(R.id.action_navigation_profile_to_detailsFragment)
            }
        }

        binding.devices.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                findNavController().navigate(R.id.action_navigation_profile_to_devicesFragment)
            }
        }

        binding.support.setOnClickListener {
            viewModel.openSupport()
        }

        binding.aboutTheApplication.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                findNavController().navigate(R.id.action_navigation_profile_to_aboutApplicationFragment)
            }
        }

        binding.safety.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                findNavController().navigate(R.id.action_navigation_profile_to_safetyFragment)
            }
        }

        binding.notifications.setOnClickListener {

        }

        binding.buttonRemove.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            viewModel.clearUserDetails()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getDarkTheme().observe(viewLifecycleOwner) { isDarkTheme ->
            binding.switchDarkTheme.isChecked = isDarkTheme
        }

        binding.switchDarkTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.setDarkTheme(checked)
        }
    }
}