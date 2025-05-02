package Model

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithCategorySpend(
    @Embedded val budget: BudgetModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "budgetId"
    )
    val categorySpend: List<CategorySpendModel>
)
