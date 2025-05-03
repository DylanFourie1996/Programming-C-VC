package ViewModels.Factories

import ViewModels.LoginRegisterViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinquest.data.DatabaseProvider


class LoginRegisterViewModelFactory(private val context: Context) : ViewModelProvider.Factory { // (Developers et al., 2025)
    override fun <T : ViewModel> create(modelClass : Class<T>) : T {
        val userDao = DatabaseProvider.getDatabase(context).userDao()

        return LoginRegisterViewModel(userDao) as T
    }
}
/*
References


Developers. 2025. Create ViewModels with dependencies , 10 February 2025. [Online]. Available at: https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-factories [Accessed 3 May 2025].

 */