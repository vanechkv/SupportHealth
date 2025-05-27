package com.example.supporthealth.habits.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.supporthealth.databinding.FragmentHabitsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HabitsFragment : Fragment() {

    private val viewModel: HabitsViewModel by viewModel()

    private lateinit var binding: FragmentHabitsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}