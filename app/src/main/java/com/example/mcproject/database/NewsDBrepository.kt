package com.example.mcproject.database

import kotlinx.coroutines.flow.Flow


class NewsDBrepository (private val dao:NewsDao) {
    val allData: Flow<List<NewsData>> =dao.getAllData()


    suspend fun upsert(news: NewsData){
        dao.upsertData(news)
    }

    suspend fun delete(news: NewsData){
        dao.deleteData(news)
    }
    suspend fun clearAllData(){
        dao.clearAllData()
    }


}