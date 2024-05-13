package com.example.mcproject

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.runtime.remember
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.mcproject.NewsViewModel.DatabaseViewModel
import com.example.mcproject.NewsViewModel.NewsViewModel
import com.example.mcproject.api.Article
import com.example.mcproject.api.Source
import com.example.mcproject.database.NewsData
import com.example.mcproject.ui.theme.BackgroundColor
import com.example.mcproject.ui.theme.Bold
import com.example.mcproject.ui.theme.ExtraBold
import com.example.mcproject.ui.theme.HeaderUnselected
import com.example.mcproject.ui.theme.MCProjectTheme
import com.example.mcproject.ui.theme.MediumItalic
import com.example.mcproject.ui.theme.Primary

class DownloadedNewsActivity : ComponentActivity() {
    private lateinit var DBviewModel: DatabaseViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        DBviewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]
//        DBviewModel.clearAllData()

        setContent {
            MCProjectTheme {
                val items = listOf(
                    BottomNavigationItem(
                        title = "",
                        selectedIcon = painterResource(id = R.drawable.news_selected),
                        unselectedIcon = painterResource(id = R.drawable.news_unselected),
                        hasNews = false
                    ),
                    BottomNavigationItem(
                        title = "",
                        selectedIcon = painterResource(id = R.drawable.download_selected),
                        unselectedIcon = painterResource(id = R.drawable.download_unselected),
                        hasNews = false
                    )
                )
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(1)
                }
                val context = LocalContext.current

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    alwaysShowLabel = false,
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                        when (index) {
                                            0 -> {
                                                val intent = Intent(context, HomeActivity::class.java)
                                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                                context.startActivity(intent)
                                            }

                                            1 -> {
                                                // Do nothing when Downloads item is clicked
                                            }
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            painter = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                            contentDescription = item.title,
                                            tint = if (selectedItemIndex == index) Primary else Color.Gray

                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                    },
                                    label = {
                                        Text(
                                            text = item.title,
                                            color = if (selectedItemIndex == index) Primary else Color.Gray,
                                            modifier=Modifier.padding(16.dp)
                                        )
                                    }
                                )
                            }

                        }
                    }
                ) {
                    DownloadScreen(DBviewModel)
                }

            }
        }
    }
}

@Composable
fun DownloadScreen(DBviewModel:DatabaseViewModel) {
    val headlines by DBviewModel.allData.observeAsState(initial = emptyList())
    val articles = headlines
    Log.d("LENGTH_Downloads","${articles.size}")
    val categories = listOf("general","business", "entertainment",  "health", "science", "sports", "technology")
    var selectedCategory by remember { mutableStateOf("general") }
    var searchQuery by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
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
//                                    DBviewModel.updateCategory(selectedCategory)
                                           },
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
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 24.dp, vertical = 14.dp)
//                    .background(shape = RoundedCornerShape(14.dp), color = Color.White)
//                    .border(2.dp, Color.LightGray, shape = RoundedCornerShape(14.dp))
//            ) {
//                TextField(
//                    value = searchQuery,
//                    placeholder = { Text("Search") },
//                    onValueChange = { searchQuery = it },
//                    colors = TextFieldDefaults.colors(
//                        focusedTextColor = Color.Black,
//                        unfocusedTextColor = Color.Black,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    ),
//
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                        .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
//                        .background(color = Color.White)
//                )
//
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "Search Button",
//                    tint = Color.White,
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(topEnd = 14.dp, bottomEnd = 14.dp))
//                        .background(Primary)
//                        .padding(5.dp)
//                        .size(48.dp)
//                        .clickable {
//                            //when search clicked what should be done
//                            //viewModel.updateCategory(searchQuery)
//                        }
//                )
//
//            }
            val context = LocalContext.current
            LazyColumn(contentPadding = PaddingValues(14.dp)) {
                items(articles) { article ->
                    NewsCard2(article, context, "downloaded")
                }
            }
        }
    }



}

@Composable
fun NewsCard2(news: NewsData?, context: Context, mode:String="downloaded"){
    var article:Article?=Article(
        source= Source(id="",name=""),
        author="",
        title="",
        description="",
        url="",
        urlToImage="",
        publishedAt="",
        content=""
    )
    if (news != null) {
        article=Article(
            source= Source(id="",name=news.source?:""),
            author=news.author,
            title=news.title?:"",
            description="",
            url="",
            urlToImage="",
            publishedAt=news.publishedAt,
            content=news.description
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .background(color = Color.White, shape = RoundedCornerShape(14.dp))
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(14.dp))
            .clickable {
                val intent = Intent(context, ContentScreenActivity::class.java);
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
