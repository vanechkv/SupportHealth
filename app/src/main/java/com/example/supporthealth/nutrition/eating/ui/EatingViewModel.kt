package com.example.supporthealth.nutrition.eating.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.main.domain.models.MealWithProducts
import com.example.supporthealth.nutrition.eating.domain.models.ProductWithGrams
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.search.domain.api.interactor.ProductInteractor
import com.example.supporthealth.nutrition.search.domain.models.Product
import kotlinx.coroutines.launch

class EatingViewModel(
    private val mealId: Long,
    private val nutritionInteractor: NutritionInteractor,
    private val productInteractor: ProductInteractor
) : ViewModel() {

    fun observeMealWithProducts(): LiveData<MealWithProducts> {
        return nutritionInteractor.getMealWithProduct(mealId).asLiveData()
    }

    fun observeProductsWithGrams(): LiveData<List<ProductWithGrams>> {
        return nutritionInteractor.getProductsWithGrams(mealId).asLiveData()
    }

    fun onProductClick(product: Product) {
        productInteractor.saveProduct(product)
    }

    fun deleteProduct(product: ProductWithGrams) {
        viewModelScope.launch {
            nutritionInteractor.deleteProductFromMeal(mealId, product.id)
        }
    }
}