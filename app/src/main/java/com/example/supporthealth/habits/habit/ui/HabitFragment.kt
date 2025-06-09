package com.example.supporthealth.habits.habit.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils.formatElapsedTime
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.supporthealth.app.HabitGoalUpdateWorker
import com.example.supporthealth.app.scheduleGoalAlarm
import com.example.supporthealth.databinding.FragmentHabitBinding
import com.example.supporthealth.habits.dialog.ui.HabitListDialogFragment
import com.example.supporthealth.habits.main.ui.HabitsViewModel
import com.example.supporthealth.main.domain.models.HabitEntity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HabitFragment : Fragment() {

    companion object {

        private const val MENU_EDIT_ID = 1
        private const val MENU_DELETE_ID = 2

        fun newInstance() = HabitFragment()
    }

    private val viewModel: HabitsViewModel by viewModel()
    private lateinit var binding: FragmentHabitBinding

    private val handler = Handler(Looper.getMainLooper())
    private var startTimeMillis: Long = 0L
    private var currentHabit: HabitEntity? = null

    private val args: HabitFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.observeHabit(args.habitId).observe(viewLifecycleOwner) { habit ->
            habit?.let {
                currentHabit = it
                binding.title.text = it.name
                binding.attemptValue.text = it.attempt.toString()
                binding.targetValue.text = "${it.target} ${getDaySuffix(it.target)}"
                binding.recordValue.text = "${it.record} ${getDaySuffix(it.record)}"
                startTimeMillis = it.attemptStartTimeMillis

                val now = System.currentTimeMillis()
                val hasProgress = now > it.attemptStartTimeMillis
                binding.buttonOver.isVisible = hasProgress
                startTimer()
            }

            binding.buttonOver.setOnClickListener {
                habit?.let { habitNonNull ->
                    showRestartConfirmationDialog(habitNonNull)
                }
            }

            binding.buttonMore.setOnClickListener { anchorView ->
                val popup = PopupMenu(requireContext(), anchorView)
                val menu = popup.menu

                menu.add(0, MENU_EDIT_ID, 0, "Изменить")
                menu.add(0, MENU_DELETE_ID, 1, "Удалить")

                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        MENU_EDIT_ID -> {
                            showDialog(habit.id)
                            true
                        }
                        MENU_DELETE_ID -> {
                            showDeleteConfirmationDialog(habit)
                            true
                        }
                        else -> false
                    }
                }

                popup.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopTimer()
    }

    private fun showDialog(habitId: Long) {
        val habitDialog = HabitListDialogFragment.newInstance(habitId)
        habitDialog.show(parentFragmentManager, "HabitDialog")
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            updateTimer()
            handler.postDelayed(this, 300)
        }
    }

    private fun startTimer() {
        handler.post(updateRunnable)
    }

    private fun stopTimer() {
        handler.removeCallbacks(updateRunnable)
    }

    private fun updateTimer() {

        val habit = currentHabit ?: return

        val now = System.currentTimeMillis()
        if (now < startTimeMillis) {
            val timeLeft = startTimeMillis - now
            binding.time.text = "До старта: ${formatElapsedTime(timeLeft)}"
            binding.staticDonut.updateSteps(0f, 1f)
        } else {
            val elapsed = now - startTimeMillis
            binding.time.text = formatElapsedTime(elapsed)

            val targetDays = habit.target
            if (targetDays > 0) {
                val elapsedDays = elapsed.toFloat() / (24 * 60 * 60 * 1000)
                binding.staticDonut.updateSteps(elapsedDays, targetDays.toFloat())
            } else {
                binding.staticDonut.updateSteps(0f, 1f)
            }
        }
    }


    private fun formatElapsedTime(elapsedMillis: Long): String {
        val seconds = (elapsedMillis / 1000) % 60
        val minutes = (elapsedMillis / (60 * 1000)) % 60
        val hours = (elapsedMillis / (60 * 60 * 1000)) % 24
        val days = elapsedMillis / (24 * 60 * 60 * 1000)

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
    }

    private fun restartHabit(habit: HabitEntity) {

        val now = System.currentTimeMillis()
        val elapsedDays = ((now - habit.attemptStartTimeMillis).toFloat() / (24 * 60 * 60 * 1000))
        val newRecord = maxOf(habit.record.toFloat(), elapsedDays).toInt()
        val newAttempt = habit.attempt + 1

        val updatedHabit = habit.copy(
            target = 1,
            record = newRecord,
            attempt = newAttempt,
            attemptStartTimeMillis = now
        )

        viewModel.updateHabit(updatedHabit)

        startTimeMillis = now
        stopTimer()
        startTimer()

        binding.attemptValue.text = "${newAttempt} ${getDaySuffix(newAttempt)}"
        binding.recordValue.text = "${newRecord} ${getDaySuffix(newRecord)}"
        binding.targetValue.text = updatedHabit.target.toString()
    }

    private fun showRestartConfirmationDialog(habit: HabitEntity) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Начать заново?")
            .setMessage("Если вы начнете заново, текущий прогресс будет сброшен.")
            .setPositiveButton("Да") { _, _ ->
                scheduleGoalAlarm(requireContext(), habit)
                restartHabit(habit)
            }
            .setNegativeButton("Отмена", null)
            .create()

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(habit: HabitEntity) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Удалить привычку?")
            .setMessage("Вы уверены, что хотите удалить эту привычку? Это действие нельзя будет отменить.")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteHabit(habit)
                findNavController().navigateUp()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun getDaySuffix(number: Int): String {
        val n = number % 100
        if (n in 11..14) return "дней"
        return when (n % 10) {
            1 -> "день"
            2, 3, 4 -> "дня"
            else -> "дней"
        }
    }
}