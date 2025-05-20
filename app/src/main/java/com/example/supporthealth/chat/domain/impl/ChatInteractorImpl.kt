package com.example.supporthealth.chat.domain.impl

import com.example.supporthealth.chat.domain.api.interactor.ChatInteractor
import com.example.supporthealth.chat.domain.api.repository.ChatRepository
import com.example.supporthealth.chat.domain.models.ChatMessage
import com.example.supporthealth.chat.domain.models.Resource
import java.util.concurrent.Executors

class ChatInteractorImpl(private val repository: ChatRepository) : ChatInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun sendMessage(message: String, consumer: ChatInteractor.ChatConsumer) {
        executor.execute {
            when (val response = repository.sendMessage(message)) {
                is Resource.Success -> {
                    consumer.consume(response.data, null)
                }
                is Resource.Error -> consumer.consume(null, response.message)
            }
        }
    }
}