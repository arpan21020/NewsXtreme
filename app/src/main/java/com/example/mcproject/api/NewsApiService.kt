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

    interface NewsApiTopHeadlines{
        @GET("/v2/top-headlines")
        suspend fun getTopHeadlines(
                @Query("country") country:String?="in",
                @Query("apiKey") apiKey: String="f2b7a577139f4631b5add4f6d7026a79",
                @Query("category") category:String?="general",
                @Query("pageSize") limit:Int?=100
        ): NewsResponse
    }
}