package com.example.coinquest.data

import DOA.CategorySpendDao
import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "coinquest_database"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = instance
            instance
        }
    }

    fun getCategorySpendDao(context: Context): CategorySpendDao {
        return getDatabase(context).CategorySpendDao()
    }
}
