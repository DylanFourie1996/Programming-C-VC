package ViewModels

import Model.CategoryModel
import Utils.SessionManager
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinquest.data.CategoryDao
import com.example.coinquest.data.UserDao
import com.example.coinquest.data.UserModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.stateIn

class CategoryViewModel(private val categoryDao : CategoryDao, sessionManager: SessionManager) : ViewModel() {
    init {
        /* Only on mobile storage version. */
        if (sessionManager.isLoggedIn()) {
            insertPremadeCategories(sessionManager.getUserId())
        }
    }
    fun getPremadeCategories(userId : Int) : List<CategoryModel>
    {
        return listOf(
            CategoryModel(userId=userId, title="Finance", premade=true),
            CategoryModel(userId=userId, title="Health", premade=true),
            CategoryModel(userId=userId, title="Technology", premade=true),
            CategoryModel(userId=userId, title="Entertainment", premade=true),
            CategoryModel(userId=userId, title="Education", premade=true)
        )
    }

    fun insertPremadeCategories(userId : Int) {
        viewModelScope.launch {
            val existing = categoryDao.getPremadeCategoryIds(userId)
            val missing = getPremadeCategories(userId).filter  {cat -> cat.title !in existing}

            if (missing.isNotEmpty())
            {
                categoryDao.insertCategories(missing)
            }

        }
    }

    fun insertNewCategory(category: CategoryModel) {
        viewModelScope.launch {
            categoryDao.insertCategory(category)
        }
    }

    fun deleteCategory(category : CategoryModel) {
        if (category.id in 1..5) return
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }

    val allCategories : StateFlow<List<CategoryModel>> = categoryDao.getAllCategories(sessionManager.getUserId())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList<CategoryModel>())

}