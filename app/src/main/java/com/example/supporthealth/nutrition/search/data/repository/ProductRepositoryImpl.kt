package com.example.supporthealth.nutrition.search.data.repository

import com.example.supporthealth.nutrition.search.data.NetworkClient
import com.example.supporthealth.nutrition.search.data.dto.ProductSearchRequest
import com.example.supporthealth.nutrition.search.data.storage.ProductHistoryStorage
import com.example.supporthealth.nutrition.search.domain.api.repository.ProductRepository
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.nutrition.search.domain.models.Resource

class ProductRepositoryImpl(
    private val networkClient: NetworkClient,
    private val productsHistoryStorage: ProductHistoryStorage
) : ProductRepository {


    override fun searchProduct(expression: String): Resource<List<Product>> {
        val response = networkClient.doRequest(ProductSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("")
            }

            200 -> {
                Resource.Success(response.products.map {
                    Product(
                        it.productId,
                        it.name,
                        it.calories.toInt(),
                        it.proteins,
                        it.fats,
                        it.carbs
                    )
                })
            }

            else -> {
                Resource.Error("")
            }
        }
    }

    override fun saveProductToHistory(product: Product, historyProductList: ArrayList<Product>) {
        productsHistoryStorage.saveProduct(product, historyProductList)
    }

    override fun saveHistory(products: List<Product>) {
        productsHistoryStorage.saveProducts(products)
    }

    override fun saveProduct(product: Product) {
        productsHistoryStorage.saveProductEating(product)
    }

    override fun getProduct(): Product {
        return productsHistoryStorage.getProduct()
    }

    override fun getHistory(): List<Product> {
        return productsHistoryStorage.getProducts()
    }

    override fun clearHistory() {
        productsHistoryStorage.clear()
    }
}