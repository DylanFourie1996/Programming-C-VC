package ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinquest.data.UserDao
import com.example.coinquest.data.UserModel
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class LoginRegisterViewModel(private val userDao : UserDao) : ViewModel() {
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    fun login(email : String, password : String, onResult: (Boolean, Boolean, Boolean, Int, String, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = userDao.getUserByEmail(email)
                var userExists = user != null
                var passwordMatches = false

                if (userExists) {
                    passwordMatches = BCrypt.checkpw(password, user!!.password)
                }

                val message = when {
                    !userExists -> "Account does not exist"
                    !passwordMatches -> "Incorrect password"
                    else -> null
                }

                val success = userExists && passwordMatches

                println("Login Attempt: Email=$email, Password=$password; User Exists?: ${user != null}")  // Debug Statement
                onResult(
                    success,
                    userExists,
                    passwordMatches,
                    user!!.id,
                    user.name,
                    message
                ) // Pass back if password matches as well, to check if the user is logging in or not.
            } catch (e : Exception) {
                e.printStackTrace()
                onResult(false, false, false, -1, "null", "error")
            }
        }
    }

    fun register(name : String, email : String, password : String, onResult: (Boolean, Boolean, Boolean, Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                // Create bool expressions to validate registration
                val userExists = userDao.getUserByEmail(email) != null
                val validName = name.isNotEmpty()
                val validEmail = email.isNotEmpty() && email.matches(emailRegex)
                val validPassword = password.length >= 8

                // bool expr that combines the previous 3 expressions for overall validation
                val success = !userExists && validName && validEmail && validPassword
                var message: String? = null

                when {
                    !validName -> message = "Name cannot be empty"
                    !validEmail -> message = "Invalid email format"
                    !validPassword -> message = "Password must be at least 8 characters"
                    userExists -> message = "User already exists"
                }


                println("Register Attempt: Email=$email, Password=$password; Account will be registered?: ${!userExists}") // Debug Statement
                if (success) {
                    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
                    val newUser = UserModel(name = name, email = email, password = hashedPassword)
                    userDao.insertUser(newUser)
                }

                // Pass back the true if the user did not exist on register, or false if the user already existed.
                onResult(success, userExists, validEmail, validPassword, message)
            } catch (e : Exception) {
                e.printStackTrace()
                onResult(false, false, false, false, "Error")
            }
        }
    }

    fun delete(userID : Int, email : String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val user : UserModel? = userDao.getUserById(userID)

                val userExists = user != null


                if (userExists) {
                    val userCredentialsMatch = userID == user.id && email == user.email

                    if (userCredentialsMatch) {
                        userDao.deleteUserById(userID)
                        onResult(true)
                    }
                }
                onResult(false)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }
}