package com.example.supporthealth.welcome.onbording.ui

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentOnbordingBinding

class OnbordingFragment : Fragment() {

    companion object {
        fun newInstance() = OnbordingFragment()
    }

    private val viewModel: OnbordingViewModel by viewModels()
    private lateinit var binding: FragmentOnbordingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnbordingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonNext.setOnClickListener {
            findNavController().navigate(R.id.action_onbordingFragment_to_detailsOnBordingFragment)
        }

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_onbordingFragment_to_loginFragment)
        }
    }
}