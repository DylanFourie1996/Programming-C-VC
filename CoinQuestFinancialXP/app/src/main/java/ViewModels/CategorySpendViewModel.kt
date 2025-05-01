package ViewModels

import Model.CategorySpendModel
import DOA.CategorySpendDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CategorySpendViewModel(private val dao: CategorySpendDao) : ViewModel() {

    fun insertCategorySpendAndUpdateBudget(entry: CategorySpendModel) {
        viewModelScope.launch {
            val budget = dao.getBudgetById(entry.budgetId)

            val updatedBalance = (budget?.remainingBalance ?: 0f) - entry.spend

            if (budget != null) {
                dao.updateBudget(budget.copy(remainingBalance = updatedBalance))
            }

            dao.insertCategorySpend(entry)
        }
    }
}
