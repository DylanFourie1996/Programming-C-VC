package ui.screens

import ViewModels.Factories.LoginRegisterViewModelFactory
import ViewModels.LoginRegisterViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
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


    BackHandler {}
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val customColors = LocalCustomColors.current

    val context = LocalContext.current

    val loginRegisterViewModel : LoginRegisterViewModel = viewModel(factory= LoginRegisterViewModelFactory(context))

    val labelFontSize=12.sp

    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember {mutableStateOf(false) }
    var showNameError by remember {mutableStateOf(false)}
    var showAccountError by remember {mutableStateOf(false)}

    var nameErrorMessage by remember { mutableStateOf("") }
    var emailErrorMessage by remember { mutableStateOf("") }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var accountErrorMessage by remember { mutableStateOf("") }

    var email by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}
    var name by remember {mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(customColors.page)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(color=customColors.TextColor, text="Register")
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(1.0f).padding(start=16.dp, end=16.dp, top=32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement= Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Column() {
                Text(color=customColors.TextColor, text="Enter your full name", fontSize=labelFontSize)
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextBox(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "Full Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier=Modifier.height(3.dp))
                if (showNameError) Text(text=nameErrorMessage, fontSize=12.sp, modifier=Modifier.fillMaxWidth(), color=Color(0.7f, 0f, 0f, 1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column() {
                Text(color=customColors.TextColor, text="Enter your email", fontSize=labelFontSize)
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextBox(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier=Modifier.height(3.dp))
                if (showEmailError) Text(text=emailErrorMessage, fontSize=12.sp, modifier=Modifier.fillMaxWidth(), color=Color(0.7f, 0f, 0f, 1f))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Column() {
                Text(color=customColors.TextColor, text="Enter your password", fontSize=labelFontSize)
                Spacer(modifier = Modifier.height(16.dp))
                StandardTextBox(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier=Modifier.height(3.dp))
                if (showPasswordError) Text(text=passwordErrorMessage, fontSize=12.sp, modifier=Modifier.fillMaxWidth(), color=Color(0.7f, 0f, 0f, 1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier=Modifier.height(3.dp))
            if (showAccountError) Text(text=accountErrorMessage, fontSize=12.sp, modifier=Modifier.fillMaxWidth(), color=Color(0.7f, 0f, 0f, 1f))
            Spacer(modifier = Modifier.height(16.dp))
            StandardButton(
                text = "Register",
                onClick = {
                    // Grab RegisterViewModel
                    loginRegisterViewModel.register(
                        name,
                        email,
                        password
                    ) { success, userExists, validEmail, validPassword, message ->
                        var msg = message ?: ""
                        showNameError=  false
                        showEmailError = false
                        showPasswordError = false
                        showAccountError = false
                        print(message)
                        if (success) {
                            println("Register successful!")
                            println("New Account:\nName: $name\nEmail: $email\nPassword: $password")
                            onRegisterSuccess(email) // pass email
                        } else {
                            if (userExists) {
                                println("User already exists.")
                                showEmailError = true
                                emailErrorMessage = "Email is already being used by another account!"
                            }
                            if (!validEmail) {
                                println("Email does not meet the requirements.")
                                showEmailError = true
                                emailErrorMessage = "Email is not valid"
                            }
                            if (!validPassword) {
                                println("Password does not meet the requirements")
                                showPasswordError = true
                                passwordErrorMessage = if (password.length < 8)
                                    "Password needs to be at least 8 characters long!"
                                else
                                    "Password is not valid"
                            }

                            if (name.isEmpty()) {
                                showNameError = true
                                nameErrorMessage = "Please enter a name!"
                            }
                            println("Register failed.")
                            // To-Do: Let the user know why the registration failed:
                        }
                    }
                })
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Row() {
                Text(text="Already have an account? ",
                    fontSize=10.sp,
                    color=customColors.hyperlinkInactive)
                Text(
                    text="Login",
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