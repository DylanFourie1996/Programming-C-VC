package com.example.coinquestfinancialxp.navigation

import Screens.CategorySpendScreen
import Utils.SessionManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.coinquestfinancialxp.ui.screens.HomeScreen
import com.example.coinquestfinancialxp.ui.screens.LoginScreen
import com.example.coinquestfinancialxp.ui.screens.ProfileScreen
import com.example.coinquestfinancialxp.ui.screens.SettingsScreen
import ui.screens.AchievementScreen
import ui.screens.CaptureCategorySpendScreen
import ui.screens.CaptureNewBudgetScreen
import ui.screens.CategoryCreation
import ui.screens.RegisterScreen
import ui.screens.StatsScreen

@Composable
fun Navigation(navController: NavHostController, isDarkTheme : Boolean, onShowNavbarChanged: (Boolean) -> Unit, onToggleTheme: () -> Unit) { // (Developers et al., 2025)
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context=context) }

    val startDest = if (sessionManager.isLoggedIn()) Screen.Home.route else Screen.Login.route
    NavHost(navController = navController, startDestination = startDest) { // (Developers et al., 2025)
        composable(route=Screen.Login.route+"?email={email}",
            arguments = listOf(
                navArgument("email") {
                    nullable=true
                    defaultValue = null
                }
            )) { backStackEntry ->

            val routeEmail = backStackEntry.arguments?.getString("email")
            onShowNavbarChanged(false)
            LoginScreen(navController, routeEmail=routeEmail, onLoginSuccess = { userID : Int, userEmail : String, userName : String ->

                // Clear previous session
                sessionManager.clearSession()


                // Save new session
                sessionManager.saveUserSession(userID, userEmail, userName)

                if (sessionManager.isLoggedIn()) { // Only direct if logged in!!!
                    println("User is logged in!")
                    println("User ID: $userID")
                    println("User Email: $userEmail")
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            })
        }
        composable(Screen.Home.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            HomeScreen(navController)
        }
        composable(Screen.Profile.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            ProfileScreen(navController)
        }

        composable(Screen.AchievementScreen.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            AchievementScreen(navController)
        }


        composable(Screen.StatsScreen.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            StatsScreen(navController)
        }

        composable(Screen.Settings.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            SettingsScreen(navController, isDarkTheme, onToggleTheme)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController, onRegisterSuccess = { email ->
                // If the register is successful, take user to the login page!
                navController.navigate("${Screen.Login.route}?email=$email")
            })
        }


        composable(Screen.CategorySpendScreen.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            CategorySpendScreen(navController)
        }


        composable(Screen.CaptureCategorySpendScreen.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            CaptureCategorySpendScreen(navController)
        }

        composable(Screen.CaptureNewBudget.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            CaptureNewBudgetScreen(navController)
        }

        composable(Screen.CategoryCreation.route) {
            if (checkSessionAndRedirect(sessionManager, navController)) {
                return@composable
            }
            onShowNavbarChanged(true)
            CategoryCreation(navController)
        }
    }
}

fun checkSessionAndRedirect(sessionManager: SessionManager, navController : NavHostController): Boolean { // (Developers et al., 2025)
    if (!sessionManager.isLoggedIn()) {
        navController.navigate(Screen.Login.route) {
            popUpTo(0) {inclusive=true}
        }

        return true
    }
    return false
}

/* References



Developers. 2025. Design systems in Compose, 16 April 2025 [Online]. Available at: https://developer.android.com/develop/ui/compose/designsystems [Accessed 3 May 2025].

Developers. 2025. Navigation with Compose, 16 April 2025 [Online]. Available at: https://developer.android.com/develop/ui/compose/navigation [Accessed 3 May 2025].
 */