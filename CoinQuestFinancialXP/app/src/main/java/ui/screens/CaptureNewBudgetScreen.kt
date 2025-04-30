package ui.screens

import Utils.SessionManager
import ViewModels.CaptureNewBudgetViewModel
import ViewModels.Factories.CaptureNewBudgetViewModelFactory
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.coinquest.data.DatabaseProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureNewBudgetScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sessionHandler = remember { SessionManager.getInstance(context) }
    val currentUserId = sessionHandler.getUserId()
    val currentUserEmail = sessionHandler.getUserEmail()

    val viewModel: CaptureNewBudgetViewModel = viewModel(
        factory = CaptureNewBudgetViewModelFactory(
            DatabaseProvider.getDatabase(context).BudgetDao()
        )
    )

    var limit by remember { mutableStateOf("") }
    var save by remember { mutableStateOf("") }

    val durationOptions = listOf("Weekly", "Biweekly", "Monthly")
    var expanded by remember { mutableStateOf(false) }
    var selectedDuration by remember { mutableStateOf("Monthly") }

    val durationType = when (selectedDuration) {
        "Weekly" -> 1
        "Biweekly" -> 2
        else -> 3
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (currentUserId != -1 && currentUserEmail != null) {
            Text("Logged in as: $currentUserEmail (ID: $currentUserId)")
        } else {
            Text("User not logged in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = limit,
            onValueChange = { limit = it },
            label = { Text("Budget Limit (R)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = save,
            onValueChange = { save = it },
            label = { Text("Savings (R)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown for Duration Type
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedDuration,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Duration") },
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
                        text = { Text(label) },
                        onClick = {
                            selectedDuration = label
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
            Text("Save Budget")
        }
    }
}
