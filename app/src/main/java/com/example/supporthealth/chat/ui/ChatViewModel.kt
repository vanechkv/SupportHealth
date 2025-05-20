package com.example.supporthealth.chat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.R
import com.example.supporthealth.chat.domain.api.interactor.ChatInteractor
import com.example.supporthealth.chat.domain.models.ChatMessage
import com.example.supporthealth.chat.domain.models.ChatState
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.search.domain.models.Product
import kotlinx.coroutines.launch
import java.time.LocalDate

class ChatViewModel(
    private val chatInteractor: ChatInteractor,
    private val nutritionInteractor: NutritionInteractor
) : ViewModel() {

    private var message: String? = null

    private val stateLiveData = MutableLiveData<ChatState>()
    fun observeState(): LiveData<ChatState> = stateLiveData

    fun sendMessage(newMessage: String) {
        if (newMessage.isNotEmpty()) {
            renderState(ChatState.Loading)

            chatInteractor.sendMessage(
                newMessage,
                object : ChatInteractor.ChatConsumer {
                    override fun consume(
                        mealSuggestion: ChatMessage.MealSuggestion?,
                        error: String?
                    ) {
                        when {
                            error != null -> {
                                renderState(ChatState.Error(R.string.internet_error))
                            }

                            mealSuggestion != null -> {
                                renderState(ChatState.MealSuggestion(mealSuggestion))
                            }

                            else -> renderState(ChatState.Empty(R.string.search_not_found))
                        }
                    }
                })
        }
    }

    private fun renderState(state: ChatState) {
        stateLiveData.postValue(state)
    }

    fun addMeal(mealSuggestion: ChatMessage.MealSuggestion) {
        viewModelScope.launch {
            var date: String = LocalDate.now().toString()
            if (mealSuggestion.date != null) {
                date = mealSuggestion.date
            }
            var nutrition = nutritionInteractor.getNutritionData(date)
            if (nutrition == null) {
                nutritionInteractor.insertNutritionData(date)
                nutrition = nutritionInteractor.getNutritionData(date)
            }
            mealSuggestion.products.forEach { product ->
                var productData = nutritionInteractor.getProductByProductId(product.productId)
                if (productData == null) {
                    nutritionInteractor.insertProduct(product = Product(
                        productId = product.productId,
                        name = product.name,
                        calories = product.calories,
                        protein = product.protein,
                        fat = product.fat,
                        carbs = product.carbs
                    ))
                    productData = nutritionInteractor.getProductByProductId(product.productId)
                }
                val mealId = nutritionInteractor.getMealId(nutrition!!.id, mealSuggestion.mealType)
                nutritionInteractor.addProductToMeal(mealId, productData!!.id, product.grams)
                nutritionInteractor.updateMeal(date, mealSuggestion.mealType)
            }
        }
    }
}