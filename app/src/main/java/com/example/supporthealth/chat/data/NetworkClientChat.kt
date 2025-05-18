package com.example.supporthealth.chat.data

import com.example.supporthealth.chat.data.dto.Response

interface NetworkClientChat {
    fun doRequest(dto: Any): Response
}