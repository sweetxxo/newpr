package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Query("SELECT * FROM content_items ORDER BY updatedAtMillis DESC")
    fun observeAll(): Flow<List<ContentEntity>>

    @Query("SELECT COUNT(*) FROM content_items")
    suspend fun countItems(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: ContentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<ContentEntity>)

    @Query("DELETE FROM content_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM content_items")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(items: List<ContentEntity>) {
        deleteAll()
        upsertAll(items)
    }
}
