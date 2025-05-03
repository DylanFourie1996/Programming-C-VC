package ViewModels

import DOA.CategorySpendOnlyDao
import Model.CategorySpendModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class CategorySpendOnlyViewModel(private val categorySpendDao: CategorySpendOnlyDao) : ViewModel() {
    private val _spends = MutableStateFlow<List<CategorySpendModel>>(emptyList())
    val spends: StateFlow<List<CategorySpendModel>> = _spends

    fun getAllSpendsForUser(userId: Int) {
        viewModelScope.launch {
            val userSpends = categorySpendDao.getAllSpendsForUser(userId)
            _spends.value = userSpends
        }
    }

    fun insertSpend(budgetId: Int, itemName: String, category: Int, spend: Float, photoUri: String) {
        val spendEntry = CategorySpendModel(
            id = 0,
            budgetId = budgetId,
            ItemName = itemName,
            category = category,
            spend = spend,
            creationDate = Date(),
            photoUri = photoUri
        )
        viewModelScope.launch {
            categorySpendDao.insert(spendEntry)
            getAllSpendsForUser(budgetId) // Refresh spends
        }
    }

    fun deleteSpend(spendEntry: CategorySpendModel) {
        viewModelScope.launch {
            categorySpendDao.deleteEntry(spendEntry)
            getAllSpendsForUser(spendEntry.budgetId) // Refresh spends
        }
    }
}