package Screens

import Model.CategoryModel
import Model.CategorySpendModel
import Utils.SessionManager
import ViewModels.CategoryViewModel
import ViewModels.Factories.CategorySpendOnlyViewModelFactory
import ViewModels.Factories.CategoryViewModelFactory
import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.io.File
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
import ui.RequestImagePermissionIfNeeded
import ui.screens.CategoryDropdown
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CategorySpendScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val userId = sessionManager.getUserId()
    val customColors = LocalCustomColors.current

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
    var selectedEntries by remember { mutableStateOf<List<CategorySpendModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedEntryId by remember { mutableStateOf<Int?>(null) }
    var pageTitle by remember {mutableStateOf("Expenses")}
    var removeSpace by remember {mutableStateOf(false)}
    // Trigger fetch on composition
    LaunchedEffect(Unit) {
        try {
            viewModel.getAllUserEntries(userId) {
                entries = it
                isLoading = false
            }
        } catch (e : Exception)
        {
            e.printStackTrace()
            println("Error loading user entries")
            isLoading = true
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(horizontal=16.dp).padding(top=16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(color=customColors.TextColor,text=pageTitle, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier=Modifier.height(8.dp))
        Divider(modifier=Modifier.width(350.dp))
        if (!removeSpace)
            Spacer(modifier=Modifier.height(32.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Text(text="Error: $error", color = MaterialTheme.colorScheme.error)
        } else if (entries.isEmpty()) {
            Text(color=customColors.TextColor,text="No expense entries found.")
        } else {
            if (selectedEntryId == null) {
                removeSpace = false
                pageTitle = "Expenses"
                // Show User Selectable Date
                UserSelectableDate(entries=entries) { filtered ->
                    selectedEntries = filtered
                }
                // Show the list of entries
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(selectedEntries.ifEmpty {entries}, key = { it.id }) { entry ->
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
fun UserSelectableDate(entries: List<CategorySpendModel>,
                       onFilter: (List<CategorySpendModel>) -> Unit)
{
    val customColors = LocalCustomColors.current
    var startDateStr by remember {mutableStateOf("")}
    var endDateStr by remember {mutableStateOf("")}
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var expanded by remember {mutableStateOf(false)}

    fun showDatePicker(onDateSelected : (String) -> Unit) {
        DatePickerDialog(context, { _, year, month, day ->
            onDateSelected("${day.toString().padStart(2, '0')}/${(month + 1).toString().padStart(2, '0')}/$year")

        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun filterLogic()
    {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDate = sdf.parse(startDateStr)
            val endDate = sdf.parse(endDateStr)

            if (startDate != null && endDate != null)
            {
                val filtered = entries.filter {
                    val entryDate = it.creationDate
                    entryDate != null && !entryDate.before(startDate) && !entryDate.after(endDate)
                }
                onFilter(filtered)
            }
        } catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    Box(contentAlignment=Alignment.Center, modifier = Modifier.fillMaxWidth()
    ) {
        if (!expanded) {
            OutlinedButton(
                onClick= {
                // Expand box
                expanded = !expanded
            },
                modifier=Modifier.width(300.dp)) {
                Text(color=customColors.TextColor,text="Click here to filter!")
            }
        }
        if (expanded) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { showDatePicker { startDateStr = it } },
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text(color=customColors.TextColor,text=if (startDateStr.isNotEmpty()) startDateStr else "Start Date")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedButton(
                        onClick = { showDatePicker { endDateStr = it } },
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text(color=customColors.TextColor,text=if (endDateStr.isNotEmpty()) endDateStr else "End Date")
                    }


                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(colors= ButtonDefaults.buttonColors(containerColor =customColors.InColor),
                    onClick = {
                        // Filter
                        filterLogic()
                    },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(color=customColors.TextColor,text="FILTER")
                }
                Spacer(modifier = Modifier.height(16.dp))
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
    // (Developer et al., 2025)
    RequestImagePermissionIfNeeded()
    var expanded by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var actualTitle by remember {mutableStateOf("")}
    var customColors = LocalCustomColors.current
    val simpleDateFormat = SimpleDateFormat("yyyy-mm-dd' | Time: 'HH:mm:ss")
    val context = LocalContext.current

    // Load the image from the URI (photoUri)
    LaunchedEffect(entry.photoUri) {
        try {
            // CONTENT RESOLVER API
            val uri = Uri.parse(entry.photoUri)
            context.contentResolver.openInputStream(uri)?.use {inputStream ->
                bitmap = BitmapFactory.decodeStream(inputStream)
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
        colors=CardDefaults.cardColors(containerColor=customColors.PadColor),
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
                    Text(color=customColors.TextColor,text=entry.ItemName, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier=Modifier.height(5.dp))
                    Divider()
                    Spacer(modifier=Modifier.height(8.dp))
                    
                    categoryViewModel.getCategoryById(entry.category)
                    { actualCategory : CategoryModel? ->
                        if (actualCategory != null)
                            actualTitle = actualCategory!!.title
                        else
                            actualTitle = "NULL"
                    }

                    Text(color=customColors.TextColor,
                        text="Category: $actualTitle",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(color=customColors.TextColor,text="Spend: ${entry.spend}", style = MaterialTheme.typography.bodyMedium)
                    Text(color=customColors.TextColor,text="Date: ${simpleDateFormat.format(entry.creationDate)}", style = MaterialTheme.typography.bodyMedium)
                }

                // (Developer et al., 2025)
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
                    Text(color=customColors.TextColor,text="Photo URI: ${entry.photoUri}", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.Center, modifier=Modifier.fillMaxWidth()) {
                        Button(
                            colors=ButtonDefaults.buttonColors(containerColor=customColors.InColor),
                            onClick = { onUpdate(entry) }) {
                            Text(color=customColors.TextColor,text="Update")
                        }
                        Spacer(modifier=Modifier.width(8.dp))
                        Button(
                            colors=ButtonDefaults.buttonColors(containerColor=customColors.InColor),
                            onClick = { onDelete() }) {
                            Text(color=customColors.TextColor,text="Delete")
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
    // (Developer et al., 2025)
    RequestImagePermissionIfNeeded()
    val customColors = LocalCustomColors.current
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
    var photoUri by remember { mutableStateOf(entry?.photoUri ?: "") }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Load the image from the URI (photoUri)
    LaunchedEffect(photoUri) {
        try {
            // CONTENT RESOLVER API
            val uri = Uri.parse(photoUri)
            context.contentResolver.openInputStream(uri)?.use {inputStream ->
                bitmap = BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            Log.e("ExpenseEntryRow", "Error loading image: ${e.message}")
        }
    }

    // (Developer et al., 2025)
    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoUri = uri?.toString() ?: ""
    }



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
        containerColor=customColors.page,
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

                Spacer(modifier = Modifier.height(16.dp))

                // Spend Title input field
                StandardTextBox(
                    value = spendTitle,
                    onValueChange = { spendTitle = it },
                    placeholder = "Expense Title",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier=Modifier.height(16.dp))
                // Spend input field
                StandardTextBox(
                    value = spend,
                    onValueChange = { spend = it },
                    placeholder="Spend (R)",
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier=Modifier.fillMaxWidth(), contentAlignment= Alignment.Center)
                {
                    // (Developer et al., 2025)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Upload Image button
                StandardButton(
                    modifier=Modifier.padding(horizontal=32.dp),
                    text="Update Image",
                    onClick = {
                    /* Open image picker */
                        imageLauncher.launch("image/*")
                    })

                Spacer(modifier = Modifier.height(32.dp))

                // Display selected image URI
                Text(color=customColors.TextColor,text="Selected Image URI: $photoUri", fontSize = 12.sp)

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
                            ItemName = spendTitle ?: "",
                            spend = spendValue,
                            category=selectedCategory!!.id,
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

/*
References

Developers. 2025. Access media files from shared storage, 16 April 2025. [Online]. Available at: https://developer.android.com/training/data-storage/shared/media [Accessed 3 May 2025].
Developers. 2025. Compose and other libraries, 16 April 2025. [Online]. Available at: https://developer.android.com/develop/ui/compose/libraries [Accessed 3 May 2025].
 */