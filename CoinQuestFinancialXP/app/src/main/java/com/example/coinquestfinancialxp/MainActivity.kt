package com.example.coinquestfinancialxp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.coinquestfinancialxp.navigation.Navigation
import com.example.coinquestfinancialxp.navigation.Navigation
import com.example.coinquestfinancialxp.ui.theme.CoinQuestFinancialXPTheme

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
            var isDarkTheme by rememberSaveable { mutableStateOf(false) }
            CoinQuestFinancialXPTheme(darkTheme = isDarkTheme) {

                MainScreen(isDarkTheme, onToggleTheme={isDarkTheme=!isDarkTheme})
            }
        }
    }
}

@Composable
fun MainScreen(isDarkTheme : Boolean, onToggleTheme: () -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {  },
        bottomBar = {

            Navigation(navController = navController, isDarkTheme, onToggleTheme)
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                Navigation(navController = navController, isDarkTheme, onToggleTheme)
            }
        }
    )
}
