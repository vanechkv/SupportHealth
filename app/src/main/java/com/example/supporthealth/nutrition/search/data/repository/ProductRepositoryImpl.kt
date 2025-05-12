package com.example.supporthealth.nutrition.search.data.repository

import com.example.supporthealth.nutrition.search.data.NetworkClient
import com.example.supporthealth.nutrition.search.data.dto.ProductSearchRequest
import com.example.supporthealth.nutrition.search.data.dto.ProductSearchResponse
import com.example.supporthealth.nutrition.search.domain.api.repository.ProductRepository
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.nutrition.search.domain.models.Resource

class ProductRepositoryImpl(
    private val networkClient: NetworkClient
) : ProductRepository {


    override fun searchProduct(expression: String): Resource<List<Product>> {
        val response = networkClient.doRequest(ProductSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("")
            }

            200 -> {
                Resource.Success((response as ProductSearchResponse).products.map {
                    Product(
                        it.productId,
                        it.name,
                        it.calories.toInt(),
                        it.protein.toInt(),
                        it.fat.toInt(),
                        it.carbs.toInt()
                    )
                })
            }

            else -> {
                Resource.Error("")
            }
        }
    }
}