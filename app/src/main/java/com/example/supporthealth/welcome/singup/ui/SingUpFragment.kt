package com.example.supporthealth.welcome.singup.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentSingUpBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SingUpFragment : Fragment() {

    companion object {
        fun newInstance() = SingUpFragment()
    }

    private val viewModel: SingUpViewModel by viewModel()

    private lateinit var binding: FragmentSingUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonCheckIn.setOnClickListener {
            findNavController().navigate(R.id.action_singUpFragment_to_detailsOnBordingFragment)
        }
    }
}