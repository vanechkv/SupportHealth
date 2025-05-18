package com.example.supporthealth.chat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.supporthealth.R
import com.example.supporthealth.chat.domain.api.interactor.ChatInteractor
import com.example.supporthealth.chat.domain.models.ChatMessage
import com.example.supporthealth.chat.domain.models.ChatState

class ChatViewModel(private val chatInteractor: ChatInteractor) : ViewModel() {

    private var message: String? = null

    private val stateLiveData = MutableLiveData<ChatState>()
    fun observeState(): LiveData<ChatState> = stateLiveData

    fun sendMessage(newMessage: String) {
        if (newMessage.isNotEmpty()) {
            renderState(ChatState.Loading)

            chatInteractor.sendMessage(
                newMessage,
                object : ChatInteractor.ChatConsumer {
                    override fun consume(
                        text: ChatMessage.Text?,
                        mealSuggestion: ChatMessage.MealSuggestion?,
                        error: String?
                    ) {
                        when {
                            error != null -> {
                                renderState(ChatState.Error(R.string.internet_error))
                            }

                            text != null -> {
                                renderState(ChatState.Message(text))
                            }

                            mealSuggestion != null -> {
                                renderState(ChatState.MealSuggestion(mealSuggestion))
                            }

                            else -> renderState(ChatState.Empty(R.string.search_not_found))
                        }
                    }
                })
        }
    }

    private fun renderState(state: ChatState) {
        stateLiveData.postValue(state)
    }
}