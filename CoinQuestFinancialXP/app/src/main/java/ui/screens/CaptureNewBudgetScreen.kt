package ui.screens

import Utils.SessionManager
import ViewModels.CaptureNewBudgetViewModel
import ViewModels.Factories.CaptureNewBudgetViewModelFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.coinquest.data.DatabaseProvider
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureNewBudgetScreen(navController: NavHostController) {
    val customColors = LocalCustomColors.current
    // Context and session management
    val context = LocalContext.current
    val sessionHandler = remember { SessionManager.getInstance(context) }
    val currentUserId = sessionHandler.getUserId()
    val currentUserEmail = sessionHandler.getUserEmail()

    // Initialize ViewModel
    val viewModel: CaptureNewBudgetViewModel = viewModel(
        factory = CaptureNewBudgetViewModelFactory(
            DatabaseProvider.getDatabase(context).budgetDao()
        )
    )

    // State variables
    var limit by remember { mutableStateOf("") }
    var save by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedDuration by remember { mutableStateOf("Monthly") }

    // Duration options and mapping to type codes
    val durationOptions = listOf("Weekly", "Biweekly", "Monthly")
    val durationType = when (selectedDuration) {
        "Weekly" -> 1
        "Biweekly" -> 2
        else -> 3
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // User info display
        /*if (currentUserId != -1 && currentUserEmail != null) {
            Text(color=customColors.TextColor,text="Logged in as: $currentUserEmail (ID: $currentUserId)")
        } else {
            Text(color=customColors.TextColor,text="User not logged in")
        }*/

        Spacer(modifier = Modifier.height(16.dp))

        // Budget limit input
        TextField(
            value = limit,
            onValueChange = { limit = it },
            label = { Text(color=customColors.TextColor,text="Budget Limit (R)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Savings input
        TextField(
            value = save,
            onValueChange = { save = it },
            label = { Text(color=customColors.TextColor,text="Savings (R)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Duration type dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedDuration,
                onValueChange = {},
                readOnly = true,
                label = { Text(color=customColors.TextColor,text="Select Duration") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                durationOptions.forEach { label ->
                    DropdownMenuItem(
                        text = { Text(color=customColors.TextColor,text=label) },
                        onClick = {
                            selectedDuration = label
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(
            onClick = {
                val limitFloat = limit.toFloatOrNull() ?: 0f
                val saveFloat = save.toFloatOrNull() ?: 0f

                if (currentUserId != -1) {
                    viewModel.insertBudget(currentUserId, limitFloat, saveFloat, durationType)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(color=customColors.TextColor,text="Save Budget")
        }
    }
}