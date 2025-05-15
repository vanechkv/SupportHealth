package com.example.supporthealth.nutrition.search.data.storage

import android.content.SharedPreferences
import com.example.supporthealth.nutrition.search.domain.models.Product
import com.google.gson.Gson

class ProductHistoryStorage(
    private val pref: SharedPreferences,
    private val gson: Gson
) {
    companion object {
        private const val HISTORY_PRODUCTS_LIST_KEY = "key_history_product_key"
        private const val NEW_PRODUCT_IN_HISTORY_KEY = "new_product_in_history_key"
    }

    fun saveProduct(product: Product, historyProductList: ArrayList<Product>) {
        historyProductList.removeIf { it.productId == product.productId }
        historyProductList.add(0, product)
        if (historyProductList.size > 10) {
            historyProductList.removeAt(historyProductList.lastIndex)
        }

        pref.edit()
            .putString(NEW_PRODUCT_IN_HISTORY_KEY, createJsonFromProduct(product))
            .apply()
        saveProducts(historyProductList)
    }

    fun getProduct(): Product {
        val json = pref.getString(NEW_PRODUCT_IN_HISTORY_KEY, null)
        return createProductFromJson(json)
    }

    fun getProducts(): List<Product> {
        val json = pref.getString(HISTORY_PRODUCTS_LIST_KEY, null)
        return createProductListFromJson(json)
    }

    fun clear() {
        pref.edit()
            .remove(HISTORY_PRODUCTS_LIST_KEY)
            .apply()
    }

    fun saveProducts(products: List<Product>) {
        val json = createJsonFromProductList(ArrayList(products))
        pref.edit()
            .putString(HISTORY_PRODUCTS_LIST_KEY, json)
            .apply()
    }

    private fun createJsonFromProductList(products: ArrayList<Product>): String {
        return gson.toJson(products)
    }

    private fun createJsonFromProduct(product: Product): String {
        return gson.toJson(product)
    }


    private fun createProductFromJson(json: String?): Product {
        return gson.fromJson(json, Product::class.java)
    }

    private fun createProductListFromJson(json: String?): ArrayList<Product> {
        return if (json.isNullOrEmpty()) {
            ArrayList()
        } else {
            val products = gson.fromJson(json, Array<Product>::class.java)
            ArrayList(products.toList())
        }
    }
}