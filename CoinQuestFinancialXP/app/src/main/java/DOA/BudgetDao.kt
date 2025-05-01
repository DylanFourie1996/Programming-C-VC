package DOA

import Model.BudgetModel
import Model.CategorySpendModel
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface BudgetDao {



    //Query for Budget
    @Insert
    suspend fun insertBudget(budget: BudgetModel): Long

    @Query("SELECT * FROM category_spend WHERE budgetId = :budgetId")
    suspend fun getSpendingByBudget(budgetId: Int): List<CategorySpendModel>

    @Insert
    suspend fun insertCategorySpend(spend: CategorySpendModel): Long
}