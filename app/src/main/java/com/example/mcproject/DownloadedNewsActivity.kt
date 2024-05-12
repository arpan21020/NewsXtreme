package com.example.mcproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.mcproject.NewsViewModel.DatabaseViewModel
import com.example.mcproject.NewsViewModel.NewsViewModel
import com.example.mcproject.api.Article
import com.example.mcproject.ui.theme.BackgroundColor
import com.example.mcproject.ui.theme.ExtraBold
import com.example.mcproject.ui.theme.HeaderUnselected
import com.example.mcproject.ui.theme.MCProjectTheme
import com.example.mcproject.ui.theme.Primary


class DownloadedNewsActivity : ComponentActivity() {
    private lateinit var viewModel: NewsViewModel

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelProvider(this)[DatabaseViewModel::class.java]
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        val headlines = viewModel.topHeadlines.value
        val articles = headlines.articles
        setContent {
            MCProjectTheme {
                val items = listOf(
                    BottomNavigationItem(
                        title = "Home",
                        selectedIcon = Icons.Filled.Email,
                        unselectedIcon = Icons.Outlined.Email,
                        hasNews = false
                    ),
                    BottomNavigationItem(
                        title = "Downloads",
                        selectedIcon = Icons.Filled.Favorite,
                        unselectedIcon = Icons.Outlined.Favorite,
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
                                            imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
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
                    DownloadScreen(articles, this)
                }

            }
        }
    }
}

@Composable
fun DownloadScreen(articles: List<Article>, downloadScreenActivity: DownloadedNewsActivity) {

    val categories = listOf("business", "entertainment", "general", "health", "science", "sports", "technology")
    var selectedCategory by remember { mutableStateOf("general") }
    var searchQuery by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back Button",
                tint = Color.Black,
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .size(36.dp)
                    .clickable {
                        downloadScreenActivity.finish()
                    }
            )

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
                                .clickable { selectedCategory = category },
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
                            //viewModel.updateCategory(searchQuery)
                        }
                )

            }
            val context = LocalContext.current
            LazyColumn(contentPadding = PaddingValues(14.dp)) {
                items(articles) { article ->
                    NewsCard(article, context, "downloaded")
                }
            }
        }
    }
}

