package com.example.supporthealth.nutrition.search.domain.api.interactor

import com.example.supporthealth.nutrition.search.domain.models.Product

interface ProductInteractor {

    fun searchProduct(expression: String, consumer: ProductConsumer)

    fun saveProductToHistory(product: Product, historyProductList: ArrayList<Product>)

    fun saveHistory(products: List<Product>)

    fun saveProduct(product: Product)

    fun getProduct(): Product

    fun getHistory(): List<Product>

    fun clearHistory()

    interface ProductConsumer {
        fun consume(foundProduct: List<Product>?, error: String?)
    }
}