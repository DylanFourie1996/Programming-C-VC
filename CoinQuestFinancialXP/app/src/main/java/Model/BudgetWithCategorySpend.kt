package Model

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithCategorySpend(
    @Embedded val budget: BudgetModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "budgetId"
    )
    val categorySpendList: List<CategorySpendWithCategory>
)

data class CategorySpendWithCategory(
    @Embedded val categorySpend: CategorySpendModel,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryModel
)
