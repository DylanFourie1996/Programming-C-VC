package ui.screens

import Model.CategorySpendModel
import Utils.SessionManager
import ViewModels.CategorySpendViewModel
import ViewModels.Factories.CategorySpendViewModelFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.coinquest.data.DatabaseProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedCategory ?: "",
            onValueChange = {},
            label = { Text("Category") },
            readOnly = true,
            trailingIcon = {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Expand category list")
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CaptureCategorySpendScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sessionHandler = remember { SessionManager.getInstance(context) }
    val currentUserId = sessionHandler.getUserId()
    val currentUserEmail = sessionHandler.getUserEmail()

    val viewModel: CategorySpendViewModel = viewModel(
        factory = CategorySpendViewModelFactory(
            DatabaseProvider.getDatabase(context).CategorySpendDao()
        )
    )

    var category by remember { mutableStateOf<String?>(null) }
    var spend by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf("") }
    val categories = listOf("Food", "Transport", "Entertainment", "Bills")

    // Image picker
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoUri = uri?.toString() ?: ""
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (currentUserId != -1 && currentUserEmail != null) {
            Text("Logged in as: $currentUserEmail (ID: $currentUserId)")
        } else {
            Text("User not logged in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category
        CategoryDropdown(
            categories = categories,
            selectedCategory = category,
            onCategorySelected = { category = it }
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Spend
        TextField(
            value = spend,
            onValueChange = { spend = it },
            label = { Text("Spend (R)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Upload image
        Button(onClick = { imageLauncher.launch("image/*") }) {
            Text("Upload Image")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text("Selected Image URI: $photoUri")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (category.isNullOrBlank() || spend.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val model = CategorySpendModel(
                        budgetId = currentUserId,
                        category = categories.indexOf(category),
                        spend = spend.toFloatOrNull() ?: 0f,
                        photoUri = photoUri
                    )
                    viewModel.insertCategorySpendAndUpdateBudget(model)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save Spend")
        }
    }
}
