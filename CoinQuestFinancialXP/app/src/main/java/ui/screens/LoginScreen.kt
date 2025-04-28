package com.example.coinquestfinancialxp.ui.screens

import ViewModels.Factories.LoginRegisterViewModelFactory
import ViewModels.LoginRegisterViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinquestfinancialxp.navigation.Screen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors


@Composable
fun LoginScreen(navController: NavController, routeEmail : String? = null, onLoginSuccess: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val customColors = LocalCustomColors.current

    val context = LocalContext.current

    val loginRegisterViewModel : LoginRegisterViewModel = viewModel(factory= LoginRegisterViewModelFactory(context))
    println("Route Email: $routeEmail")
    var email by remember { mutableStateOf(routeEmail ?: "") }
    var password by remember {mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Email")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value=email,
            onValueChange={email=it},
            label={Text("Enter your email!")},
            modifier=Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Password")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value=password,
            onValueChange={password=it},
            label={Text("Enter your password!")},
            modifier=Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            // Grab LoginViewModel
            loginRegisterViewModel.login(email, password) { success, userExists, passwordMatches ->
                if (success) {
                    print("Logged in!")
                    onLoginSuccess()
                } else {
                    // To-Do: Let the user know why the login failed.

                    if (userExists) {
                        if (passwordMatches) {
                            println("Uknown error when logging in.")
                        } else {
                            println("Password does not match!")
                        }
                    } else {
                        println("Account does not exist!")
                    }
                    println("Login Failed.")
                }
            }
        }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Don't have an account? Register here.",
            fontSize=if (!isPressed) 10.sp else 9.sp,
            color = if (!isPressed) customColors.hyperlinkDefault else customColors.hyperlinkHover,
            modifier = Modifier.clickable(
                interactionSource=interactionSource,
                indication = null)
            {
                navController.navigate(Screen.Register.route)
            }
        )
    }
}

