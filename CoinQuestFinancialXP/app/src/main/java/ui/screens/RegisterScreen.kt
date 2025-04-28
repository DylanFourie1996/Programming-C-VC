package ui.screens

import ViewModels.Factories.LoginRegisterViewModelFactory
import ViewModels.LoginRegisterViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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


@Composable
fun RegisterScreen(navController: NavController, onRegisterSuccess: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val customColors = LocalCustomColors.current

    val context = LocalContext.current

    val loginRegisterViewModel : LoginRegisterViewModel = viewModel(factory= LoginRegisterViewModelFactory(context))

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
        Text("Register")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Name")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value=name,
            onValueChange={name=it},
            label={Text("Enter your name!")},
            modifier=Modifier.fillMaxWidth()
        )
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
            // Grab RegisterViewModel
            loginRegisterViewModel.register(name, email, password) { success, userExists, validEmail, validPassword ->
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
        }) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Already have an account? Login here.",
            fontSize=if (!isPressed) 10.sp else 9.sp,
            color = if (!isPressed) customColors.hyperlinkDefault else customColors.hyperlinkHover,
            modifier = Modifier.clickable(
                interactionSource=interactionSource,
                indication = null)
            {
                navController.navigate(Screen.Login.route)
            }
        )
    }
}

/*

Budget1---
Budget2--
Budget3----

 */