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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mcproject.NewsViewModel.DatabaseViewModel
import com.example.mcproject.NewsViewModel.NewsViewModel
import com.example.mcproject.api.Article
import com.example.mcproject.database.NewsData
import com.example.mcproject.ui.theme.BackgroundColor
import com.example.mcproject.ui.theme.Bold
import com.example.mcproject.ui.theme.ExtraBold
import com.example.mcproject.ui.theme.HeaderUnselected
import com.example.mcproject.ui.theme.MCProjectTheme
import com.example.mcproject.ui.theme.MediumItalic
import com.example.mcproject.ui.theme.Primary


class MainActivity : ComponentActivity() {
    private val userLocation:UserLocation=UserLocation(this)
    companion object {
        lateinit var appContext: Context
            private set
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Only start SplashActivity if it's not already running
//        if (!intent.getBooleanExtra("FROM_SPLASH", false)) {
//            val splashIntent = Intent(this, SplashActivity::class.java)
//            splashIntent.putExtra("FROM_MAIN", true)
//            startActivity(splashIntent)
//        }
        val databaseViewModel= ViewModelProvider(this).get(DatabaseViewModel::class.java)

        appContext = applicationContext
        enableEdgeToEdge()
        setContent {
            MCProjectTheme {
//                CustomFontText()
                MainScreen(userLocation = userLocation,databaseViewModel)
            }
        }
    }



}

@Composable
fun MainScreen(userLocation:UserLocation, databaseViewModel: DatabaseViewModel){
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }

    latitude=userLocation.getUserLocation(context = MainActivity.appContext).latitude
    longitude=userLocation.getUserLocation(context = MainActivity.appContext).longitude


    val categories = listOf("general","entertainment", "business", "science","technology", "politics")
    var selectedCategory by remember { mutableStateOf("general") }
    var searchQuery by remember { mutableStateOf("") }


    val viewModel:NewsViewModel=viewModel()
    val headlines=viewModel.topHeadlines.value
    val articles=headlines.articles
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 28.dp)
    )  {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Text(
                text = "news",
                fontSize = 36.sp,
                fontFamily = ExtraBold,
                color = Primary
            )
            Text(
                text = "xtreme",
                fontSize = 36.sp,
                fontFamily = ExtraBold
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BackgroundColor)
        ) {
            LazyRow {
                items(categories) { category ->
                    Column {
                        Text(
                            text = category,
                            fontSize = 20.sp,
                            fontFamily = ExtraBold,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 14.dp)
                                .clickable { selectedCategory = category;
                                    viewModel.updateCategory(selectedCategory) },
                            color = if (category == selectedCategory) Primary else HeaderUnselected
                        )
                        if (category == selectedCategory) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .background(color = Primary)
                            )
                        }
                    }

                }
            }
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp)
                    .background(shape = RoundedCornerShape(14.dp), color = Color.White)
                    .border(2.dp, Color.LightGray, shape = RoundedCornerShape(14.dp))
            ) {
                TextField(
                    value = searchQuery,
                    placeholder = { Text("Search") },
                    onValueChange = { searchQuery = it },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                        .background(color = Color.White,)
                )

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Button",
                    tint = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp))
                        .background(Primary)

                        .padding(5.dp)
                        .size(48.dp)
                        .clickable {
                            //when search clicked what should be done
                            viewModel.updateCategory(searchQuery)
                        }
                )

            }


            val context = LocalContext.current
            LazyColumn(contentPadding = PaddingValues(14.dp)) {
                items(articles){article->
                    NewsCard(article = article, context = context, mode = "online")
                    databaseViewModel.upsert(
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

}




@Composable
fun NewsCard(article: Article?, context: Context, mode:String="online"){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = Color.White, shape = RoundedCornerShape(14.dp))
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(14.dp))
            .clickable {
                val intent = Intent(context, ContentScreenActivity::class.java)
                intent.putExtra("article", article)
                intent.putExtra("mode", mode) // Replace "your_mode_string" with the actual string you want to pass
                context.startActivity(intent)
            }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ){
            if (article != null) {
                Text(
                    text = article.publishedAt?.let { convertDateString(it) } ?: "No Date-Time",
                    fontSize = 12.sp,
                    color = Primary,
                    fontFamily = ExtraBold,
                    modifier = Modifier.padding(4.dp)
                )
            }
            Text(
                text = article?.title ?: "No Title",
                fontSize = 14.sp,
                fontFamily = Bold,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = article?.author ?: "No Author",
                fontSize = 12.sp,
                color = Primary,
                textAlign = TextAlign.Right,
                fontFamily = MediumItalic,
                modifier = Modifier.fillMaxWidth().padding(4.dp)
            )
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

