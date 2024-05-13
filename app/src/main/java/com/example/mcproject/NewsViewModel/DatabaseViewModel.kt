package com.example.mcproject.NewsViewModel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.mcproject.database.NewsDBrepository
import com.example.mcproject.database.NewsData
import com.example.mcproject.database.NewsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseViewModel(application : Application): AndroidViewModel(application) {
    val allData: LiveData<List<NewsData>>
    private val repository: NewsDBrepository

    init{
        val wordDao= NewsDatabase.getDatabase(application).getDao()
        repository= NewsDBrepository(wordDao)
        allData=repository.allData.asLiveData()
//        fetchMinTemp(getCurrentDate())
    }
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun upsert(news: NewsData) = viewModelScope.launch(Dispatchers.IO) {
        repository.upsert(news)
    }

    fun delete(news: NewsData)=viewModelScope.launch(Dispatchers.IO) {
        repository.delete(news)
    }

    fun clearAllData()=viewModelScope.launch(Dispatchers.IO) {
        repository.clearAllData()
    }

    private suspend fun getBitmap(context : Context): Bitmap {
        val loading = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data("https://avatars3.githubusercontent.com/u/14994036?s=400&u=2832879700f03d4b37ae1c09645352a352b9d2d0&v=4")
            .build()

        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }
}