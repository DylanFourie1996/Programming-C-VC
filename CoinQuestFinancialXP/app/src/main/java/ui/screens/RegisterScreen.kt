package ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.coinquestfinancialxp.navigation.Screen
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors


@Composable
fun RegisterScreen(navController: NavController, onRegisterSuccess: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val customColors = LocalCustomColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Register")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRegisterSuccess) {
            Text("Register")
        }
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