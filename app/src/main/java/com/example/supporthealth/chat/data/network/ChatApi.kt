package com.example.supporthealth.chat.data.network

import com.example.supporthealth.chat.data.dto.ChatResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatApi {

    @GET("")
    fun sendMessage(@Query("message") message: String): Call<ChatResponse>
}