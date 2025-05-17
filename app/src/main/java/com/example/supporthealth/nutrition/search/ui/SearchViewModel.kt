package com.example.supporthealth.nutrition.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.supporthealth.R
import com.example.supporthealth.nutrition.search.domain.api.interactor.ProductInteractor
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.example.supporthealth.nutrition.search.domain.models.ProductState

class SearchViewModel(private val productInteractor: ProductInteractor) : ViewModel() {

    private var searchText: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private val stateLiveData = MutableLiveData<ProductState>()
    fun observeState(): LiveData<ProductState> = stateLiveData

    private val historyLiveData = MutableLiveData<List<Product>>()
    fun observeHistory(): LiveData<List<Product>> = historyLiveData

    private val historyProductList = ArrayList<Product>()

    init {
        historyProductList.add(Product(
            productId = "default_001",
            name = "Продукт 1",
            calories = 100,
            protein = 10f,
            fat = 5f,
            carbs = 5f
        ))
        historyProductList.add(Product(
            productId = "default_002",
            name = "Продукт 2",
            calories = 200,
            protein = 20f,
            fat = 10f,
            carbs = 10f
        ))
        historyProductList.add(Product(
            productId = "default_003",
            name = "Продукт 3",
            calories = 300,
            protein = 30f,
            fat = 15f,
            carbs = 15f
        ))
        historyLiveData.postValue(historyProductList)
    }

    fun onProductClick(product: Product) {
        productInteractor.saveProductToHistory(product, historyProductList)
        historyLiveData.postValue(historyProductList)
    }

    fun clearHistory() {
        historyProductList.clear()
        productInteractor.clearHistory()
        historyLiveData.postValue(historyProductList)
    }

    fun saveHistory() {
        productInteractor.saveHistory(historyProductList)
    }

    private fun search(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(ProductState.Loading)

            productInteractor.searchProduct(
                newSearchText,
                object : ProductInteractor.ProductConsumer {
                    override fun consume(foundProduct: List<Product>?, error: String?) {
                        val products = mutableListOf<Product>()
                        if (foundProduct != null) {
                            products.addAll(foundProduct)
                        }

                        when {
                            error != null -> {
                                renderState(
                                    ProductState.Error(
                                        errorMessage = R.string.internet_error
                                    )
                                )
                            }

                            products.isEmpty() -> {
                                renderState(
                                    ProductState.Empty(
                                        message = R.string.search_not_found
                                    )
                                )
                            }

                            else -> {
                                renderState(
                                    ProductState.Content(
                                        products = products
                                    )
                                )
                            }
                        }
                    }

                }
            )
        }
    }

    private fun renderState(state: ProductState) {
        stateLiveData.postValue(state)
    }

    fun searchDebounce(changedText: String) {
        this.searchText = changedText

        searchRunnable?.let { handler.removeCallbacks(it) }

        if (changedText.isBlank()) {
            renderState(ProductState.SearchHistory(historyProductList))
            return
        }

        searchRunnable = Runnable { search(changedText) }

        handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}