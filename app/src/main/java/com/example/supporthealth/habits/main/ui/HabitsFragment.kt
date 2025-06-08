package com.example.supporthealth.habits.main.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.supporthealth.databinding.FragmentHabitsBinding
import com.example.supporthealth.habits.dialog.ui.HabitListDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HabitsFragment : Fragment() {

    private val viewModel: HabitsViewModel by viewModel()

    private lateinit var binding: FragmentHabitsBinding
    private lateinit var adapter: HabitsAdapter

    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private val updateRunnable = object : Runnable {
        override fun run() {
            val now = System.currentTimeMillis()
            if (::adapter.isInitialized) {
                adapter.updateProgressForVisibleItems(now)
            }
            handler.postDelayed(this, 300)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeHabitsData().observe(viewLifecycleOwner) { habits ->
            if (habits.isNullOrEmpty()) {
                binding.recyclerHabits.isVisible = false
                binding.buttonAdd.isVisible = false
                binding.errorView.error.isVisible = true
                binding.errorView.titleError.text = "Список пуст"
                binding.errorView.descriptionError.isVisible = true
                binding.errorView.descriptionError.text = "Похоже, вы еще не добавляли вредных привычек. Добавьте их, чтобы отслеживать и постепенно избавляться от них"
                binding.errorView.errorButton.text = "Добавить привычку"
                binding.errorView.errorButton.setOnClickListener {
                    showDialog()
                }
            } else {
                binding.errorView.error.isVisible = false
                binding.recyclerHabits.isVisible = true
                binding.buttonAdd.isVisible = true
                adapter = HabitsAdapter(habits) {
                    onHabitClick(it.id)
                }
                binding.recyclerHabits.adapter = adapter
                binding.buttonAdd.setOnClickListener {
                    showDialog()
                }
            }
        }
    }

    private fun onHabitClick(habitId: Long) {
        val action = HabitsFragmentDirections
            .actionNavigationHabitsToHabitFragment(
                habitId = habitId
            )
        findNavController().navigate(action)
    }

    private fun showDialog() {
        val habitDialog = HabitListDialogFragment.newInstance(null)
        habitDialog.show(parentFragmentManager, "HabitDialog")
    }

    override fun onResume() {
        super.onResume()
        handler.post(updateRunnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(updateRunnable)
    }
}