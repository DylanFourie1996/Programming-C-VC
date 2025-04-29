package Utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val USER_ID = "user_id"
        private const val USER_EMAIL = "user_email"
    }

    fun saveUserSession(id: Int, email: String) {
        prefs.edit().apply {
            putInt(USER_ID, id)
            putString(USER_EMAIL, email)
            apply()
        }
    }

    fun getUserId(): Int = prefs.getInt(USER_ID, -1)

    fun getUserEmail(): String? = prefs.getString(USER_EMAIL, null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean = getUserId() != -1
}