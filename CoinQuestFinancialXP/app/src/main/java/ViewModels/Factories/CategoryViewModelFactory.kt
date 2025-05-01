package ViewModels.Factories

import Utils.SessionManager
import ViewModels.CategoryViewModel
import ViewModels.LoginRegisterViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinquest.data.DatabaseProvider

class CategoryViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        val categoryDao = DatabaseProvider.getDatabase(context).CategoryDao()
        val sessionManager = SessionManager.getInstance(context)

        return CategoryViewModel(
            categoryDao,
            sessionManager
        ) as T
    }
}