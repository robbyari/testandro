package com.robbyari.testandroid.core

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class, Transaction::class, Path::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun pathDao(): PathDao
}