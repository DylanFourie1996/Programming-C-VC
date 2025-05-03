package DOA

import androidx.room.*
import Model.CategorySpendModel

@Dao
interface CategorySpendOnlyDao {

    @Query("""
        SELECT cs.* FROM categoryspend cs
        INNER JOIN budget b ON cs.budgetId = b.id
        WHERE b.userId = :userId
    """)
    suspend fun getAllSpendsForUser(userId: Int): List<CategorySpendModel>

    @Query("SELECT * FROM categoryspend WHERE id = :entryId")
    suspend fun getEntryById(entryId: Int): CategorySpendModel?

    // Insert method
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: CategorySpendModel)

    // Update method
    @Update
    suspend fun updateEntry(entry: CategorySpendModel)

    @Delete
    suspend fun deleteEntry(entry: CategorySpendModel)

}
