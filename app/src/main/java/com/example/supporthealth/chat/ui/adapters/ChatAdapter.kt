package com.example.supporthealth.chat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.chat.domain.models.ChatMessage

class ChatAdapter(
    private val items: List<ChatMessage>,
    private val onAddMeal: (ChatMessage.MealSuggestion) -> Unit,
    private val onCancelMeal: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val LOADING = 0
        private const val TYPE_TEXT_AI = 1
        private const val TYPE_TEXT_USER = 2
        private const val TYPE_MEAL = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            LOADING -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.loading_view, parent, false)
            )

            TYPE_TEXT_USER -> TextViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.message_user, parent, false)
            )

            TYPE_TEXT_AI -> TextViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.message_ai, parent, false)
            )

            TYPE_MEAL -> MealViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.message_meal, parent, false)
            )

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is ChatMessage.Loading -> LOADING

            is ChatMessage.Text -> {
                if (item.isUser) {
                    TYPE_TEXT_USER
                } else {
                    TYPE_TEXT_AI
                }
            }

            is ChatMessage.MealSuggestion -> TYPE_MEAL
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ChatMessage.Loading -> {}
            is ChatMessage.Text -> (holder as TextViewHolder).bind(item)
            is ChatMessage.MealSuggestion -> (holder as MealViewHolder).bind(item)
        }
    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
    }

    inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.message)
        fun bind(msg: ChatMessage.Text) {
            textView.text = msg.text
        }
    }

    inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateView = view.findViewById<TextView>(R.id.date)
        val mealTypeView = view.findViewById<TextView>(R.id.mealType)
        val productsRecycler = view.findViewById<RecyclerView>(R.id.recycler_products)
        val buttonAdd = view.findViewById<Button>(R.id.button_add)
        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)

        fun bind(msg: ChatMessage.MealSuggestion) {
            dateView.text = msg.date
            mealTypeView.text = msg.mealType.displayName
            val productsAdapter = ProductAdapter(msg.products)
            productsRecycler.adapter = productsAdapter

            buttonAdd.setOnClickListener {
                onAddMeal(msg.copy(products = productsAdapter.getCurrentProducts()))
            }
            buttonCancel.setOnClickListener {
                onCancelMeal(adapterPosition)
            }
        }
    }
}