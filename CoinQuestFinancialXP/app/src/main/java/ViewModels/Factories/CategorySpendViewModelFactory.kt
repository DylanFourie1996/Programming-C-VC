package ViewModels.Factories

import DOA.CategorySpendDao
import ViewModels.CategorySpendViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CategorySpendViewModelFactory(private val dao: CategorySpendDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategorySpendViewModel(dao) as T
    }
}
