package DOA

import Model.BudgetModel
import Model.BudgetWithCategorySpend
import Model.CategorySpendModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface BudgetDao {

    // Fetch all budgets with their related category spends for a given user


    // Fetch all budgets for a user
    @Query("SELECT * FROM budget WHERE userId = :userId")
    suspend fun getBudgetsByUserId(userId: Int): List<BudgetModel>

    // Insert a new budget
    @Insert
    suspend fun insertBudget(budget: BudgetModel): Long

    // Fetch all category spends for a given budget
    @Query("SELECT * FROM category_spend WHERE budgetId = :budgetId")
    suspend fun getSpendingByBudget(budgetId: Int): List<CategorySpendModel>

    // Insert a new category spend
    @Insert
    suspend fun insertCategorySpend(spend: CategorySpendModel): Long

    // Fetch a single budget by its ID
    @Query("SELECT * FROM budget WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetModel

    // Update the remaining balance of a budget
    @Query("UPDATE budget SET remainingBalance = :newBalance WHERE id = :budgetId")
    suspend fun updateRemainingBalance(budgetId: Int, newBalance: Float)
}
