@file:Suppress("JUnitMalformedDeclaration")

package ViewModels

import DOA.BudgetDao
import Model.BudgetModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertFailsWith

class CaptureNewBudgetViewModelTest {
    internal lateinit var viewModel: CaptureNewBudgetViewModel
    internal lateinit var fakeBudgetDao: FakeBudgetDao

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeBudgetDao = FakeBudgetDao()
        viewModel = CaptureNewBudgetViewModel(fakeBudgetDao)
    }

    private fun UnconfinedTestDispatcher(): Any {
        TODO("Not yet implemented")
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInsertBudgetInvalidInput() {
        assertFailsWith<IllegalArgumentException> {
            viewModel.insertBudget(
                userId = 1,
                limit = 1000f,
                save = 1500f,
                durationType = CaptureNewBudgetViewModel.WEEKLY
            )
        }
    }

    @Test
    suspend fun testInsertBudgetValidInput() {
        val userId = 1
        val limit = 1000f
        val save = 300f
        val durationType = CaptureNewBudgetViewModel.MONTHLY
        val currency = "ZAR"

        // Act
        viewModel.insertBudget(userId, limit, save, durationType, currency)

        // Assert
        val budgets = fakeBudgetDao.getBudgetsByUserId(userId)
        assertEquals(1, budgets.size)
        val insertedBudget = budgets.last()
        assertEquals(userId, insertedBudget.userId)
        assertEquals(limit, insertedBudget.limit)
        assertEquals(save, insertedBudget.save)
        assertEquals(durationType, insertedBudget.durationType)
        assertEquals(currency, insertedBudget.currency)
        assertEquals(limit - save, insertedBudget.remainingBalance)
        assertEquals(0f, insertedBudget.totalSpent)
    }

    @Test
    fun testLoadCurrentBudgetNotFound() {
        // Arrange
        val userId = 1

        // Act
        viewModel.loadCurrentBudget(userId)

        // Assert
        val currentBudget = viewModel.currentBudget.value
        assertNull(currentBudget)
    }
}

@Test
suspend fun CaptureNewBudgetViewModelTest.testLoadCurrentBudgetFound() {
    // Arrange
    val userId = 1
    val budget = BudgetModel(
        id = 1,
        userId = userId,
        limit = 1000f,
        save = 200f,
        durationType = CaptureNewBudgetViewModel.WEEKLY,
        startDate = System.currentTimeMillis(),
        remainingBalance = 800f,
        totalSpent = 0f,
        currency = "ZAR"
    )
    fakeBudgetDao.insertBudget(budget)

    // Act
    viewModel.loadCurrentBudget(userId)

    // Assert
    val currentBudget = viewModel.currentBudget.value
    assertNotNull(currentBudget)
    assertEquals(budget, currentBudget)
}

private fun Dispatchers.resetMain() {
    TODO("Not yet implemented")
}

private fun Dispatchers.setMain(value: Any) {
    TODO("Not yet implemented")
}

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

    override fun getAllBudgets(userId: Int): kotlinx.coroutines.flow.Flow<List<BudgetModel>> {
        throw NotImplementedError("Not needed for tests")
    }

    override suspend fun deleteBudgetByUserId(userId: Int) {
        budgets.removeAll { it.userId == userId }
    }
}