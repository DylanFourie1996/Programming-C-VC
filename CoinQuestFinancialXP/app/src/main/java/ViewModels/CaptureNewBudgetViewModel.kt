package ViewModels

import DOA.BudgetDao
import Model.BudgetModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CaptureNewBudgetViewModel(private val budgetDao: BudgetDao) : ViewModel() { // (Developers et al., 2025)

    companion object {
        const val WEEKLY = 1
        const val BIWEEKLY = 2
        const val MONTHLY = 3
    }

    suspend fun getTotalSpentOnBudget(): Float {
        return budgetDao.getTotalSpentOnBudget(1)
    }

    fun insertUpdatedBudget(userId: Int, limit: Float, save: Float, durationType: Int, toSub : Float, currency: String = "ZAR") {
        if (save > limit) {
            throw IllegalArgumentException("Savings cannot exceed budget limit.")
        }

        var usableAmount = limit - save
        usableAmount -= toSub

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
            val existingBudget = budgetDao.getBudgetById(1)

            if (existingBudget != null)
            {
                val updatedBudget = existingBudget.copy(
                    limit=limit,
                    save=save,
                    durationType=durationType,
                    startDate=System.currentTimeMillis(),
                    remainingBalance=usableAmount,
                    currency = currency
                )

                budgetDao.updateBudget(updatedBudget)
            }
            else{
                budgetDao.insertBudget(newBudget)
            }
            //budgetDao.deleteBudgetByUserId(userId)

        }
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
            budgetDao.deleteBudgetByUserId(userId)
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
            val newRemainingBalance = budget!!.remainingBalance - spendAmount
            updateBudgetRemainingBalance(budgetId, newRemainingBalance)
        }
    }

    suspend fun getBudget(): BudgetModel? {
        return withContext(Dispatchers.IO)
        {
            budgetDao.forceGetBudgetById(1)
        }

    }
    private suspend fun getBudgetById(budgetId: Int): BudgetModel? {
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


    private val _currentBudget = MutableStateFlow<BudgetModel?>(null)
    val currentBudget: StateFlow<BudgetModel?> = _currentBudget


    fun loadCurrentBudget(userId: Int) {
        viewModelScope.launch {
            val budgets = budgetDao.getBudgetsByUserId(userId)
            if (budgets.isNotEmpty()) {
                _currentBudget.value = budgets.last() // or sort by date if needed
            }
        }
    }


        fun resetIfNewPeriod(budgetId: Int) {
        viewModelScope.launch {
            val budget = getBudgetById(budgetId)
            val now = System.currentTimeMillis()

            val periodLength = when (budget!!.durationType) {
                WEEKLY -> 7 * 24 * 60 * 60 * 1000L
                BIWEEKLY -> 14 * 24 * 60 * 60 * 1000L
                MONTHLY -> 30 * 24 * 60 * 60 * 1000L
                else -> 30 * 24 * 60 * 60 * 1000L
            }

            if (now - budget.startDate >= periodLength) {
                val resetBalance = budget!!.limit - budget.save
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

/*
References
Developers. 2025. ViewModel overview, 10 February 2025 [Online]. Available at: https://developer.android.com/topic/libraries/architecture/viewmodel/ [Accessed 3 May 2025].

 */
