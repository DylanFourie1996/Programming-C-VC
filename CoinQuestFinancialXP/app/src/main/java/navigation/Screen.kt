package com.example.coinquestfinancialxp.navigation

data class ScreenItem(val route: String, val title: String)

object Screen {
    val Login = ScreenItem("login", "Login")
    val Home = ScreenItem("home", "Home")
    val Profile = ScreenItem("profile", "Profile")
    val Settings = ScreenItem("settings", "Settings")
    val Register = ScreenItem("register", "Register")
    val CategorySpendScreen = ScreenItem(
        route = "CategorySpendScreen?budgetId={budgetId}",
        title = "Expense"
    )
    val CaptureNewBudget = ScreenItem("CaptureNewBudget", "New Budget Entry")
    val CategoryCreation = ScreenItem("CategoryCreation", "Categories")
    val CaptureCategorySpendScreen = ScreenItem("CaptureCategorySpendScreen", "CaptureCategorySpendScreen")
    val StatsScreen = ScreenItem("StatsScreen","Stats")
    val AchievementScreen = ScreenItem("AchievementScreen","AchievementScreen")

}
