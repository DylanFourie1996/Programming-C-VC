package ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinquest.data.UserDao
import com.example.coinquest.data.UserModel
import kotlinx.coroutines.launch

class LoginRegisterViewModel(private val userDao : UserDao) : ViewModel() {
    fun login(email : String, password : String, onResult: (Boolean, Boolean, Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)

            var userExists = user != null
            var passwordMatches = false
            if (userExists) {
                passwordMatches = user?.password == password
            }

            println("Login Attempt: Email=$email, Password=$password; User Exists?: ${user != null}")
            onResult(userExists && passwordMatches, userExists, passwordMatches) // Pass back if password matches as well, to check if the user is logging in or not.
        }
    }

    fun register(name : String, email : String, password : String, onResult: (Boolean, Boolean, Boolean, Boolean) -> Unit) {
        viewModelScope.launch {
            val userExists = userDao.getUserByEmail(email) != null
            val validEmail = !email.isEmpty()
            val validPassword = !password.isEmpty()
            val validRegistration = !userExists && validEmail && validPassword
            println("Register Attempt: Email=$email, Password=$password; Account will be registered?: ${!userExists}")
            if (validRegistration) { // Since user does not exist, register them.
                // Create new user model data
                val newUser : UserModel = UserModel(
                    name=name,
                    email=email,
                    password=password
                )

                // Insert the new user model into the RoomDB userDAO.
                userDao.insertUser(newUser)
            }

            // Pass back the true if the user did not exist on register, or false if the user already existed.
            onResult(validRegistration, userExists, validEmail, validPassword)
        }
    }
}