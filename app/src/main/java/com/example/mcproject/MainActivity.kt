package com.example.mcproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mcproject.NewsViewModel.NewsViewModel
import com.example.mcproject.api.Article
import com.example.mcproject.ui.theme.MCProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MCProjectTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(){
    val viewModel:NewsViewModel=viewModel()
    val headlines=viewModel.topHeadlines.value
    val articles=headlines.articles
//    MyButton()
    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(articles){article->
            News(article = article)

        }
    }
}

@Composable
fun News(article: Article?){
    Card(
        shape= RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
//        Text(text = "Source: ${article?.source?.name ?: "N/A"}")
            Text(text = "Title: ${article?.title ?: "N/A"}", fontSize = 20.sp)
            Text(text = "Author: ${article?.author ?: "N/A"}")
            Text(text = "Description: ${article?.description ?: "N/A"}")
//        Text(text = "Published At: ${article?.publishedAt ?: "N/A"}")
//        Text(text="Content : ${article?.content}")


        }
    }

}
@Composable
fun MyButton() {
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")) }

    Button(modifier = Modifier.fillMaxWidth().padding(top=36.dp),

        onClick = { context.startActivity(intent) }) {
        Text(text = "Navigate to Google!")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MCProjectTheme {
//        Greeting("Android")
        News(article = null)

    }
}