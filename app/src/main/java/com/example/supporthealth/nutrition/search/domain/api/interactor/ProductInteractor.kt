package com.example.supporthealth.nutrition.search.domain.api.interactor

import com.example.supporthealth.nutrition.search.domain.models.Product

interface ProductInteractor {

    fun searchProduct(expression: String, consumer: ProductConsumer)

    interface ProductConsumer {
        fun consume(foundProduct: List<Product>?, error: String?)
    }
}