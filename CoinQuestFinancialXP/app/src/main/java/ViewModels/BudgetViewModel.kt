package ViewModels

import DOA.BudgetDao
import Model.BudgetModel
import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinquest.data.AppDatabase
import kotlinx.coroutines.launch

class BudgetViewModel(private val budgetDao: BudgetDao) : ViewModel() {
    private val _budgets = mutableStateOf<List<BudgetModel>>(emptyList())
    val budget: State<List<BudgetModel>> = _budgets

    fun loadBudgets(userId: Int) {
        viewModelScope.launch {
            try {
                _budgets.value = budgetDao.getBudgetsByUserId(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
