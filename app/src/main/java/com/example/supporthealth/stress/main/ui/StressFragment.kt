package com.example.supporthealth.stress.main.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentStressBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class StressFragment : Fragment(R.layout.fragment_stress) {

    private var _binding: FragmentStressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StressViewModel by viewModel()

    private var currentEnergyColor: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStressBinding.bind(view)

        setupMoodChart()

        binding.buttonBarChar.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_stress_to_statisticMoodFragment)
        }

        binding.moodSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateMoodUI(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.energySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateEnergyUI(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.saveButton.setOnClickListener {
            val mood = binding.moodSeekBar.progress
            val energy = binding.energySeekBar.progress
            viewModel.saveMoodData(mood, energy)
        }

        updateMoodUI(binding.moodSeekBar.progress)
        updateEnergyUI(binding.energySeekBar.progress)
    }

    private fun updateMoodUI(moodValue: Int) {
        val moodDescription = viewModel.getMoodDescription(moodValue)
        val colorResId = viewModel.getMoodColorResId(moodValue)
        val emojiResId = viewModel.getMoodEmojiResId(moodValue)
        val newColor = ContextCompat.getColor(requireContext(), colorResId)

        binding.moodText.text = moodDescription
        binding.moodEmoji.setImageResource(emojiResId)

        val oldColor = binding.moodText.currentTextColor

        val animator = ValueAnimator.ofObject(ArgbEvaluator(), oldColor, newColor)
        animator.duration = 250
        animator.addUpdateListener { anim ->
            val animatedColor = anim.animatedValue as Int
            binding.saveButton.setBackgroundColor(animatedColor)
            binding.moodText.setTextColor(animatedColor)
            binding.moodSeekBar.progressDrawable.setTint(animatedColor)
            binding.moodSeekBar.thumb.setTint(animatedColor)
        }
        animator.start()
    }

    private fun updateEnergyUI(energyValue: Int) {
        val energyDescription = viewModel.getEnergyDescription(energyValue)
        val colorResId = viewModel.getEnergyColorResId(energyValue)
        val newColor = ContextCompat.getColor(requireContext(), colorResId)

        binding.energyLabel.text = energyDescription

        if (currentEnergyColor == 0) {
            currentEnergyColor = ContextCompat.getColor(requireContext(), R.color.mood_default)
        }

        val animator = ValueAnimator.ofObject(ArgbEvaluator(), currentEnergyColor, newColor)
        animator.duration = 250
        animator.addUpdateListener { anim ->
            val animatedColor = anim.animatedValue as Int
            binding.energySeekBar.progressDrawable.setTint(animatedColor)
            binding.energySeekBar.thumb.setTint(animatedColor)
            binding.energyLabel.setTextColor(animatedColor)
        }
        animator.start()

        currentEnergyColor = newColor
    }

    private fun setupMoodChart() {
        // Заглушка: для будущей инициализации графика по дням
        // chart data can be set here once history is available
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
