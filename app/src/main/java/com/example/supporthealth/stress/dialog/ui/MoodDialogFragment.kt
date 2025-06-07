package com.example.supporthealth.stress.dialog.ui

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.example.supporthealth.R
import com.example.supporthealth.databinding.FragmentMoodDialogListDialogBinding
import com.example.supporthealth.stress.dialog.domain.DayPart
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate

class MoodDialogFragment : BottomSheetDialogFragment() {

    companion object {

        private const val ARG_MOOD_ID = "arg_mood_id"
        private const val ARG_DATE = "arg_date"
        private const val ARG_DAY_PERIOD = "arg_day_period"

        fun newInstance(moodId: Long?, date: String, dayPart: DayPart): MoodDialogFragment =
            MoodDialogFragment().apply {
                arguments = Bundle().apply {
                    if (moodId != null) putLong(ARG_MOOD_ID, moodId)
                    putString(ARG_DATE, date)
                    putString(ARG_DAY_PERIOD, dayPart.displayName)
                }
            }

    }

    private var _binding: FragmentMoodDialogListDialogBinding? = null
    private val viewModel: StressViewModel by viewModel()

    private val binding get() = _binding!!

    private var currentEnergyColor: Int = 0

    override fun onStart() {
        super.onStart()
        val dialog = dialog as? BottomSheetDialog ?: return
        val bottomSheet =
            dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return
        val behavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheet.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        bottomSheet.requestLayout()

        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMoodDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonBack.setOnClickListener {
            dismiss()
        }

        binding.title.text = "${formatDate(arguments?.getString(ARG_DATE))} — ${
            arguments?.getString(
                ARG_DAY_PERIOD
            )
        }"

        viewModel.observeMoodData().observe(viewLifecycleOwner) { mood ->
            if (mood != null) {
                updateMoodUI(mood.moodLevel)
                updateEnergyUI(mood.energyLevel)
            } else {
                updateMoodUI(3)
                updateEnergyUI(3)
            }
        }

        val moodId = arguments?.getLong(ARG_MOOD_ID)
        if (moodId != null) {
            viewModel.getMoodById(moodId)
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
            val date = arguments?.getString(ARG_DATE)
            val dayPartString = arguments?.getString(ARG_DAY_PERIOD)
            val dayPart = dayPartString?.let { str ->
                DayPart.values().find { it.displayName.equals(str, ignoreCase = true) }
            }
            if (date != null && dayPart != null) {
                viewModel.saveMoodData(date, dayPart, mood, energy)
            }
            dismiss()
        }

        updateMoodUI(binding.moodSeekBar.progress)
        updateEnergyUI(binding.energySeekBar.progress)
    }

    private fun updateMoodUI(moodValue: Int) {
        if (binding.moodSeekBar.progress != moodValue) {
            binding.moodSeekBar.progress = moodValue
        }
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
            binding.moodEmoji.setColorFilter(animatedColor)
            binding.moodSeekBar.progressDrawable.setTint(animatedColor)
            binding.moodSeekBar.thumb.setTint(animatedColor)
        }
        animator.start()
    }

    private fun updateEnergyUI(energyValue: Int) {
        if (binding.energySeekBar.progress != energyValue) {
            binding.energySeekBar.progress = energyValue
        }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatDate(dateString: String?): String {
        val date = LocalDate.parse(dateString)
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val tomorrow = today.plusDays(1)

        return when (date) {
            yesterday -> "Вчера"
            today -> "Сегодня"
            tomorrow -> "Завтра"
            else -> {
                val dayOfWeek = when (date.dayOfWeek) {
                    java.time.DayOfWeek.MONDAY -> "Пн"
                    java.time.DayOfWeek.TUESDAY -> "Вт"
                    java.time.DayOfWeek.WEDNESDAY -> "Ср"
                    java.time.DayOfWeek.THURSDAY -> "Чт"
                    java.time.DayOfWeek.FRIDAY -> "Пт"
                    java.time.DayOfWeek.SATURDAY -> "Сб"
                    java.time.DayOfWeek.SUNDAY -> "Вс"
                }
                "$dayOfWeek, ${date.dayOfMonth}"
            }
        }
    }
}