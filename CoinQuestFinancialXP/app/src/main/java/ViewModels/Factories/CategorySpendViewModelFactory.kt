package ViewModels.Factories

import DOA.BudgetDao
import DOA.CategorySpendDao
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinquest.viewmodel.CategorySpendViewModel

class CategorySpendViewModelFactory(
    private val categorySpendDao: CategorySpendDao,
    private val budgetDao: BudgetDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategorySpendViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategorySpendViewModel(categorySpendDao, budgetDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
