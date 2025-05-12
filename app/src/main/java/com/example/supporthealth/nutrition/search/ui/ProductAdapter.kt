package com.example.supporthealth.nutrition.search.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.nutrition.search.domain.models.Product

class ProductAdapter(
    private var products: ArrayList<Product>,
    private val onProductClick: (Product) -> Unit
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

    fun updateProduct(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }
}