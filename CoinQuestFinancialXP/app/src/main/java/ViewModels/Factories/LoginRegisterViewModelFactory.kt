package ViewModels.Factories

import ViewModels.LoginRegisterViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinquest.data.DatabaseProvider

class LoginRegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        val userDao = DatabaseProvider.getDatabase(context).userDao()

        return LoginRegisterViewModel(userDao) as T
    }
}