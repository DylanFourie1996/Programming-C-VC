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

    @Query("SELECT spend FROM categoryspend WHERE category=:categoryId")
    suspend fun getSpendForCategory(categoryId : Int) : Float?

    @Query("""
        SELECT c.*, cs.spend
        FROM category c
        LEFT JOIN categoryspend cs ON cs.category = c.id AND cs.budgetId = :budgetId
        WHERE c.userId = :userId OR c.userId IS NULL
    """)
    fun getCategorySpendPairs(userId: Int, budgetId: Int): Flow<List<CategorySpendPair>>


}