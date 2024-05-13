package com.example.mcproject.api

class NewsRepository() {
    private val newsService:NewsApiService = NewsApiService()

    suspend fun getTopHeadlines(category:String?,country:String?):NewsResponse{
        return newsService.getTopHeadlines(category,country=country)
    }
    suspend fun getSearchResults(query:String?):NewsResponse{
        return newsService.getSearchResults(query)
    }

}