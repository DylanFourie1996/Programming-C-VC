package com.example.coinquestfinancialxp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.coinquestfinancialxp.ui.screens.*
import ui.screens.RegisterScreen

@Composable
fun Navigation(navController: NavHostController, isDarkTheme : Boolean, onToggleTheme: () -> Unit) {
    NavHost(navController = navController, startDestination = Screen.Login.route) { // CHANGE BACK TO LOGIN.ROUTE
        composable(route=Screen.Login.route+"?email={email}",
            arguments = listOf(
                navArgument("email") {
                    nullable=true
                    defaultValue = null
                }
            )) { backStackEntry ->
            val routeEmail = backStackEntry.arguments?.getString("email")
            LoginScreen(navController, routeEmail=routeEmail, onLoginSuccess = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController, isDarkTheme, onToggleTheme)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, onRegisterSuccess = { email ->
                // If the register is successful, take user to the login page!
                navController.navigate("${Screen.Login.route}?email=$email")
            })
        }
    }
}
