package com.example.mcproject.api

class NewsRepository() {
    private val newsService:NewsApiService = NewsApiService()

    suspend fun getTopHeadlines():NewsResponse{
        return newsService.getTopHeadlines()
    }

}