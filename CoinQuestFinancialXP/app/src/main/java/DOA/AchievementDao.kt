//package DOA
//
//import Model.AchievementModel
//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.Query
//import androidx.room.Update
//
//@Dao
//interface AchievementDao {
//
//    // Insert a new Achievement
//    @Insert
//    suspend fun insertAchievement(achievement: AchievementModel)
//
//    // Update an existing Achievement
//    @Update
//    suspend fun updateAchievement(achievement: AchievementModel)
//
//    // Delete an Achievement
//    @Delete
//    suspend fun deleteAchievement(achievement: AchievementModel)
//
//    // Get all achievements (using @Query)
//    @Query("SELECT * FROM achievements")
//    suspend fun getAllAchievements(): List<AchievementModel>
//
//    // Get a specific achievement by ID (using @Query)
//    @Query("SELECT * FROM achievements WHERE id = :achievementId")
//    suspend fun getAchievementById(achievementId: Int): AchievementModel?
//
//    // Example of using @RawQuery to execute a custom query
//    @RawQuery
//    suspend fun runCustomQuery(query: SupportSQLiteQuery): List<AchievementModel>
//}
