package com.example.supporthealth.chat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.supporthealth.R
import com.example.supporthealth.chat.domain.models.ChatMessage
import com.example.supporthealth.main.domain.models.MealType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ChatAdapter(
    val items: List<ChatMessage>,
    private val onAddMeal: (ChatMessage.MealSuggestion) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onMealTypeChanged: (position: Int, newType: MealType) -> Unit = { _, _ -> }
    var onDateChanged: (position: Int, newDate: LocalDate) -> Unit = { _, _ -> }

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
        val result = view.findViewById<TextView>(R.id.result)
        val dateView = view.findViewById<TextView>(R.id.date)
        val mealTypeView = view.findViewById<TextView>(R.id.mealType)
        val productsRecycler = view.findViewById<RecyclerView>(R.id.recycler_products)
        val buttonAdd = view.findViewById<Button>(R.id.button_add)
        val buttonCancel = view.findViewById<Button>(R.id.button_cancel)

        fun bind(msg: ChatMessage.MealSuggestion) {
            dateView.text = formatDate(msg.date)
            mealTypeView.text = msg.mealType.displayName
            mealTypeView.setOnClickListener {
                val mealTypes = MealType.entries.toTypedArray()
                val popup = PopupMenu(itemView.context, mealTypeView)
                mealTypes.forEachIndexed { idx, mealType ->
                    popup.menu.add(0, idx, idx, mealType.displayName)
                }
                popup.setOnMenuItemClickListener { menuItem ->
                    val selectedMealType = mealTypes[menuItem.itemId]
                    mealTypeView.text = selectedMealType.displayName
                    onMealTypeChanged(adapterPosition, selectedMealType)
                    true
                }
                popup.show()
            }
            dateView.setOnClickListener {
                val initialDate = parseAnyDate(msg.date) ?: LocalDate.now()

                val context = itemView.context
                val datePicker = android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val newDate = LocalDate.of(year, month + 1, dayOfMonth)
                        dateView.text = formatDate(newDate.toString())
                        onDateChanged(adapterPosition, newDate)
                    },
                    initialDate.year,
                    initialDate.monthValue - 1,
                    initialDate.dayOfMonth
                )
                datePicker.show()
            }
            val productsAdapter = ProductAdapter(msg.products)
            productsRecycler.adapter = productsAdapter

            buttonAdd.setOnClickListener {
                onAddMeal(msg.copy(products = productsAdapter.getCurrentProducts()))
                result.isVisible = true
                result.text = "— Добавлен"
                buttonAdd.isVisible = false
                buttonCancel.isVisible = false
                dateView.isEnabled = false
                mealTypeView.isEnabled = false
                productsAdapter.setEditable(false)
            }
            buttonCancel.setOnClickListener {
                result.isVisible = true
                result.text = "— Отменен"
                buttonAdd.isVisible = false
                buttonCancel.isVisible = false
                dateView.isEnabled = false
                mealTypeView.isEnabled = false
                productsAdapter.setEditable(false)
            }
        }
    }

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""

        val date: LocalDate = try {
            when {
                dateString.length <= 10 -> {
                    LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
                }
                else -> {
                    try {
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSS]")
                        java.time.LocalDateTime.parse(dateString, formatter).toLocalDate()
                    } catch (e: Exception) {
                        val safeDateString = if (dateString.length >= 19) {
                            dateString.substring(0, 19)
                        } else {
                            dateString
                        }
                        java.time.LocalDateTime.parse(
                            safeDateString,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                        ).toLocalDate()
                    }
                }
            }
        } catch (e: Exception) {
            return dateString
        }

        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val tomorrow = today.plusDays(1)

        return when (date) {
            yesterday -> "Вчера"
            today -> "Сегодня"
            tomorrow -> "Завтра"
            else -> {
                val dayOfWeek = when (date.dayOfWeek) {
                    java.time.DayOfWeek.MONDAY -> "Пн"
                    java.time.DayOfWeek.TUESDAY -> "Вт"
                    java.time.DayOfWeek.WEDNESDAY -> "Ср"
                    java.time.DayOfWeek.THURSDAY -> "Чт"
                    java.time.DayOfWeek.FRIDAY -> "Пт"
                    java.time.DayOfWeek.SATURDAY -> "Сб"
                    java.time.DayOfWeek.SUNDAY -> "Вс"
                }
                "$dayOfWeek, ${date.dayOfMonth}"
            }
        }
    }

    private fun parseAnyDate(dateString: String?): LocalDate? {
        if (dateString.isNullOrBlank()) return null
        val dateTimeFormats = listOf(
            "yyyy-MM-dd'T'HH:mm:ss[.SSSSSS][.SSS]",
            "yyyy-MM-dd'T'HH:mm:ss"
        )
        for (pattern in dateTimeFormats) {
            try {
                return java.time.LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(pattern)).toLocalDate()
            } catch (ignored: Exception) {}
        }
        return try {
            LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (ignored: Exception) {
            null
        }
    }

}