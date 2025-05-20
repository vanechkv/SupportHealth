package com.example.supporthealth.nutrition.eating.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.main.domain.models.ProductEntity
import com.example.supporthealth.nutrition.eating.domain.models.ProductWithGrams
import com.example.supporthealth.nutrition.search.domain.models.Product

class ProductAdapter(
    private var products: ArrayList<ProductWithGrams>,
    private val onProductClick: (ProductWithGrams) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(parent, onProductClick)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun updateProduct(newProducts: List<ProductWithGrams>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}