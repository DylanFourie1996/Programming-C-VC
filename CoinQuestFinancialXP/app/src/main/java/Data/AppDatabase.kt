package com.example.coinquest.data

import DOA.BudgetDao
import Model.BudgetModel
import Model.CategoryModel
import Model.CategorySpendModel
import androidx.room.Dao
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserModel::class, CategoryModel::class, BudgetModel::class, CategorySpendModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun BudgetDao() : BudgetDao
    abstract fun CategoryDao() : CategoryDao
}