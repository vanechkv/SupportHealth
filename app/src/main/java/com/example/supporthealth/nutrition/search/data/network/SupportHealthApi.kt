package com.example.supporthealth.nutrition.search.data.network

import com.example.supporthealth.nutrition.search.data.dto.ProductDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SupportHealthApi {

    @GET("/products/search")
    fun search(@Query("query") query: String): Call<List<ProductDto>>
}