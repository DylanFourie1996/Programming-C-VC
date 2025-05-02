package com.example.coinquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import DOA.CategorySpendDao
import DOA.BudgetDao
import Model.CategoryModel
import Model.CategorySpendModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategorySpendViewModel(
    private val categorySpendDao: CategorySpendDao,
    private val budgetDao: BudgetDao
) : ViewModel() {

    fun getSpendsForBudget(budgetId: Int, onResult: (List<CategorySpendModel>) -> Unit) {
        viewModelScope.launch {
            try {
                val entries = categorySpendDao.getSpendsForBudgetFlow(budgetId)
                onResult(entries as List<CategorySpendModel>)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(emptyList())
            }
        }
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
