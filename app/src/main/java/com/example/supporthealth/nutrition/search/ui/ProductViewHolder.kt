package com.example.supporthealth.nutrition.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.nutrition.search.domain.models.Product

class ProductViewHolder(parent: ViewGroup, private val onProductClick: (Product) -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_view, parent, false)
    ) {
    private val productName: TextView = itemView.findViewById(R.id.product_name)
    private val calories: TextView = itemView.findViewById(R.id.product_calories)

    private var currentProduct: Product? = null

    init {
        itemView.setOnClickListener {
            currentProduct?.let(onProductClick)
        }
    }

    fun bind(product: Product) {
        currentProduct = product
        productName.text = product.name
        calories.text = "${product.calories} ккал"
    }
}