package ViewModels.Factories

import DOA.BudgetDao
import ViewModels.CaptureNewBudgetViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CaptureNewBudgetViewModelFactory(private val budgetDao: BudgetDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaptureNewBudgetViewModel::class.java)) {
            return CaptureNewBudgetViewModel(budgetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
