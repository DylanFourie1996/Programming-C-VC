package ViewModels

import DOA.BudgetDao
import Model.BudgetModel
import Model.CategoryModel
import Utils.SessionManager
import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinquest.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetViewModel(private val budgetDao: BudgetDao, private val sessionManager: SessionManager) : ViewModel() {
    private val _budgets = mutableStateOf<List<BudgetModel>>(emptyList())
    val budget: State<List<BudgetModel>> = _budgets
    val allBudgets : StateFlow<List<BudgetModel>> = budgetDao.getAllBudgets(sessionManager.getUserId())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList<BudgetModel>())

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
