package com.example.supporthealth.nutrition.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.supporthealth.nutrition.search.data.NetworkClient
import com.example.supporthealth.nutrition.search.data.dto.ProductSearchRequest
import com.example.supporthealth.nutrition.search.data.dto.ProductSearchResponse
import com.example.supporthealth.nutrition.search.data.dto.Response
import java.net.SocketTimeoutException

class RetrofitNetworkClient(private val context: Context, private val supportHealthApi: SupportHealthApi) : NetworkClient {
    override fun doRequest(dto: Any): Response {
        if (isConnected() == false) {
            return Response().apply { resultCode = -1 }
        }
        if (dto !is ProductSearchRequest) {
            return Response().apply { resultCode == 400 }
        }

        return try {
            val resp = supportHealthApi.search(dto.expression).execute()
            val body = resp.body()
            return if (body != null) {
                body.apply { resultCode = resp.code() }
            }else {
                Response().apply { resultCode = resp.code() }
            }
        }catch (e: SocketTimeoutException) {
            Response().apply { resultCode = 408 }
        } catch (e: Exception) {
            Response().apply { resultCode = 500 }
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