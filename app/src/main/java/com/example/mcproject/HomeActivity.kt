package com.example.mcproject

import UserLocation
import android.content.Intent
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)


class HomeActivity : ComponentActivity() {
    private val userLocation:UserLocation=UserLocation(this)
    companion object {
        lateinit var appContext: Context
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val databaseViewModel= ViewModelProvider(this).get(DatabaseViewModel::class.java)

        appContext = applicationContext
        setContent {
            MCProjectTheme {
                val items = listOf(
                    BottomNavigationItem(
                        title = "Top News",
                        selectedIcon = painterResource(id = R.drawable.news_selected),
                        unselectedIcon = painterResource(id = R.drawable.news_unselected),
                        hasNews = false
                    ),
                    BottomNavigationItem(
                        title = "Downloads",
                        selectedIcon = painterResource(id = R.drawable.download_selected),
                        unselectedIcon = painterResource(id = R.drawable.download_unselected),
                        hasNews = false
                    )
                )
                var selectedItemIndex by rememberSaveable{
                    mutableIntStateOf(0)
                }
                val context = LocalContext.current

                Scaffold(
                    modifier=Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar{
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                        when (index) {
                                            0 -> {
                                                val intent = Intent(context, MainActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                context.startActivity(intent)
                                            }

                                            1 -> {
                                                val intent = Intent(context, DownloadedNewsActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                context.startActivity(intent)
                                            }
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painter = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title,
                                            tint = if (selectedItemIndex == index) Primary else Color.Gray
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            color = if (selectedItemIndex == index) Primary else Color.Gray
                                        )
                                    },
                                )
                            }

                        }
                    }
                ) {
                    MainScreen(userLocation = userLocation,databaseViewModel, LocalContext.current)
                }



            }
        }
    }



}

@Composable
fun MainScreen(userLocation:UserLocation, databaseViewModel: DatabaseViewModel,context:Context){
    var latitude by remember { mutableDoubleStateOf(0.0) }
    var longitude by remember { mutableDoubleStateOf(0.0) }

    latitude=userLocation.getUserLocation(context = HomeActivity.appContext).latitude
    longitude=userLocation.getUserLocation(context = HomeActivity.appContext).longitude


    val categories = listOf("general","business", "entertainment", "health", "science", "sports", "technology")
    var selectedCategory by remember { mutableStateOf("general") }
    var searchQuery by remember { mutableStateOf("") }


    val viewModel:NewsViewModel=viewModel()
    val headlines=viewModel.topHeadlines.value
    val articles=headlines.articles
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
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
                                           viewModel.updateCategory(selectedCategory)},
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
                        .background(color = Color.White)
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
                            if (searchQuery==""){
                                Log.d("COMPARISON","Comparision successfull")

                            }
                            else{
                                Log.d("COMPARISION","UNSUECCESSFUL :$searchQuery")
                                viewModel.userSearch(searchQuery)

                            }
                        }
                )

            }


            //MyButton(viewModel)
            Log.d("LENGTH","${articles.size}")
            Log.d("LENGTH","${viewModel.category.value}")

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
                            category = selectedCategory,
                            publishedAt =article.publishedAt,
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
                intent.putExtra(
                    "mode",
                    mode
                ) // Replace "your_mode_string" with the actual string you want to pass
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}

