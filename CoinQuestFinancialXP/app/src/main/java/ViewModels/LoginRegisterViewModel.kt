package ViewModels

import Model.AchievementModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinquest.data.DatabaseProvider
import com.example.coinquest.data.UserDao
import com.example.coinquest.data.UserModel
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LoginRegisterViewModel(private val userDao : UserDao) : ViewModel() { // (Developers et al., 2025)
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()


    fun login(email: String, password: String, onResult: (Boolean, Boolean, Boolean, Int, String, String?) -> Unit,) {
        viewModelScope.launch {
            try {
//                val achievementDoa: AchievementDoa

                val user = userDao.getUserByEmail(email)
                val userExists = user != null
                var passwordMatches = false

                if (userExists) {
                    // (JavaDoc, 2017)
                    passwordMatches = BCrypt.checkpw(password, user!!.password)
                }

                val message = when {
                    !userExists -> "Account does not exist"
                    !passwordMatches -> "Incorrect password"
                    else -> null
                }

                val success = userExists && passwordMatches
                println("Login Attempt: Email=$email, Password=$password; User Exists?: ${user != null}")
                //This block will set achievement as one:first login
                if (success) {
//                    val firstLogin = achievementDoa.isAchievementUnlocked(user.id, 1) == false
//                    if (firstLogin) {
//
//                        val achievementOne = AchievementModel(
//                            userId = user.id,
//                            achievementOneId = 1 // Inserting 1 is the ID for the "First Login" achievement
//                        )
//                        achievementDoa.insertUserAchievement(achievementOne)
//                        println("First Login Achievement Awarded to User ID: ${user.id}")
//                    }

                    onResult(
                        success,
                        userExists,
                        passwordMatches,
                        user!!.id,
                        user.name,
                        message
                    )
                } else {
                    onResult(
                        success,
                        userExists,
                        passwordMatches,
                        user!!.id,
                        user.name,
                        message
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false, false, false, -1, "null", "error")
            }
        }
    }

    fun register(name: String, email: String, password: String, onResult: (Boolean, Boolean, Boolean, Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val userExists = userDao.getUserByEmail(email) != null
                val validName = name.isNotEmpty()
                val validEmail = email.isNotEmpty() && email.matches(emailRegex)
                val validPassword = password.length >= 8
                val success = !userExists && validName && validEmail && validPassword
                var message: String? = null

                when {
                    !validName -> message = "Name cannot be empty"
                    !validEmail -> message = "Invalid email format"
                    !validPassword -> message = "Password must be at least 8 characters"
                    userExists -> message = "User already exists"
                }

                println("Register Attempt: Email=$email, Password=$password; Account will be registered?: ${!userExists}")

                if (success) {
                    //
                    val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
                    val newUser = UserModel(name = name, email = email, password = hashedPassword)
                    userDao.insertUser(newUser)

                    //When Date is Created It will be saved in the DB.The function is for Achievement
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    val readableDate = sdf.format(Date(newUser.dateCreated))
                    println("User registered on: $readableDate")
                }

                onResult(success, userExists, validEmail, validPassword, message)
            } catch (e: Exception) {
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

/*
References
JavaDoc. 2017. Class BCrypt, n.d.. [Online]. Available at: https://www.javadoc.io/doc/org.mindrot/jbcrypt/0.4/org/mindrot/jbcrypt/BCrypt.html [Accessed 3 May 2025].
Developers. 2025. ViewModel overview, 10 February 2025 [Online]. Available at: https://developer.android.com/topic/libraries/architecture/viewmodel/ [Accessed 3 May 2025].

 */