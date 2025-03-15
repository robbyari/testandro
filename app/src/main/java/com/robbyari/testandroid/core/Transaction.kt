package com.robbyari.testandroid.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class Transaction (
    @PrimaryKey
    @ColumnInfo(name = "no_transaction")
    val noTransaction: String,

    @ColumnInfo(name = "created_at", index = true)
    val createdAt: String,
)