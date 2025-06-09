package com.example.supporthealth.chat.ui

import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.supporthealth.R
import com.example.supporthealth.chat.domain.models.ChatMessage
import com.example.supporthealth.chat.domain.models.ChatState
import com.example.supporthealth.chat.ui.adapters.ChatAdapter
import com.example.supporthealth.databinding.ActivityChatBinding
import com.example.supporthealth.habits.dialog.ui.HabitListDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding

    private val viewModel: ChatViewModel by viewModel()

    private val messages = mutableListOf<ChatMessage>()

    private val adapter = ChatAdapter(
        messages,
        onAddMeal = { mealSuggestion ->
            onOkClick(mealSuggestion)
        }
    ).apply {
        onMealTypeChanged = { position, newType ->
            val item = items[position]
            if (item is ChatMessage.MealSuggestion) {
                val newMsg = item.copy(mealType = newType)
                (items as MutableList)[position] = newMsg
                notifyItemChanged(position)
            }
        }
        onDateChanged = { position, newDate ->
            val item = items[position]
            if (item is ChatMessage.MealSuggestion) {
                val updatedItem = item.copy(date = newDate.toString())
                (items as MutableList)[position] = updatedItem
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.observeState().observe(this) { state ->
            render(state)
        }

        binding.recyclerChat.adapter = adapter

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val hasText = !s.isNullOrEmpty()
                if (hasText) {
                    binding.buttonChat.isVisible = true
                } else {
                    binding.buttonChat.isVisible = false
                }
            }
        }

        binding.chatEditText.addTextChangedListener(textWatcher)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonChat.setOnClickListener {
            val text = binding.chatEditText.text.toString()
            messages.add(ChatMessage.Text(text, true))
            adapter.notifyDataSetChanged()
            binding.recyclerChat.smoothScrollToPosition(adapter.itemCount - 1)
            viewModel.sendMessage(text)
            binding.chatEditText.text.clear()

        }

        binding.buttonHelp.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val chatDialog = ChatDialogFragment.newInstance()
        chatDialog.show(supportFragmentManager, "ChatDialog")
    }

    private fun onOkClick(mealSuggestion: ChatMessage.MealSuggestion) {
        viewModel.addMeal(mealSuggestion)
    }

    private fun render(state: ChatState) {

        when (state) {
            is ChatState.Loading -> {
                messages.removeAll { it is ChatMessage.Loading }
                messages.add(ChatMessage.Loading)
                adapter.notifyDataSetChanged()
                binding.recyclerChat.smoothScrollToPosition(adapter.itemCount - 1)
            }

            is ChatState.MealSuggestion -> {
                messages.removeAll { it is ChatMessage.Loading }
                messages.add(state.mealSuggestion)
                adapter.notifyDataSetChanged()
                binding.recyclerChat.smoothScrollToPosition(adapter.itemCount - 1)
            }

            is ChatState.Empty -> {
                messages.removeAll { it is ChatMessage.Loading }
                messages.add(ChatMessage.Text(getText(state.message).toString(), false))
                adapter.notifyDataSetChanged()
                binding.recyclerChat.smoothScrollToPosition(adapter.itemCount - 1)
            }

            is ChatState.Error -> {
                messages.removeAll { it is ChatMessage.Loading }
                messages.add(ChatMessage.Text(getText(state.errorMessage).toString(), false))
                adapter.notifyDataSetChanged()
                binding.recyclerChat.smoothScrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}