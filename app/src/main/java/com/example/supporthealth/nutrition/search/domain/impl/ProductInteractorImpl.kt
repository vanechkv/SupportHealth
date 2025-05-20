package com.example.supporthealth.nutrition.search.domain.impl

import com.example.supporthealth.nutrition.search.domain.api.interactor.ProductInteractor
import com.example.supporthealth.nutrition.search.domain.api.repository.ProductRepository
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.nutrition.search.domain.models.Resource
import java.util.concurrent.Executors

class ProductInteractorImpl(private val repository: ProductRepository) : ProductInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchProduct(expression: String, consumer: ProductInteractor.ProductConsumer) {
        executor.execute {
            when (val resource = repository.searchProduct(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
                }
            }
        }
    }

    override fun saveProductToHistory(product: Product, historyProductList: ArrayList<Product>) {
        repository.saveProductToHistory(product, historyProductList)
    }

    override fun saveHistory(products: List<Product>) {
        repository.saveHistory(products)
    }

    override fun saveProduct(product: Product) {
        repository.saveProduct(product)
    }

    override fun getProduct(): Product {
        return repository.getProduct()
    }

    override fun getHistory(): List<Product> {
        return repository.getHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}