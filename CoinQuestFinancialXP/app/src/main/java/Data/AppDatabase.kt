package com.example.coinquest.data

import DOA.BudgetDao
import DOA.CategorySpendDao
import Model.BudgetModel
import Model.CategorySpendModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BudgetModel::class, CategorySpendModel::class, UserModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun BudgetDao(): BudgetDao
    abstract fun CategorySpendDao(): CategorySpendDao

}