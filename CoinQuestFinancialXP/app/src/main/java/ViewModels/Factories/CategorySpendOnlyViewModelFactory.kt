package ViewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinquest.viewmodel.CategorySpendOnlyViewModel
import DOA.CategorySpendOnlyDao

class CategorySpendOnlyViewModelFactory(
    private val categorySpendOnlyDao: CategorySpendOnlyDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategorySpendOnlyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategorySpendOnlyViewModel(categorySpendOnlyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
