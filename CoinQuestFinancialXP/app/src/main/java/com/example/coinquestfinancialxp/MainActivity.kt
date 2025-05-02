package com.example.coinquestfinancialxp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.coinquestfinancialxp.navigation.Navigation
import com.example.coinquestfinancialxp.navigation.Navigation
import com.example.coinquestfinancialxp.ui.BottomNavBar
import com.example.coinquestfinancialxp.ui.TopNavBar
import com.example.coinquestfinancialxp.ui.theme.CoinQuestFinancialXPTheme
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            /*

    var colorScheme = if (isDarkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }*/
            var showNavBar by rememberSaveable { mutableStateOf(true) }
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            CoinQuestFinancialXPTheme(darkTheme = isDarkTheme) {

                MainScreen(isDarkTheme=isDarkTheme, showNavBar=showNavBar, onShowNavBarChanged={showNavBar=it}, onToggleTheme={isDarkTheme=!isDarkTheme})
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(isDarkTheme : Boolean, showNavBar : Boolean, onShowNavBarChanged: (Boolean) -> Unit, onToggleTheme: () -> Unit) {
    val navController = rememberNavController()
    val customColors = LocalCustomColors.current
    Scaffold(
        modifier= Modifier.background(customColors.page),
        topBar = {
            if (showNavBar) TopNavBar(navController)
        },
        bottomBar = {
            Box(modifier=Modifier.background(customColors.page)) {
                if (showNavBar) BottomNavBar(navController)
            }
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues).background(customColors.page)) {
                Navigation(navController = navController, isDarkTheme, onShowNavBarChanged, onToggleTheme)
            }
        }
    )
}