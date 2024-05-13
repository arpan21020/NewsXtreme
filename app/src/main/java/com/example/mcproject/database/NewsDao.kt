package com.example.mcproject.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Upsert
    suspend fun upsertData(news:NewsData)

    @Delete
    suspend fun deleteData(news: NewsData)

    @Query("SELECT * from news_table")
    fun getAllData(): Flow<List<NewsData>>

    @Query("DELETE FROM news_table")
    suspend fun clearAllData()
}