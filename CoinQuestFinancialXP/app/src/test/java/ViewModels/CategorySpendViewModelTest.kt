package com.example.coinquest.viewmodel

import DOA.BudgetDao
import DOA.CategorySpendDao
import DOA.CategorySpendPair
import Model.BudgetModel
import Model.CategorySpendModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("JUnitMalformedDeclaration")
class CategorySpendViewModelTest {
    private lateinit var viewModel: CategorySpendViewModel
    private lateinit var fakeCategorySpendDao: FakeCategorySpendDao
    private lateinit var fakeBudgetDao: FakeBudgetDao

    @BeforeEach
    fun setup() {
        // Set up coroutine dispatcher for testing
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeCategorySpendDao = FakeCategorySpendDao()
        fakeBudgetDao = FakeBudgetDao()
        viewModel = CategorySpendViewModel(fakeCategorySpendDao, fakeBudgetDao)
    }

    private fun UnconfinedTestDispatcher(): Any {
        TODO("Not yet implemented")
    }

    @AfterEach
    fun tearDown() {
        // Reset coroutine dispatcher
        Dispatchers.resetMain()
    }

    @Test
    suspend fun testGetBudgetByIdSuccess() {
        // Arrange
        val budgetId = 1
        val budget = BudgetModel(
            id = budgetId,
            userId = 1,
            limit = 1000f,
            save = 200f,
            durationType = 1,
            startDate = System.currentTimeMillis(),
            totalSpent = 0f,
            remainingBalance = 800f,
            currency = "ZAR"
        )
        fakeBudgetDao.insertBudget(budget)
        var result: BudgetModel? = null

        // Act
        viewModel.getBudgetById(budgetId) { budgetResult ->
            result = budgetResult
        }

        // Assert
        assertNotNull(result)
        assertEquals(budget, result)
    }

    @Test
    fun testGetBudgetByIdNotFound() {
        // Arrange
        val budgetId = 999
        var result: BudgetModel? = null

        // Act
        viewModel.getBudgetById(budgetId) { budgetResult ->
            result = budgetResult
        }

        // Assert
        assertNull(result)
    }

    @Test
    suspend fun testInsertSpendAndUpdateBudget() {
        // Arrange
        val budgetId = 1
        val initialBudget = BudgetModel(
            id = budgetId,
            userId = 1,
            limit = 1000f,
            save = 200f,
            durationType = 1,
            startDate = System.currentTimeMillis(),
            totalSpent = 0f,
            remainingBalance = 800f,
            currency = "ZAR"
        )
        fakeBudgetDao.insertBudget(initialBudget)
        val spendEntry = CategorySpendModel(
            id = 0,
            budgetId = budgetId,
            ItemName = "Coffee",
            category = 1,
            spend = 50f,
            creationDate = Date(),
            photoUri = "uri://photo1"
        )
        var onDoneCalled = false

        // Act
        viewModel.insertSpendAndUpdateBudget(spendEntry) {
            onDoneCalled = true
        }

        // Assert
        val spends = fakeCategorySpendDao.getSpends()
        assertEquals(1, spends.size)
        assertEquals(spendEntry.copy(id = 1), spends.first())
        val updatedBudget = fakeBudgetDao.getBudgetById(budgetId)
        assertNotNull(updatedBudget)
        assertEquals(750f, updatedBudget.remainingBalance) // 800 - 50
        assertTrue(onDoneCalled)
    }
}

private fun Dispatchers.setMain(value: Any) {}

private fun Dispatchers.resetMain() {
    TODO("Not yet implemented")
}

// Fake CategorySpendDao implementation
class FakeCategorySpendDao : CategorySpendDao {
    private val spendPairs = mutableListOf<CategorySpendPair>()
    private val spends = mutableListOf<CategorySpendModel>()
    private val spendPairsFlow = MutableSharedFlow<List<CategorySpendPair>>(replay = 1)

    fun addSpendPair(pair: CategorySpendPair) {
        spendPairs.add(pair)
        spendPairsFlow.tryEmit(spendPairs)
    }

    fun getSpends(): List<CategorySpendModel> = spends

    override fun getCategorySpendPairs(userId: Int, budgetId: Int): Flow<List<CategorySpendPair>> {
        return spendPairsFlow
    }

    override suspend fun insertCategorySpend(categorySpend: CategorySpendModel): Long {
        val newId = (spends.maxOfOrNull { it.id } ?: 0) + 1
        val spendWithId = categorySpend.copy(id = newId)
        spends.add(spendWithId)
        return newId.toLong()
    }
}

// Fake BudgetDao implementation
class FakeBudgetDao : BudgetDao {
    private val budgets = mutableListOf<BudgetModel>()

    override suspend fun getBudgetsByUserId(userId: Int): List<BudgetModel> {
        return budgets.filter { it.userId == userId }
    }

    override suspend fun getBudgetById(id: Int): BudgetModel {
        return budgets.find { it.id == id } ?: throw NoSuchElementException("Budget not found")
    }

    override suspend fun insertBudget(budget: BudgetModel): Long {
        val newId = (budgets.maxOfOrNull { it.id } ?: 0) + 1
        val budgetWithId = budget.copy(id = newId)
        budgets.add(budgetWithId)
        return newId.toLong()
    }

    override suspend fun updateRemainingBalance(budgetId: Int, newBalance: Float) {
        val budget = budgets.find { it.id == budgetId }
        if (budget != null) {
            budgets.remove(budget)
            budgets.add(budget.copy(remainingBalance = newBalance))
        }
    }

    override fun getAllBudgets(userId: Int): Flow<List<BudgetModel>> {
        throw NotImplementedError("Not needed for tests")
    }

    override suspend fun deleteBudgetByUserId(userId: Int) {
        budgets.removeAll { it.userId == userId }
    }
}