package DOA

import Model.BudgetModel
import Model.CategorySpendModel
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {

    // Budget operations
    @Query("SELECT * FROM budget WHERE userId = :userId")
    suspend fun getBudgetsByUserId(userId: Int): List<BudgetModel>

    @Query("SELECT * FROM budget WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: BudgetModel): Long

    @Query("UPDATE budget SET remainingBalance = :newBalance WHERE id = :budgetId")
    suspend fun updateRemainingBalance(budgetId: Int, newBalance: Float)



    @Query("SELECT * FROM budget WHERE userId=:userId")
    fun getAllBudgets(userId : Int) : Flow<List<BudgetModel>>

    @Query("DELETE FROM budget WHERE userId = :userId")
    suspend fun deleteBudgetByUserId(userId: Int)


}
