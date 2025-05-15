package com.example.supporthealth.main.domain.api

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.supporthealth.main.domain.models.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("SELECT * FROM `product` WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    @Query("SELECT * FROM `product`")
    suspend fun getAllProducts(): List<ProductEntity>

    @Transaction
    @Query("""
        SELECT p.* FROM `product` p
        INNER JOIN `meal_product` mp ON mp.id_product = p.id
        WHERE mp.id_meal = :mealId
    """)
    suspend fun getProductsByMealId(mealId: Long): List<ProductEntity>
}