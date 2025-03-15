package com.robbyari.testandroid.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PathDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePath(path: Path)

    @Query("SELECT * FROM path")
    fun getAllPaths(): Flow<List<Path>>

    @Query("UPDATE path SET parent_id = :newParentId WHERE parent_id = :oldParentId")
    suspend fun updateParent(oldParentId: Int, newParentId: Int)

    @Query("DELETE FROM path WHERE parent_id = :parentId")
    suspend fun deleteChildren(parentId: Int)

    @Query("SELECT * FROM path WHERE parent_id IS NULL")
    fun getRootPaths(): Flow<List<Path>>

    @Query("SELECT * FROM path WHERE parent_id = :parentId")
    fun getPathsByParentId(parentId: Int): Flow<List<Path>>

    @Query("UPDATE path SET name = :newName WHERE name = :oldName")
    suspend fun updatePathName(newName: String, oldName: String)

    @Query("SELECT id FROM path WHERE name = :name LIMIT 1")
    suspend fun getPathIdByName(name: String): Int?

    @Query("SELECT * FROM path WHERE parent_id = :parentId")
    suspend fun getChildren(parentId: Int): List<Path>

    @Query("DELETE FROM path WHERE id = :pathId")
    suspend fun deletePathById(pathId: Int)

    @Query("SELECT COUNT(*) FROM path WHERE name = :name")
    suspend fun isPathNameExists(name: String): Int
}