package com.example.coinquestfinancialxp.navigation

data class ScreenItem(val route: String, val title: String)

object Screen {
    val Login = ScreenItem("login", "Login")
    val Home = ScreenItem("home", "Home")
    val Profile = ScreenItem("profile", "Profile")
    val Settings = ScreenItem("settings", "Settings")
}
