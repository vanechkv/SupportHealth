package com.example.supporthealth.nutrition.product.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.supporthealth.main.domain.models.MealType
import com.example.supporthealth.nutrition.main.domain.api.interactor.NutritionInteractor
import com.example.supporthealth.nutrition.main.domain.models.Meal
import com.example.supporthealth.nutrition.search.domain.api.interactor.ProductInteractor
import com.example.supporthealth.nutrition.search.domain.models.Product
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productInteractor: ProductInteractor,
    private val nutritionInteractor: NutritionInteractor
) : ViewModel() {

    private val productValueLiveData = MutableLiveData<Product>()
    fun observeProductValue(): LiveData<Product> = productValueLiveData

    private val baseProduct = productInteractor.getProduct()

    fun updateGrams(grams: Int) {
        val factor = grams / 100f
        productValueLiveData.value = Product(
            productId = baseProduct.productId,
            name = baseProduct.name,
            calories = (baseProduct.calories * factor).toInt(),
            protein = baseProduct.protein * factor,
            fat = baseProduct.fat * factor,
            carbs = baseProduct.carbs * factor
        )
    }

    fun getName(): String = baseProduct.name

    fun addProduct(date: String, mealType: MealType, weight: Float) {
        viewModelScope.launch {
            var nutrition = nutritionInteractor.getNutritionData(date)
            if (nutrition == null) {
                nutritionInteractor.insertNutritionData(date)
                nutrition = nutritionInteractor.getNutritionData(date)
            }
            var productData = nutritionInteractor.getProductByProductId(baseProduct.productId)
            if (productData == null) {
                nutritionInteractor.insertProduct(baseProduct)
                productData = nutritionInteractor.getProductByProductId(baseProduct.productId)
            }
            val mealId = nutritionInteractor.getMealId(nutrition!!.id, mealType)
            nutritionInteractor.addProductToMeal(mealId, productData!!.id, weight)
            nutritionInteractor.updateMeal(date, mealType)
        }
    }
}