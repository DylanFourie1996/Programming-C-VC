package com.example.coinquestfinancialxp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.coinquestfinancialxp.navigation.Screen
import com.example.coinquestfinancialxp.ui.BottomNavBar


// REMEMBER THIS
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview()
{
    val navController = rememberNavController()
    HomeScreen(navController=navController)
}

@Composable
fun HomeScreen(navController: NavController) {
    BackHandler {  }
    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Home Screen")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate(Screen.Profile.route)
            }) {
                Text(text = "Go to Profile")
            }
        }
    }
}


