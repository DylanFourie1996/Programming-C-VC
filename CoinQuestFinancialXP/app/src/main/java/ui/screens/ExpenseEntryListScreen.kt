package Screens

import Model.CategoryModel
import Model.CategorySpendModel
import Utils.SessionManager
import ViewModels.CategoryViewModel
import ViewModels.Factories.CategorySpendOnlyViewModelFactory
import ViewModels.Factories.CategoryViewModelFactory
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.io.File
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import java.io.FileInputStream
import com.example.coinquest.data.DatabaseProvider
import com.example.coinquest.viewmodel.CategorySpendOnlyViewModel
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors
import ui.CustomComposables.StandardButton
import ui.CustomComposables.StandardButtonTheme
import ui.CustomComposables.StandardTextBox
import ui.screens.CategoryDropdown

@Composable
fun CategorySpendScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val userId = sessionManager.getUserId()

    val viewModel: CategorySpendOnlyViewModel = viewModel(
        factory = CategorySpendOnlyViewModelFactory(
            context
        )
    )
    val categoryViewModel: CategoryViewModel = viewModel(
        factory = CategoryViewModelFactory(
            context
        )
    )

    var entries by remember { mutableStateOf<List<CategorySpendModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedEntryId by remember { mutableStateOf<Int?>(null) }
    var pageTitle by remember {mutableStateOf("Expenses")}
    var removeSpace by remember {mutableStateOf(false)}
    // Trigger fetch on composition
    LaunchedEffect(Unit) {
        viewModel.getAllUserEntries(userId) {
            entries = it
            isLoading = false
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal=16.dp).padding(top=16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(pageTitle, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier=Modifier.height(8.dp))
        Divider(modifier=Modifier.width(350.dp))
        if (!removeSpace)
            Spacer(modifier=Modifier.height(32.dp))

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
                removeSpace = false
                pageTitle = "Expenses"
                // Show the list of entries
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(entries, key = { it.id }) { entry ->
                        ExpenseEntryRow(
                            categoryViewModel=categoryViewModel,
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
                removeSpace = true
                pageTitle = "Modifying Expense"
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
    categoryViewModel: CategoryViewModel,
    entry: CategorySpendModel,
    onUpdate: (CategorySpendModel) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var actualTitle by remember {mutableStateOf("")}
    var customColors = LocalCustomColors.current

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
        colors=CardDefaults.cardColors(containerColor=customColors.TextBoxBG),
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
                    Text(entry.ItemName, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier=Modifier.height(5.dp))
                    Divider()
                    Spacer(modifier=Modifier.height(8.dp))
                    categoryViewModel.getCategoryById(entry.category)
                    { actualCategory : CategoryModel? ->

                        actualTitle = actualCategory!!.title
                    }

                    Text(
                        "Category: $actualTitle",
                        style = MaterialTheme.typography.bodySmall
                    )
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
            context
        )
    )
    var entry by remember { mutableStateOf<CategorySpendModel?>(null) }
    var selectedCategory by remember { mutableStateOf<CategoryModel?>(null) }
    var spend by remember { mutableStateOf("") }
    var spendTitle by remember {mutableStateOf("")}
    var photoUri by remember { mutableStateOf("") }



    var categoryViewModel : CategoryViewModel = viewModel(factory= CategoryViewModelFactory(context))
    val categories by categoryViewModel.allCategories.collectAsState()

    LaunchedEffect(entryId) {
        viewModel.getEntryById(entryId) { data ->
            // Retrieve and set the selectedCategory to the chosen category for this expense when created.
            categoryViewModel.getCategoryById(data!!.category) { grabbedCat ->
                selectedCategory = grabbedCat
            }
            entry = data
            spend = data?.spend?.toString() ?: ""
            spendTitle = data!!.ItemName
            photoUri = data?.photoUri ?: ""
        }
    }

    Scaffold(
        topBar = {
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding).padding(horizontal=32.dp).verticalScroll(rememberScrollState())) {

            entry?.let {
                // Category Dropdown
                if (selectedCategory != null) {
                    CategoryDropdown(
                        categories = categories,
                        selectedCategory = selectedCategory!!.title,
                        onCategorySelected = { selectedCategory = it }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Spend Title input field
                StandardTextBox(
                    value = spendTitle,
                    onValueChange = { spendTitle = it },
                    placeholder = "Expense Title",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier=Modifier.height(8.dp))
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
                StandardButton(
                    modifier=Modifier.padding(horizontal=32.dp),
                    text="Upload Image",
                    onClick = { /* Open image picker */ })

                Spacer(modifier = Modifier.height(16.dp))

                // Display selected image URI
                Text("Selected Image URI: $photoUri", fontSize = 12.sp)

                Spacer(modifier = Modifier.height(32.dp))

                // Save button
                StandardButton(
                    themeType= StandardButtonTheme.ORANGEGRAND,
                    modifier=Modifier.padding(horizontal=32.dp),
                    text="CONFIRM",
                    onClick = {
                        val spendValue = spend.toFloatOrNull()
                        if (selectedCategory == null || spendValue == null) {
                            Toast.makeText(context, "Please enter valid spend and select a category.", Toast.LENGTH_SHORT).show()
                            return@StandardButton
                        }

                        val updatedModel = it.copy(
                            ItemName = selectedCategory!!.title ?: "",
                            spend = spendValue,
                            photoUri = photoUri
                        )

                        viewModel.updateEntry(updatedModel) {
                            Toast.makeText(context, "Entry updated successfully", Toast.LENGTH_SHORT).show()
                            onUpdateComplete()
                        }
                    }
                )
                Spacer(modifier=Modifier.height(16.dp))
            }
        }
    }
}