package ViewModels.Factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinquest.viewmodel.CategorySpendOnlyViewModel
import DOA.CategorySpendOnlyDao
import android.content.Context
import com.example.coinquest.data.DatabaseProvider

class CategorySpendOnlyViewModelFactory(
    private val context : Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategorySpendOnlyViewModel::class.java)) {
            val categorySpendOnlyDao = DatabaseProvider.getDatabase(context).categorySpendOnlyDao()
            return CategorySpendOnlyViewModel(categorySpendOnlyDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
