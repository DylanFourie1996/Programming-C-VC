package ViewModels.Factories

import DOA.BudgetDao
import ViewModels.CaptureNewBudgetViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CaptureNewBudgetViewModelFactory(private val budgetDao: BudgetDao) : ViewModelProvider.Factory { // (Developers et al., 2025)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CaptureNewBudgetViewModel::class.java)) {
            return CaptureNewBudgetViewModel(budgetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

/*
References


Developers. 2025. Create ViewModels with dependencies , 10 February 2025. [Online]. Available at: https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories [Accessed 3 May 2025].
 */