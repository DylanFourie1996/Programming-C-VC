package ViewModels

import DOA.BudgetDao
import Model.BudgetModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CaptureNewBudgetViewModel(private val budgetDao: BudgetDao) : ViewModel() {

    companion object {
        const val WEEKLY = 1
        const val BIWEEKLY = 2
        const val MONTHLY = 3
    }

    fun insertBudget(
        userId: Int,
        limit: Float,
        save: Float,
        durationType: Int,
        currency: String = "ZAR"
    ) {
        if (save > limit) {
            throw IllegalArgumentException("Savings cannot exceed budget limit.")
        }

        val usableAmount = limit - save

        val newBudget = BudgetModel(
            userId = userId,
            limit = limit,
            save = save,
            durationType = durationType,
            startDate = System.currentTimeMillis(),
            remainingBalance = usableAmount,
            currency = currency
        )

        viewModelScope.launch {
            budgetDao.deleteBudgetByUserId(userId)
            budgetDao.insertBudget(newBudget)
        }
    }

    private val _currentBudget = MutableStateFlow<BudgetModel?>(null)
    val currentBudget: StateFlow<BudgetModel?> = _currentBudget


    fun loadCurrentBudget(userId: Int) {
        viewModelScope.launch {
            val budgets = budgetDao.getBudgetsByUserId(userId)
            if (budgets.isNotEmpty()) {
                _currentBudget.value = budgets.last()
            }
        }
    }
}
