package com.example.mcproject.NewsViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
}