package com.example.mcproject.NewsViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcproject.api.Article
import com.example.mcproject.api.NewsRepository
import com.example.mcproject.api.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class NewsViewModel : ViewModel() {

    val topHeadlines:MutableState<NewsResponse> = mutableStateOf(NewsResponse(
        status = "",
        totalResults = 0,
        articles = emptyList<Article>()
    ))
    private val repository:NewsRepository=NewsRepository()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            topHeadlines.value=getTopHeadlines()
        }
    }

    private suspend  fun getTopHeadlines():NewsResponse{
        return repository.getTopHeadlines()
    }
}