package com.example.coinquestfinancialxp.ui.screens

import Utils.SessionManager
import ViewModels.Factories.LoginRegisterViewModelFactory
import ViewModels.LoginRegisterViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coinquestfinancialxp.R
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors
import ui.CustomComposables.StandardButton
import ui.CustomComposables.StandardButtonTheme
import ui.CustomComposables.StandardTextBox

@Composable
fun LoginScreen(navController: NavController, routeEmail : String? = null, onLoginSuccess: (userID : Int, userEmail : String) -> Unit) {
    BackHandler {  }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val customColors = LocalCustomColors.current

    val context = LocalContext.current

    var showEmailError by remember { mutableStateOf(false) }

    var showPasswordError by remember {mutableStateOf(false)}

    val loginRegisterViewModel : LoginRegisterViewModel = viewModel(factory= LoginRegisterViewModelFactory(context))
    println("Route Email: $routeEmail")
    var email by remember { mutableStateOf(routeEmail ?: "") }
    var password by remember {mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier=Modifier.fillMaxWidth().padding(bottom=50.dp), contentAlignment=Alignment.Center) {
            Text("Login")
        }
        Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement= Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.coinquest),
                contentDescription = "App Logo",
                modifier = Modifier.size(150.dp).padding(vertical = 16.dp).fillMaxWidth()
            )
        }
        Column(modifier=Modifier.fillMaxWidth().weight(1.0f).padding(16.dp), horizontalAlignment=Alignment.CenterHorizontally, verticalArrangement=Arrangement.Bottom) {
            Spacer(modifier = Modifier.height(16.dp))
            StandardTextBox(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier=Modifier.height(3.dp))
            if (showEmailError) Text(text="Email does not exist.", fontSize=12.sp, modifier=Modifier.fillMaxWidth(), color=Color(0.7f, 0f, 0f, 1f))
            Spacer(modifier = Modifier.height(16.dp))
            StandardTextBox(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier=Modifier.height(3.dp))
            if (showPasswordError)Text(text="Password entered is incorrect.", fontSize=12.sp, modifier=Modifier.fillMaxWidth(), color=Color(0.7f, 0f, 0f, 1f))
            Spacer(modifier = Modifier.height(24.dp))
            StandardButton(
                text = "Login",
                onClick = {
                    // Grab LoginViewModel
                    loginRegisterViewModel.login(
                        email,
                        password
                    ) { success, userExists, passwordMatches, userID, message ->
                        println(message)
                        showEmailError = false
                        showPasswordError = false
                        if (success) {
                            println("Logged in!")
                            onLoginSuccess(userID, email)
                        } else {


                            if (userExists) {
                                if (passwordMatches) {
                                    println("Uknown error when logging in.")
                                } else {
                                    println("Password does not match!")

                                    showPasswordError = true

                                }
                            } else {
                                println("Account does not exist!")
                                showEmailError = true
                            }
                            println("Login Failed.")
                        }
                    }
                }
            )
            Spacer(modifier=Modifier.height(16.dp))
            StandardButton(
                themeType= StandardButtonTheme.BLACK,
                text = "Register",
                onClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Row() {
                Text("Don't have an account? ",
                    fontSize=10.sp,
                    color=customColors.hyperlinkInactive)
                Text(
                    "Sign Up",
                    fontSize = if (!isPressed) 10.sp else 9.sp,
                    color = if (!isPressed) customColors.hyperlinkDefault else customColors.hyperlinkHover,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    )
                    {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            Spacer(modifier=Modifier.height(32.dp))
        }
    }
}
