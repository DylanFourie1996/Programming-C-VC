package Screens

import Model.CategorySpendModel
import Utils.SessionManager
import ViewModels.Factories.CategorySpendOnlyViewModelFactory
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.io.File
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import java.io.FileInputStream
import com.example.coinquest.data.DatabaseProvider
import com.example.coinquest.viewmodel.CategorySpendOnlyViewModel
import ui.screens.CategoryDropdown

@Composable
fun CategorySpendScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val userId = sessionManager.getUserId()

    val viewModel: CategorySpendOnlyViewModel = viewModel(
        factory = CategorySpendOnlyViewModelFactory(
            DatabaseProvider.getDatabase(context).categorySpendOnlyDao()
        )
    )

    var entries by remember { mutableStateOf<List<CategorySpendModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedEntryId by remember { mutableStateOf<Int?>(null) }

    // Trigger fetch on composition
    LaunchedEffect(Unit) {
        viewModel.getAllUserEntries(userId) {
            entries = it
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Your Expense Entries", style = MaterialTheme.typography.headlineMedium)

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Text("Error: $error", color = MaterialTheme.colorScheme.error)
        } else if (entries.isEmpty()) {
            Text("No expense entries found.")
        } else {
            if (selectedEntryId == null) {
                // Show the list of entries
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(entries, key = { it.id }) { entry ->
                        ExpenseEntryRow(
                            entry = entry,
                            onUpdate = { updated ->
                                // Set the selected entry and navigate to the update section
                                selectedEntryId = updated.id
                            },
                            onDelete = {
                                viewModel.deleteEntry(entry) {
                                    viewModel.getAllUserEntries(userId) {
                                        entries = it
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                // Show the update screen for the selected entry
                val entry = entries.find { it.id == selectedEntryId }
                entry?.let {
                    UpdateCategorySpendScreen(
                        navController = navController,
                        entryId = it.id,
                        onUpdateComplete = {
                            selectedEntryId = null // Reset back to list view after updating
                            viewModel.getAllUserEntries(userId) { updatedEntries ->
                                entries = updatedEntries
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ExpenseEntryRow(
    entry: CategorySpendModel,
    onUpdate: (CategorySpendModel) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Load the image from the URI (photoUri)
    LaunchedEffect(entry.photoUri) {
        try {
            val file = File(entry.photoUri)
            if (file.exists()) {
                bitmap = BitmapFactory.decodeStream(FileInputStream(file))
            }
        } catch (e: Exception) {
            Log.e("ExpenseEntryRow", "Error loading image: ${e.message}")
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 12.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Item: ${entry.ItemName}", style = MaterialTheme.typography.bodyLarge)
                    Text("Category: ${entry.category}", style = MaterialTheme.typography.bodySmall)
                    Text("Spend: ${entry.spend}", style = MaterialTheme.typography.bodyMedium)
                }

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = "Expense Photo",
                        modifier = Modifier
                            .size(60.dp)
                            .padding(start = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()

                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text("Photo URI: ${entry.photoUri}", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onUpdate(entry) }) {
                            Text("Update")
                        }
                        Button(onClick = { onDelete() }) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateCategorySpendScreen(
    navController: NavController,
    entryId: Int,
    onUpdateComplete: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val userId = sessionManager.getUserId()

    val viewModel: CategorySpendOnlyViewModel = viewModel(
        factory = CategorySpendOnlyViewModelFactory(
            DatabaseProvider.getDatabase(context).categorySpendOnlyDao()
        )
    )
    var entry by remember { mutableStateOf<CategorySpendModel?>(null) }
    var category by remember { mutableStateOf<String?>(null) }
    var spend by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf("") }

    LaunchedEffect(entryId) {
        viewModel.getEntryById(entryId) { data ->
            entry = data
            category = data?.ItemName
            spend = data?.spend?.toString() ?: ""
            photoUri = data?.photoUri ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Entry") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)) {

            entry?.let {
                // Category Dropdown
                CategoryDropdown(
                    categories = listOf("Food", "Transport", "Entertainment", "Bills"),
                    selectedCategory = category,
                    onCategorySelected = { category = it.toString() }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Spend input field
                TextField(
                    value = spend,
                    onValueChange = { spend = it },
                    label = { Text("Spend (R)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Upload Image button
                Button(onClick = { /* Open image picker */ }) {
                    Text("Upload Image")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display selected image URI
                Text("Selected Image URI: $photoUri")

                Spacer(modifier = Modifier.height(16.dp))

                // Save button
                Button(
                    onClick = {
                        val spendValue = spend.toFloatOrNull()
                        if (category.isNullOrBlank() || spendValue == null) {
                            Toast.makeText(context, "Please enter valid spend and select a category.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val updatedModel = it.copy(
                            ItemName = category ?: "",
                            spend = spendValue,
                            photoUri = photoUri
                        )

                        viewModel.updateEntry(updatedModel) {
                            Toast.makeText(context, "Entry updated successfully", Toast.LENGTH_SHORT).show()
                            onUpdateComplete()
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Update Spend")
                }
            }
        }
    }
}