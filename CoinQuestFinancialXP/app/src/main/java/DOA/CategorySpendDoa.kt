package DOA

import Model.CategoryModel
import Model.CategorySpendModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategorySpendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategorySpend(categorySpend: CategorySpendModel): Long

    @Query("SELECT * FROM categoryspend WHERE budgetId = :budgetId")
    fun getSpendsForBudgetFlow(budgetId: Int): Flow<List<CategorySpendModel>>

    @Update
    suspend fun updateCategorySpend(categorySpend: CategorySpendModel)

    @Query("DELETE FROM categoryspend WHERE id = :id")
    suspend fun deleteCategorySpendById(id: Int)

    @Query("SELECT * FROM category WHERE userId=:userId OR userId=null")
    fun getAllCategories(userId: Int): Flow<List<CategoryModel>>
}