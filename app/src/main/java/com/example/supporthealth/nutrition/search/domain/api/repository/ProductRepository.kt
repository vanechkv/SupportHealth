package com.example.supporthealth.nutrition.search.domain.api.repository

import com.example.supporthealth.nutrition.search.domain.api.interactor.ProductInteractor.ProductConsumer
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.nutrition.search.domain.models.Resource

interface ProductRepository {

    fun searchProduct(expression: String): Resource<List<Product>>

    fun saveProductToHistory(product: Product, historyProductList: ArrayList<Product>)

    fun saveHistory(products: List<Product>)

    fun saveProduct(product: Product)

    fun getProduct(): Product

    fun getHistory(): List<Product>

    fun clearHistory()
}