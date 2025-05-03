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

class CategoryViewModel(private val categoryDao : CategoryDao, private val userDao : UserDao, sessionManager: SessionManager) : ViewModel() { // (Developers et al., 2025)
    init {
        /* Only on mobile storage version. */
        if (sessionManager.isLoggedIn()) {
            val userId = sessionManager.getUserId()
            viewModelScope.launch {
                if (userDao.getUserById(userId) != null) {
                    insertPremadeCategories(sessionManager.getUserId())
                } else
                {
                    println("User id is null: ${userId}")
                }
            }
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

    fun getCategoryById(categoryId: Int, onResult: (CategoryModel?) -> Unit)
    {
        viewModelScope.launch {
            var category = categoryDao.getCategoryById(categoryId)
            onResult(category)
        }
    }

    val allCategories : StateFlow<List<CategoryModel>> = categoryDao.getAllCategories(sessionManager.getUserId())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList<CategoryModel>())

}

/*
References
Developers. 2025. ViewModel overview, 10 February 2025 [Online]. Available at: https://developer.android.com/topic/libraries/architecture/viewmodel/ [Accessed 3 May 2025].

 */