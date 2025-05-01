package com.example.coinquest.data

import Model.CategoryModel
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category WHERE userId=:userId OR userId=null")
    fun getAllCategories(userId : Int) : Flow<List<CategoryModel>>

    @Insert(onConflict=REPLACE)
    suspend fun insertCategory(category : CategoryModel)

    @Insert(onConflict=REPLACE)
    suspend fun insertCategories(categoryList : List<CategoryModel>)

    @Query("SELECT title FROM category WHERE userId=:userId AND premade = 1")
    suspend fun getPremadeCategoryIds(userId : Int) : List<String>

    @Delete
    suspend fun deleteCategory(category: CategoryModel)
}
