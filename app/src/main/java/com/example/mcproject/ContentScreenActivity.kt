package com.example.mcproject
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.mcproject.api.Article
import java.lang.reflect.Modifier

class ContentScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val article = intent.getSerializableExtra("article") as Article
        setContent {
            NewsContent(article)
        }
    }

    @Composable
    fun NewsContent(article: Article) {

        LazyColumn(
            modifier = Modifier.fillMaxSize()


        ) {

        }



        Text(text = article.title)
        // Add more fields from the Article object as needed
    }
}