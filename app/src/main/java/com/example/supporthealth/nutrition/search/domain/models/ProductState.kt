package com.example.supporthealth.nutrition.search.domain.models

interface ProductState {

    object Loading : ProductState

    data class Content(
        val products: List<Product>
    ) : ProductState

    data class Error(
        val errorMessage: Int
    ) : ProductState

    data class Empty(
        val message: Int
    ) : ProductState
}