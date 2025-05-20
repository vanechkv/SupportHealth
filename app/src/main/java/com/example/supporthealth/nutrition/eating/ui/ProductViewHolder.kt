package com.example.supporthealth.nutrition.eating.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.main.domain.models.ProductEntity
import com.example.supporthealth.nutrition.eating.domain.models.ProductWithGrams
import com.example.supporthealth.nutrition.search.domain.models.Product

class ProductViewHolder(parent: ViewGroup, private val onProductClick: (ProductWithGrams) -> Unit) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.product_view, parent, false)
    ) {
    private val productName: TextView = itemView.findViewById(R.id.product_name)
    private val calories: TextView = itemView.findViewById(R.id.product_calories)
    private val imageProduct: ImageView = itemView.findViewById(R.id.icon_add)
    private val grams: TextView = itemView.findViewById(R.id.grams)

    private var currentProduct: ProductWithGrams? = null

    init {
        itemView.setOnClickListener {
            currentProduct?.let(onProductClick)
        }
    }

    fun bind(product: ProductWithGrams) {
        currentProduct = product
        imageProduct.isVisible = false
        productName.text = product.name
        calories.text = "${(product.calories * product.grams / 100f).toInt()} ккал"
        grams.text = "— ${product.grams}"
    }
}