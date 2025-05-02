package com.example.coinquestfinancialxp.ui.screens

import Utils.SessionManager
import ViewModels.BudgetViewModel
import ViewModels.CaptureNewBudgetViewModel
import ViewModels.Factories.BudgetViewModelFactory
import ViewModels.Factories.CaptureNewBudgetViewModelFactory
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.coinquest.data.DatabaseProvider
import com.example.coinquestfinancialxp.navigation.Screen
import com.example.coinquestfinancialxp.ui.theme.LocalCustomColors
import kotlinx.coroutines.flow.count


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val expandedFab = remember { mutableStateOf(false) }
    val customColors = LocalCustomColors.current

    var budgetIsCreated by remember { mutableStateOf(false) }

    // Initialize ViewModel for BudgetViewModel
    var budgetViewModel : BudgetViewModel = viewModel(factory = BudgetViewModelFactory(
        LocalContext.current
    ))

    // Check that budget has been created
    LaunchedEffect(Unit) {
        budgetViewModel.allBudgets.collect {budgetList ->
            budgetIsCreated = budgetList.isNotEmpty()
        }
    }

    BackHandler {  }
    Scaffold(
        containerColor=customColors.page,
        topBar = {
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Sub-FABs
                    AnimatedVisibility(
                        visible = expandedFab.value,
                        enter = fadeIn() + slideInVertically(),
                        exit = fadeOut() + slideOutVertically()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "Create Budget" FAB
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                /*Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Create Budget",
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }


                                SmallFloatingActionButton(
                                    onClick = {
                                        navController.navigate(Screen.CaptureNewBudget.route)
                                        expandedFab.value = false
                                    },
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Create,
                                        contentDescription = "Create Budget",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                                 */
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Insert Expense",
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                SmallFloatingActionButton(
                                    onClick = {
                                        if (!budgetIsCreated) { return@SmallFloatingActionButton }
                                        navController.navigate(Screen.CaptureCategorySpendScreen.route)
                                        expandedFab.value = false
                                    },
                                    modifier=Modifier.alpha(if (budgetIsCreated) 1.0f else 0.5f),
                                    containerColor = if (budgetIsCreated) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondary
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Insert Expense",

                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Add Category",
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 6.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                SmallFloatingActionButton(
                                    onClick = {
                                        navController.navigate(Screen.CategoryCreation.route)
                                        expandedFab.value = false
                                    },
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Add Category",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }

                    // Main FAB
                    FloatingActionButton(
                        onClick = { expandedFab.value = !expandedFab.value },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = if (expandedFab.value) Icons.Default.Close else Icons.Default.Add,
                            contentDescription = if (expandedFab.value) "Close Menu" else "Open Menu"
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal=16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment=Alignment.CenterHorizontally) {
                // Summary Card
                FinancialSummaryCard(navController)

                if (budgetIsCreated)
                {
                    Button(onClick = {
                        // Navigate to create budget screen
                        navController.navigate(Screen.CaptureNewBudget.route)
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "Create")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update Budget")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Transactions Section
            Text(
                text = "Recent Transactions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

//            RecentTransactionsList()

            Spacer(modifier = Modifier.height(24.dp))

            // Budget Progress Section
            Text(
                text = "Budget Progress",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

//            BudgetProgressList()

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(Screen.CategorySpendScreen.route) }, //Should Move it to Expense List
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Go To Expense List")
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = "Navigate")
            }

            Spacer(modifier=Modifier.height(24.dp))
        }
    }
}

@Composable
fun FinancialSummaryCard(navController : NavController) {
    // Getting the context and session handler
    val context = LocalContext.current
    val sessionHandler = remember { SessionManager.getInstance(context) }
    val currentUserId = sessionHandler.getUserId()

    var budgetIsCreated by remember { mutableStateOf(false) }

    // Initialize ViewModel for BudgetViewModel
    var budgetViewModel : BudgetViewModel = viewModel(factory = BudgetViewModelFactory(
        context
    ))

    // Check that budget has been created
    LaunchedEffect(Unit) {
        budgetViewModel.allBudgets.collect {budgetList ->
            budgetIsCreated = budgetList.isNotEmpty()
        }
    }


    // Initialize ViewModel and load budget info
    val viewModel: CaptureNewBudgetViewModel = viewModel(
        factory = CaptureNewBudgetViewModelFactory(
            DatabaseProvider.getDatabase(context).budgetDao()
        )
    )

    // Load current user's budget
    viewModel.loadCurrentBudget(currentUserId)

    // Observe the current budget
    val currentBudget = viewModel.currentBudget.collectAsState().value

    currentBudget?.let { budget ->
        // Calculate the totalBalance, income, and expenses
        val totalBalance = budget.remainingBalance
        val income = budget.limit
        val expenses = budget.totalSpent

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color.White,
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Total Balance", fontSize = 16.sp, color = Color.Gray)

                Text(
                    text = "R${"%.2f".format(totalBalance)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FinancialMetric(label = "Income", value = "R${"%.2f".format(income)}", positive = true)
                    FinancialMetric(label = "Expenses", value = "R${"%.2f".format(expenses)}", positive = false)
                }
            }
        }
    } ?: run {
        // If no budget data, show a loading or error state
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = Color.White,
            shape = RoundedCornerShape(15.dp),
            shadowElevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No budget information available",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                if (!budgetIsCreated) {
                    Button(onClick = {
                        // Navigate to create budget screen
                        navController.navigate(Screen.CaptureNewBudget.route)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Create")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Budget")
                    }
                }
            }
        }
    }


//    @Composable
//    fun RecentTransactionsList() {
//        val transactions = listOf(
//            "Groceries" to "-R45.32",
//            "Salary" to "+R1,500.00",
//            "Utilities" to "-R124.56"
//        )
//
//        Column {
//            transactions.forEach { (title, amount) ->
//                TransactionItem(title = title, amount = amount)
//            }
//        }
//    }

    @Composable
    fun TransactionItem(title: String, amount: String) {
        val isPositive = amount.startsWith("+")

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(100.dp),
            color = Color.White,
            shadowElevation = 3.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title)
                Text(
                    text = amount,
                    color = if (isPositive) Color(0xFF4CAF50) else Color(0xFFE57373),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }


//    @Composable
//    fun BudgetProgressList() {
//        // Placeholder for budget progress
//        // In a real app, this would be connected to your BudgetModel
//        val categories = listOf(
//            Triple("Food", 65f, 150f),
//            Triple("Transportation", 45f, 100f),
//            Triple("Entertainment", 80f, 75f)
//        )
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center,
//            modifier = Modifier.padding(vertical = 16.dp)
//        ) {
//            categories.forEach { (category, spent, total) ->
//                BudgetProgressItem(
//                    category = category,
//                    amountSpent = spent,
//                    totalBudget = total,
//                    onCreateBudgetClick = {
//                        // Navigate to create budget for this category
//                    },
//                    onExpenseListClick = {
//                        // Navigate to expense list for this category
//                    }
//                )
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//        }
//    }

    @Composable
    fun BudgetProgressItem(
        category: String,
        amountSpent: Float,
        totalBudget: Float,
        onCreateBudgetClick: () -> Unit = {},
        onExpenseListClick: () -> Unit = {}
    ) {
        val progress = (amountSpent / totalBudget).coerceIn(0f, 1f)
        val isOverBudget = amountSpent > totalBudget

        // State to track if options are expanded
        val expanded = remember { mutableStateOf(false) }
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            //.animateContentSize(), // Add animation for smooth transition
            color = Color.White,
            shadowElevation = 3.dp,
            shape = RoundedCornerShape(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = category)
                    Text(
                        text = "R${amountSpent} / R${totalBudget}",
                        color = if (isOverBudget) Color(0xFFE57373) else Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = if (isOverBudget) Color(0xFFE57373) else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                AnimatedVisibility(
                    visible = expanded.value,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onCreateBudgetClick()
                                expanded.value = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Create",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Create Budget")
                        }

                        Button(
                            onClick = {
                                onExpenseListClick()
                                expanded.value = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "List",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Expense List")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FinancialMetric(label: String, value: String, positive: Boolean) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 18.sp,
            color = if (positive) Color(0xFF4CAF50) else Color(0xFFE57373),
            fontWeight = FontWeight.Medium
        )
    }
}