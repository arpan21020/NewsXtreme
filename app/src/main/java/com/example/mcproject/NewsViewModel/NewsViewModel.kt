package com.example.mcproject.NewsViewModel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.mcproject.api.Article
import com.example.mcproject.api.NewsRepository
import com.example.mcproject.api.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class NewsViewModel : ViewModel() {

    val category=MutableLiveData<String>("general")
    private var country=MutableLiveData<String>("in")


    val topHeadlines:MutableState<NewsResponse> = mutableStateOf(NewsResponse(
        status = "",
        totalResults = 0,
        articles = emptyList<Article>()
    ))
    private val repository:NewsRepository=NewsRepository()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            updateNews()
        }
    }

    fun updateCategory(category: String) {
        this.category.value = category

        viewModelScope.launch(Dispatchers.IO) {
            updateNews()
        }
    }

    fun updateCountry(country: String) {
        this.country.value = country

        viewModelScope.launch(Dispatchers.IO) {
            updateNews()
        }
    }
    private suspend fun updateNews(){
        try{
            topHeadlines.value=getTopHeadlines(category.value,country.value)
        }
        catch (e: Exception) {
            Log.e("NewsUpdate", "Error updating news data: ${e.message}")
        }
    }

    fun userSearch(query:String?){
        viewModelScope.launch(Dispatchers.IO) {
            topHeadlines.value=getSearchResults(query)
        }
    }

    fun setCountry(code:String){
//        country.value=code
    }

    private suspend  fun getTopHeadlines(category:String?,country:String?):NewsResponse{
        return repository.getTopHeadlines(category,country=country)
    }

    private suspend fun getSearchResults(query:String?):NewsResponse{
        return repository.getSearchResults(query)
    }


}