package ViewModels

import DOA.BudgetDao
import Model.BudgetModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CaptureNewBudgetViewModel(private val budgetDao: BudgetDao) : ViewModel() {

    fun insertBudget(userId: Int, limit: Float, save: Float, durationType: Int) {
        val newBudget = BudgetModel(
            userId = userId,
            limit = limit,
            save = save,
            durationType = durationType,
            remainingBalance = limit
        )

        viewModelScope.launch {
            budgetDao.insertBudget(newBudget)
        }
    }

    fun updateRemainingBalance(budgetId: Int, spendAmount: Float) {
        viewModelScope.launch {
            val budget = getBudgetById(budgetId)
            val newRemainingBalance = budget.remainingBalance - spendAmount
            updateBudgetRemainingBalance(budgetId, newRemainingBalance)
        }
    }

    private suspend fun getBudgetById(budgetId: Int): BudgetModel {
        return budgetDao.getBudgetById(budgetId)
    }

    private suspend fun updateBudgetRemainingBalance(budgetId: Int, newBalance: Float) {
        budgetDao.updateRemainingBalance(budgetId, newBalance)
    }
}
