package ui.screens

import ViewModels.Factories.LoginRegisterViewModelFactory
import ViewModels.LoginRegisterViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.coinquestfinancialxp.navigation.Screen
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors
import ui.CustomComposables.StandardButton
import ui.CustomComposables.StandardTextBox


@Composable
fun RegisterScreen(navController: NavController, onRegisterSuccess: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val customColors = LocalCustomColors.current

    val context = LocalContext.current

    val loginRegisterViewModel : LoginRegisterViewModel = viewModel(factory= LoginRegisterViewModelFactory(context))

    val labelFontSize=12.sp

    var email by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}
    var name by remember {mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Register")
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(1.0f).padding(start=16.dp, end=16.dp, top=32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement= Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column() {
                Text("Enter your full name", fontSize=labelFontSize)
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextBox(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Full Name",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column() {
                Text("Enter your email", fontSize=labelFontSize)
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextBox(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Column() {
                Text("Enter your password", fontSize=labelFontSize)
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextBox(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            StandardButton(
                text = "Register",
                onClick = {
                    // Grab RegisterViewModel
                    loginRegisterViewModel.register(
                        name,
                        email,
                        password
                    ) { success, userExists, validEmail, validPassword ->
                        if (success) {
                            println("Register successful!")
                            println("New Account:\nName: $name\nEmail: $email\nPassword: $password")
                            onRegisterSuccess(email) // pass email
                        } else {
                            if (userExists) {
                                println("User already exists.")
                            }
                            if (!validEmail) {
                                println("Email does not meet the requirements.")
                            }
                            if (!validPassword) {
                                println("Password does not meet the requirements.")
                            }
                            println("Register failed.")
                            // To-Do: Let the user know why the registration failed:
                        }
                    }
                })
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Row() {
                Text("Already have an account? ",
                    fontSize=10.sp,
                    color=customColors.hyperlinkInactive)
                Text(
                    "Login",
                    fontSize = if (!isPressed) 10.sp else 9.sp,
                    color = if (!isPressed) customColors.hyperlinkDefault else customColors.hyperlinkHover,
                    modifier = Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    )
                    {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
            Spacer(modifier=Modifier.height(32.dp))
        }
    }
}

/*

Budget1---
Budget2--
Budget3----

 */