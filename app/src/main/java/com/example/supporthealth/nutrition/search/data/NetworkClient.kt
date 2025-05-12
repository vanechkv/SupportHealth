package com.example.supporthealth.nutrition.search.data

import com.example.supporthealth.nutrition.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}