package ViewModels.Factories

import DOA.BudgetDao
import Utils.SessionManager
import ViewModels.BudgetViewModel
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinquest.data.DatabaseProvider

class BudgetViewModelFactory(private val context : Context) : ViewModelProvider.Factory { // (Developers et al., 2025)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BudgetViewModel::class.java)) {
            val sessionManager = SessionManager.getInstance(context);
            val budgetDao = DatabaseProvider.getDatabase(context).budgetDao()
            return BudgetViewModel(budgetDao, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
/*
References


Developers. 2025. Create ViewModels with dependencies , 10 February 2025. [Online]. Available at: https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories [Accessed 3 May 2025].
 */