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
) : ViewModel() { // (Developers et al., 2025)
    fun getCategorySendPairs(userId : Int, budgetId : Int) : Flow<List<CategorySpendPair>>
    {
        return categorySpendDao.getCategorySpendPairs(userId, budgetId)
    }



    fun getSpendsForBudget(budgetId: Int, onResult: (List<CategorySpendModel>) -> Unit) {
        viewModelScope.launch {
            try {
                categorySpendDao.getSpendsForBudgetFlow(budgetId).collect{ entries ->
                    onResult(entries)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(emptyList())
            }
        }
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

    suspend fun getSpendForCategory(categoryId : Int) : Float
    {
        val spend = categorySpendDao.getSpendForCategory(categoryId)
        return spend ?: 0f
    }

    fun getCategoriesForUser(userId: Int): Flow<List<CategoryModel>> {
        return categorySpendDao.getAllCategories(userId)
    }


    fun insertSpend(entry: CategorySpendModel, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                categorySpendDao.insertCategorySpend(entry)
                onDone()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateSpend(entry: CategorySpendModel, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                categorySpendDao.updateCategorySpend(entry)
                onDone()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteSpendById(id: Int, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                categorySpendDao.deleteCategorySpendById(id)
                onDone()
            } catch (e: Exception) {
                e.printStackTrace()
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

/*
References
Developers. 2025. ViewModel overview, 10 February 2025 [Online]. Available at: https://developer.android.com/topic/libraries/architecture/viewmodel/ [Accessed 3 May 2025].

 */