package com.example.supporthealth.nutrition.search.data.dto

import com.google.gson.annotations.SerializedName

class ProductSearchResponse(@SerializedName("results") val products: List<ProductDto>) : Response()