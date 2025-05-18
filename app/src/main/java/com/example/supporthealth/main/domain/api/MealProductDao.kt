package com.example.supporthealth.main.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.supporthealth.main.domain.models.MealProductCrossRef

@Dao
interface MealProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealProductCrossRef(crossRef: MealProductCrossRef)

    @Delete
    suspend fun deleteMealProductCrossRef(crossRef: MealProductCrossRef)

    @Update
    suspend fun updateMealProductCrossRef(crossRef: MealProductCrossRef)

    @Query("""
        SELECT * FROM `meal_product`
        WHERE id_meal = :mealId
    """)
    suspend fun getCrossRefsByMealId(mealId: Long): List<MealProductCrossRef>

    @Query("""
        SELECT * FROM `meal_product`
        WHERE id_meal = :mealId AND id_product = :productId
        LIMIT 1
    """)
    suspend fun getCrossRef(mealId: Long, productId: Long): MealProductCrossRef?

    @Transaction
    suspend fun insertOrUpdateMealProduct(mealId: Long, productId: Long, gramsToAdd: Float) {
        val existing = getCrossRef(mealId, productId)
        if (existing != null) {
            val updated = existing.copy(
                grams = (gramsToAdd).coerceAtLeast(0f)
            )
            updateMealProductCrossRef(updated)
        } else {
            val newRef = MealProductCrossRef(
                mealId = mealId,
                productId = productId,
                grams = gramsToAdd.coerceAtLeast(0f)
            )
            insertMealProductCrossRef(newRef)
        }
    }
}