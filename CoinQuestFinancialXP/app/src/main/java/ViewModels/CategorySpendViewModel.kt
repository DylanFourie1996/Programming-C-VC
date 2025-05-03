package com.example.coinquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import DOA.CategorySpendDao
import DOA.BudgetDao
import DOA.CategorySpendPair
import Model.BudgetModel
import Model.CategoryModel
import Model.CategorySpendModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategorySpendViewModel(
    private val categorySpendDao: CategorySpendDao,
    private val budgetDao: BudgetDao
) : ViewModel() {
    fun getCategorySendPairs(userId : Int, budgetId : Int) : Flow<List<CategorySpendPair>>
    {
        return categorySpendDao.getCategorySpendPairs(userId, budgetId)
    }


    fun getBudgetById(budgetId: Int, onResult: (BudgetModel?) -> Unit) {
        viewModelScope.launch {
            try {
                val budget = budgetDao.getBudgetById(budgetId)
                onResult(budget)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(null)
            }
        }
    }

    fun insertSpendAndUpdateBudget(entry: CategorySpendModel, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val budget = budgetDao.getBudgetById(entry.budgetId)
                if (budget != null) {
                    val updatedBalance = budget.remainingBalance - entry.spend
                    budgetDao.updateRemainingBalance(entry.budgetId, updatedBalance)
                    categorySpendDao.insertCategorySpend(entry)
                }
                onDone()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}