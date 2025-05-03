package com.example.coinquest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import DOA.CategorySpendOnlyDao
import Model.CategorySpendModel
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategorySpendOnlyViewModel(
    private val categorySpendOnlyDao: CategorySpendOnlyDao
) : ViewModel() {

    fun getAllUserEntries(userId: Int, onResult: (List<CategorySpendModel>) -> Unit) {
        viewModelScope.launch {
            try {
                val entries = categorySpendOnlyDao.getAllSpendsForUser(userId)
                onResult(entries)
            } catch (e: Exception) {
                onResult(emptyList())
            }
        }
    }

    fun getEntryById(entryId: Int, onResult: (CategorySpendModel?) -> Unit) {
        viewModelScope.launch {
            val result = categorySpendOnlyDao.getEntryById(entryId)
            onResult(result)
        }
    }
    fun insertEntry(entry: CategorySpendModel, onComplete: () -> Unit = {}) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dao = null
                val insert = categorySpendOnlyDao.insert(entry)
                withContext(Dispatchers.Main) {
                    onComplete()
                }
            } catch (e: Exception) {
                Log.e("CategorySpendViewModel", "Insert error: ${e.message}")
            }
        }
    }

    fun updateEntry(entry: CategorySpendModel, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                categorySpendOnlyDao.updateEntry(entry)
                onDone()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteEntry(entry: CategorySpendModel, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                categorySpendOnlyDao.deleteEntry(entry)
                onDone()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
