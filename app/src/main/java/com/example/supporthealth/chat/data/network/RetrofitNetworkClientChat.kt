package com.example.supporthealth.chat.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.supporthealth.chat.data.NetworkClientChat
import com.example.supporthealth.chat.data.dto.ChatRequest
import com.example.supporthealth.chat.data.dto.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClientChat(
    private val context: Context,
    private val chatApi: ChatApi
) : NetworkClientChat {
    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }

        if (dto !is ChatRequest) {
            return Response().apply { resultCode = 400 }
        }

        return try {
            val resp = chatApi.sendMessage(dto.message).execute()
            val body = resp.body()
            return if (body != null) {
                Response().apply {
                    resultCode = resp.code()
                    text = body.text
                    mealSuggestion = body.mealSuggestion
                }
            } else {
                Response().apply { resultCode = resp.code() }
            }
        } catch (e: SocketTimeoutException) {
            Response().apply { resultCode = 408 }
        } catch (e: Exception) {
            val code = when (e) {
                is UnknownHostException -> 503
                is SocketTimeoutException -> 408
                else -> 500
            }
            Response().apply { resultCode = code }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}