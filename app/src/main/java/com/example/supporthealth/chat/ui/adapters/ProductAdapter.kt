package com.example.supporthealth.chat.ui.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.chat.domain.models.ProductAi
import com.example.supporthealth.nutrition.search.domain.models.Product

class ProductAdapter(
    private val products: List<ProductAi>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.product_name)
        val calories = view.findViewById<TextView>(R.id.product_calories)
        val productGrams = view.findViewById<TextView>(R.id.grams)
        val grams = view.findViewById<EditText>(R.id.grams_edit_text)
        var watcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_meal, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.name.text = product.name
        holder.calories.text = "${product.calories} калл"
        holder.productGrams.text = "— ${product.grams.toInt()} г"
        holder.watcher?.let { holder.grams.removeTextChangedListener(it) }
        holder.grams.setText(product.grams.toInt().toString())
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    holder.grams.error = holder.itemView.context.getString(R.string.error_grams_zero)
                    product.grams = 0f
                } else {
                    holder.grams.error = null
                    val newValue = s.toString().toFloatOrNull() ?: 0f
                    product.grams = newValue
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        holder.grams.addTextChangedListener(watcher)
        holder.watcher = watcher
    }

    fun getCurrentProducts(): List<ProductAi> = products
}