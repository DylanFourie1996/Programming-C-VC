package com.example.coinquestfinancialxp.ui.screens

import Utils.SessionManager
import ViewModels.Factories.LoginRegisterViewModelFactory
import ViewModels.LoginRegisterViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.coinquestfinancialxp.R
import com.example.coinquestfinancialxp.navigation.Navigation
import com.example.coinquestfinancialxp.navigation.Screen
import com.example.coinquestfinancialxp.ui.BottomNavBar
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors
import ui.CustomComposables.StandardButton
import ui.CustomComposables.StandardButtonTheme

@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    val customColors = LocalCustomColors.current

    val loginRegisterViewModel : LoginRegisterViewModel = viewModel(factory= LoginRegisterViewModelFactory(context))

    // Logout session
    val sessionHandler = SessionManager.getInstance(context)

    val username = sessionHandler.getUserName() ?: "NULL"
    val email = sessionHandler.getUserEmail() ?: "NULL"

    val showDeleteDialog = remember {mutableStateOf(false)}

    BackHandler {  }
    Scaffold(
        containerColor=customColors.page,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal=16.dp).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box() {
                Text(color=customColors.TextColor,text = "Profile Screen")
            }
            Spacer(modifier = Modifier.height(32.dp))
            Image(painter=painterResource(R.drawable.profileicon), contentDescription=null, modifier=Modifier.size(48.dp))
            Spacer(modifier=Modifier.height(16.dp))
            /*LinearProgressIndicator(
                progress = 0.5f,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Blue,
                trackColor = Color.Red,
                strokeCap = Color.Black
            )*/
            Box(modifier=Modifier.fillMaxWidth()) {
                Column(horizontalAlignment=Alignment.CenterHorizontally, modifier=Modifier.align(Alignment.Center)) {
                    Text(color=customColors.TextColor,text="2/6", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = 0.33f,
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.0f)).border(
                                width = 0.1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(100.0f)
                            ).width(75.dp),
                        color = customColors.ProgressBarColor,

                        )
                }

                Spacer(modifier = Modifier.width(32.dp))
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0.95f, 0.75f, 0.0f, 1.0f),
                    modifier = Modifier.align(Alignment.Center).offset(x=60.dp).size(24.dp).clickable {
                        navController.navigate(Screen.AchievementScreen.route)
                    }
                )

            }
            Spacer(modifier=Modifier.height(32.dp))
            Column(modifier=Modifier.fillMaxWidth(), horizontalAlignment=Alignment.CenterHorizontally) {
                Divider(color=customColors.DividerColor1)
                Text(color=customColors.TextColor,text=username, modifier=Modifier.padding(vertical=16.dp), fontSize=12.sp)
                Divider(color=customColors.DividerColor2)
                Text(color=customColors.TextColor,text=email, modifier=Modifier.padding(vertical=16.dp), fontSize=12.sp)
                Divider(color=customColors.DividerColor2)
            }
            Spacer(modifier=Modifier.height(64.dp))
            Column(modifier=Modifier.padding(horizontal=32.dp).padding(bottom=32.dp)) {
                StandardButton(text = "Log Out", onClick = {


                    sessionHandler.clearSession()

                    // Force renavigation
                    navController.navigate(Screen.Login.route)
                })
                Spacer(modifier=Modifier.height(16.dp))
                StandardButton(text = "Delete Account", themeType= StandardButtonTheme.BLACK, onClick = {
                    showDeleteDialog.value = true

                })


            }
        }
    }

    if (showDeleteDialog.value) {
        AlertDialog(
            modifier=Modifier.padding(vertical=16.dp),
            onDismissRequest = {showDeleteDialog.value = false},
            title = {Text(color=customColors.TextColor,text="Delete Account")},
            text = {Text(color=customColors.TextColor,text="Are you sure you want to delete your account? This action cannot be undone.")},
            buttons = {
                Column(modifier=Modifier.fillMaxWidth().padding(16.dp)) {
                    StandardButton(
                        modifier=Modifier.padding(horizontal=16.dp),
                        text = "Confirm",
                        themeType = StandardButtonTheme.BLACK,
                        onClick = {
                            showDeleteDialog.value = false

                            // Delete account
                            val email = sessionHandler.getUserEmail()

                            if (email != null) {
                                loginRegisterViewModel.delete(sessionHandler.getUserId(), email) {
                                    sessionHandler.clearSession()

                                    // Force renavigation
                                    navController.navigate(Screen.Login.route)
                                }
                            }
                        })
                    Spacer(modifier=Modifier.height(16.dp))
                    StandardButton(modifier=Modifier.padding(start=16.dp, end=16.dp, bottom=32.dp), text = "Cancel", onClick = { showDeleteDialog.value = false })
                }
            },
        )
    }
}