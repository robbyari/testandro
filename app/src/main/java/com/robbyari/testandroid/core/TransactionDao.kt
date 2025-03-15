package com.robbyari.testandroid.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTransactionNo(transaction: Transaction)

    @Query("SELECT * FROM `transaction`")
    fun getAllTransactionNo(): Flow<List<Transaction>>

    @Query("SELECT COUNT(*) FROM `transaction` WHERE strftime('%Y-%m', created_at) = :yearMonth")
    suspend fun getTransactionCountForMonth(yearMonth: String): Int
}