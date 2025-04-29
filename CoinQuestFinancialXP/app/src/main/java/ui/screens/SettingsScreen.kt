package com.example.coinquestfinancialxp.ui.screens

import  androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.coinquestfinancialxp.navigation.Screen
import com.example.coinquestfinancialxp.ui.BottomNavBar
import ui.CustomComposables.StandardButton
import ui.CustomComposables.StandardButtonTheme


@Composable
fun SettingsScreen(navController: NavHostController, isDarkTheme : Boolean, onToggleTheme: () -> Unit) {

    val theme = if (!isDarkTheme) StandardButtonTheme.ORANGEGRAND else StandardButtonTheme.DARKGRAND
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier=Modifier.fillMaxWidth(), contentAlignment=Alignment.Center) {
                Text(text = "Settings")
            }
            Spacer(modifier = Modifier.height(64.dp))
            Column(modifier = Modifier.fillMaxWidth().weight(1.0f)) {
                StandardButton(themeType= theme,
                    text=if (!isDarkTheme) "Light Mode" else "Night Mode",
                    onClick = {
                        onToggleTheme()
                    }
                )
            }
            Box(modifier=Modifier.fillMaxWidth(), contentAlignment=Alignment.Center) {
                Text(text="Version 1.0")
            }
        }
    }
}