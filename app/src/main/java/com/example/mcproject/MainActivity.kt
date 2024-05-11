package com.example.mcproject

import UserLocation
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mcproject.NewsViewModel.DatabaseViewModel
import com.example.mcproject.NewsViewModel.NewsViewModel
import com.example.mcproject.api.Article
import com.example.mcproject.database.NewsData
import com.example.mcproject.database.NewsDatabase
import com.example.mcproject.ui.theme.MCProjectTheme




class MainActivity : ComponentActivity() {
    val user_location:UserLocation=UserLocation(this)
    companion object {
        lateinit var appContext: Context
            private set
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val DBviewModel= ViewModelProvider(this).get(DatabaseViewModel::class.java)

        appContext = applicationContext
        enableEdgeToEdge()
        setContent {
            MCProjectTheme {
//                CustomFontText()
                MainScreen(user_location = user_location,DBviewModel)
            }
        }
    }

    private val CustomFont = FontFamily(
        Font(resId = R.font.montserrat_semibold, weight = FontWeight.SemiBold, style = FontStyle.Normal)
        // Add more Font instances for more font weights and styles if necessary
    )
    @Preview(showBackground = true)
    @Composable
    fun CustomFontText() {
        Text(
            text = "Hello, World!",
            fontFamily = CustomFont,
            fontSize = 16.sp
        )
    }

}

@Composable
fun MainScreen(user_location:UserLocation,DBviewModel: DatabaseViewModel){
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }

    latitude=user_location.getUserLocation(context = MainActivity.appContext).latitude
    longitude=user_location.getUserLocation(context = MainActivity.appContext).longitude

    val viewModel:NewsViewModel=viewModel()
    val headlines=viewModel.topHeadlines.value
    val articles=headlines.articles
    Column {
        MyButton(viewModel)
        Log.d("LENGTH","${articles.size}")
        Log.d("LENGTH","${viewModel.category.value}")
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(articles){article->
                News(article = article)
                DBviewModel.upsert(
                    NewsData(
                         source=article.source.name,
                         author=article.author,
                         title=article.title,
                         description=article.description,
                         image=article.urlToImage,
                         publishing_time=article.publishedAt,
                    )
                )

            }
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
fun MyButton(viewModel:NewsViewModel) {
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/")) }

    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 36.dp),

        onClick = {
            viewModel.updateCategory("sports")
//            context.startActivity(intent)
        }) {
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


@Composable
fun GreetingPreview() {
    MCProjectTheme {
//        Greeting("Android")
        News(article = null)

    }
}