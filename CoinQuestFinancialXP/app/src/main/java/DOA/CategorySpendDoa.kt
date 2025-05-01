package DOA

import Model.BudgetModel
import Model.CategorySpendModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategorySpendDao {

    // Insert a new category spend
    @Insert
    suspend fun insertCategorySpend(entry: CategorySpendModel)

    // Fetch a single budget by its ID
    @Query("SELECT * FROM budget WHERE id = :id")
    suspend fun getBudgetById(id: Int): BudgetModel?

    // Update an existing budget
    @Update
    suspend fun updateBudget(budget: BudgetModel)

    // Fetch all category spends for a given budget
    @Query("SELECT * FROM category_spend WHERE budgetId = :budgetId")
    suspend fun getSpendsForBudget(budgetId: Int): List<CategorySpendModel>
}
