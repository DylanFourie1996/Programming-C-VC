package DOA

import Model.BudgetModel
import Model.CategorySpendModel
import androidx.room.*

@Dao
interface BudgetDao {

    // Budget operations
    @Query("SELECT * FROM budget WHERE userId = :userId")
    suspend fun getBudgetsByUserId(userId: Int): List<BudgetModel>

    @Query("SELECT * FROM budget WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetModel): Long

    @Update
    suspend fun updateBudget(budget: BudgetModel)

    @Query("UPDATE budget SET remainingBalance = :newBalance WHERE id = :budgetId")
    suspend fun updateRemainingBalance(budgetId: Int, newBalance: Float)

    // CategorySpend operations (Should likely go into a different DAO)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategorySpend(spend: CategorySpendModel): Long

    @Update
    suspend fun updateCategorySpend(entry: CategorySpendModel)

    @Query("SELECT * FROM categoryspend WHERE budgetId = :budgetId")
    suspend fun getSpendsForBudget(budgetId: Int): List<CategorySpendModel>

    @Query("DELETE FROM categoryspend WHERE id = :id")
    suspend fun deleteCategorySpendById(id: Int)

    @Query("SELECT * FROM budget WHERE userId = :userId ORDER BY startDate DESC LIMIT 1")
    suspend fun getLatestBudgetForUser(userId: Int): BudgetModel?

}
