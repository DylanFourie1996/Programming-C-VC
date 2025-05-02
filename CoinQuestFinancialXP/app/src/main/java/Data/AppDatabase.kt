package com.example.coinquest.data


import DOA.BudgetDao
import DOA.CategorySpendDao
import DOA.CategorySpendOnlyDao
import Model.*
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserModel::class,
        CategoryModel::class,
        BudgetModel::class,
        CategorySpendModel::class,
        AchievementModel::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao
    abstract fun categorySpendDao(): CategorySpendDao
    abstract fun categorySpendOnlyDao(): CategorySpendOnlyDao
}
