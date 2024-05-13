package com.example.mcproject.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class NewsApiService {
    private lateinit var TopHeadlinesApi: NewsApiTopHeadlines

    init{
        val retrofit= Retrofit.Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        TopHeadlinesApi=retrofit.create(NewsApiTopHeadlines::class.java)
    }
    suspend fun getTopHeadlines(category:String?,country:String?):NewsResponse{
        return TopHeadlinesApi.getTopHeadlines(category=category,country=country)
    }

    suspend fun getSearchResults(query:String?):NewsResponse{
        return TopHeadlinesApi.getSearchResults(search=query)
    }
    interface NewsApiTopHeadlines{
        @GET("/v2/top-headlines")
        suspend fun getTopHeadlines(
                @Query("country") country:String?="in",
                @Query("apiKey") apiKey: String="284f3b7ce14b4035954c41789d701a50",
                @Query("category") category:String?="general",
                @Query("pageSize") limit:Int?=100
        ): NewsResponse

        @GET("/v2/everything")
        suspend fun getSearchResults(
            @Query("apiKey") apiKey: String="284f3b7ce14b4035954c41789d701a50",
            @Query("pageSize") limit:Int?=100,
            @Query("q") search:String?="general"
        ):NewsResponse
    }
}