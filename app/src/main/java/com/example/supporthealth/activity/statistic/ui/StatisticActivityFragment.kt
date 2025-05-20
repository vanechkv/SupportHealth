package com.example.supporthealth.activity.statistic.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.databinding.FragmentStatisticActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticActivityFragment : Fragment() {

    companion object {
        fun newInstance() = StatisticActivityFragment()
    }

    private val viewModel: StatisticActivityViewModel by viewModel()
    private lateinit var binding: FragmentStatisticActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}