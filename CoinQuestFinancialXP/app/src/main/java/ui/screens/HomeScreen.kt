package com.example.coinquestfinancialxp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.coinquestfinancialxp.navigation.Screen


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
    BackHandler {  }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CoinQuest Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.CaptureNewBudget.route) // Rout that the uer  can click on
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Face,//Achievement Page, We can Make it profile or Achievement or Bell
                            contentDescription = "Achievements",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
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
                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Create Budget",
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
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
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                SmallFloatingActionButton(
                                    onClick = {
                                        // navController.navigate(Screen.InsertExpense.route)
                                        expandedFab.value = false
                                    },
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Insert Expense",
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Summary Card
            FinancialSummaryCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Transactions Section
            Text(
                text = "Recent Transactions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            RecentTransactionsList()

            Spacer(modifier = Modifier.height(24.dp))

            // Budget Progress Section
            Text(
                text = "Budget Progress",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            BudgetProgressList()

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { navController.navigate(Screen.BudgetEntryList.route) }, //Should Move it to Expense List
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Go To Expense List")
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ArrowForward, contentDescription = "Navigate")
            }
        }
    }
}
@Composable
fun FinancialSummaryCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Total Balance",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = "R2,450.75",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FinancialMetric(label = "Income", value = "R3,200.00", positive = true)
                FinancialMetric(label = "Expenses", value = "R749.25", positive = false)
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

@Composable
fun RecentTransactionsList() {
    val transactions = listOf(
        "Groceries" to "-R45.32",
        "Salary" to "+R1,500.00",
        "Utilities" to "-R124.56"
    )

    Column {
        transactions.forEach { (title, amount) ->
            TransactionItem(title = title, amount = amount)
        }
    }
}

@Composable
fun TransactionItem(title: String, amount: String) {
    val isPositive = amount.startsWith("+")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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

@Composable
fun BudgetProgressList() {
    // Placeholder for budget progress
    // In a real app, this would be connected to your BudgetModel
    val categories = listOf(
        Triple("Food", 65f, 150f),
        Triple("Transportation", 45f, 100f),
        Triple("Entertainment", 80f, 75f)
    )
    Column {
        categories.forEach { (category, spent, total) ->
            BudgetProgressItem(
                category = category,
                amountSpent = spent,
                totalBudget = total,
                onCreateBudgetClick = {
                    // Navigate to create budget for this category
                },
                onExpenseListClick = {
                    // Navigate to expense list for this category
                }
            )
        }
    }
}

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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .animateContentSize(), // Add animation for smooth transition
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
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
