package ViewModels

import DOA.BudgetDao
import Model.BudgetModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CaptureNewBudgetViewModel(private val budgetDao: BudgetDao) : ViewModel() {

    companion object {
        const val WEEKLY = 1
        const val BIWEEKLY = 2
        const val MONTHLY = 3
    }

    fun insertBudget(userId: Int, limit: Float, save: Float, durationType: Int, currency: String = "ZAR") {
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
            budgetDao.insertBudget(newBudget)
        }
    }

    fun calculateCurrentPeriod(startDate: Long, durationType: Int): Int {
        val now = System.currentTimeMillis()
        val millisInDay = 86400000L
        val daysPassed = (now - startDate) / millisInDay

        return when (durationType) {
            WEEKLY -> (daysPassed / 7).toInt() + 1
            BIWEEKLY -> (daysPassed / 14).toInt() + 1
            MONTHLY -> (daysPassed / 30).toInt() + 1
            else -> 1
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

    fun getPeriodProgress(budget: BudgetModel): Float {
        val now = System.currentTimeMillis()
        val elapsed = now - budget.startDate

        val totalMillis = when (budget.durationType) {
            WEEKLY -> 7 * 24 * 60 * 60 * 1000L
            BIWEEKLY -> 14 * 24 * 60 * 60 * 1000L
            MONTHLY -> 30 * 24 * 60 * 60 * 1000L
            else -> 30 * 24 * 60 * 60 * 1000L
        }

        return (elapsed.toFloat() / totalMillis).coerceIn(0f, 1f)
    }

    fun resetIfNewPeriod(budgetId: Int) {
        viewModelScope.launch {
            val budget = getBudgetById(budgetId)
            val now = System.currentTimeMillis()

            val periodLength = when (budget.durationType) {
                WEEKLY -> 7 * 24 * 60 * 60 * 1000L
                BIWEEKLY -> 14 * 24 * 60 * 60 * 1000L
                MONTHLY -> 30 * 24 * 60 * 60 * 1000L
                else -> 30 * 24 * 60 * 60 * 1000L
            }

            if (now - budget.startDate >= periodLength) {
                val resetBalance = budget.limit - budget.save
                val resetBudget = budget.copy(
                    totalSpent = 0f,
                    remainingBalance = resetBalance,
                    startDate = now
                )
                budgetDao.updateBudget(resetBudget)
            }
        }
    }

    // Optional: For displaying budget info cleanly
    fun getDisplayBudget(budget: BudgetModel): String {
        return "Budget: ${budget.currency} ${budget.limit} | Saving: ${budget.currency} ${budget.save} | Left: ${budget.currency} ${budget.remainingBalance}"
    }
}
