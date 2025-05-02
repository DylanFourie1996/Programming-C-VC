package ui.screens

import Model.CategoryModel
import Model.CategorySpendModel
import Utils.SessionManager
import ViewModels.CategoryViewModel
import ViewModels.Factories.CategorySpendViewModelFactory
import ViewModels.Factories.CategoryViewModelFactory
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
import com.example.coinquest.viewmodel.CategorySpendViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.CustomComposables.StandardTextBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    categories: List<CategoryModel>,
    selectedCategory: String?,
    onCategorySelected: (CategoryModel) -> Unit
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
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand category list"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category : CategoryModel ->
                DropdownMenuItem(
                    text = { Text(category.title) },
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
            categorySpendDao = DatabaseProvider.getDatabase(context).categorySpendDao(),
            budgetDao = DatabaseProvider.getDatabase(context).budgetDao()
        )
    )

    var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }
    var spendTitle by remember { mutableStateOf("") }
    var spend by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf("") }

    var categoryViewModel : CategoryViewModel = viewModel(factory= CategoryViewModelFactory(context))
    val categories by categoryViewModel.allCategories.collectAsState()

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoUri = uri?.toString() ?: ""
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Display user details if logged in
        if (currentUserId != -1 && currentUserEmail != null) {
            Text("Logged in as: $currentUserEmail (ID: $currentUserId)")
        } else {
            Text("User not logged in")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Category Dropdown
        CategoryDropdown(
            categories = categories,
            selectedCategory = selectedCategory?.title,
            onCategorySelected = { selectedCategory = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Spend input field
        StandardTextBox(
            value = spendTitle,
            onValueChange = { spendTitle = it },
            placeholder="Expense Title",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )

        // Spend input field
        StandardTextBox(
            value = spend,
            onValueChange = { spend = it },
            placeholder="Spend (R)",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Upload Image button
        Button(onClick = { imageLauncher.launch("image/*") }) {
            Text("Upload Image")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display selected image URI
        Text("Selected Image URI: $photoUri")

        Spacer(modifier = Modifier.height(16.dp))

        // Save Spend button
        Button(
            onClick = {
                // Basic validation
                val spendValue = spend.toFloatOrNull()
                val categoryIndex = categories.indexOf(selectedCategory)

                if (selectedCategory == null || spendValue == null || categoryIndex == -1 || currentUserId == -1) {
                    Toast.makeText(context, "Please enter valid spend and select a category.", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                // Create CategorySpendModel
                val model = CategorySpendModel(
                    budgetId = currentUserId,
                    ItemName = spendTitle,
                    category = categoryIndex,
                    spend = spendValue,
                    photoUri = photoUri
                )

                // Insert and update budget in the database
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        viewModel.insertSpendAndUpdateBudget(model) {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(context, "Spend saved successfully!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()  // Go back after saving
                            }
                        }
                    } catch (e: Exception) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save Spend")
        }
    }
}
