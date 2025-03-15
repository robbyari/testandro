package com.robbyari.testandroid.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "path")
data class Path(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "parent_id")
    val parentId: Int?
)
