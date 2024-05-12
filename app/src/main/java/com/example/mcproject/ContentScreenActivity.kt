package com.example.mcproject
import java.text.SimpleDateFormat
import java.util.*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.offline.Download
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.mcproject.api.Article
import com.example.mcproject.ui.theme.Bold
import com.example.mcproject.ui.theme.ExtraBold
import com.example.mcproject.ui.theme.Medium
import com.example.mcproject.ui.theme.MediumItalic
import com.example.mcproject.ui.theme.Primary
import com.example.mcproject.ui.theme.SemiBold
import com.example.mcproject.ui.theme.SemiBoldItalic

class ContentScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val article = intent.getSerializableExtra("article") as Article
        setContent {
            NewsContent(article, this)
        }
    }

    @Composable
    fun NewsContent(article: Article, contentScreenActivity: ComponentActivity) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)

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
                            contentScreenActivity.finish()
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
            Column {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    item {
                        article.publishedAt?.let {
                            convertDateString(it)?.let { it1 ->
                                Text(
                                    text = it1,
                                    fontSize = 16.sp,
                                    color = Primary,
                                    fontFamily = SemiBoldItalic,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = article.title,
                            fontSize = 20.sp,
                            lineHeight = 32.sp,
                            fontFamily = Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        article.urlToImage?.let {
                            DisplayImageFromUrl(
                                url = it
                            )
                        }
                    }
                    item {
                        article.author?.let {
                            Text(
                                text = it,
                                fontSize = 12.sp,
                                color = Primary,
                                textAlign = TextAlign.Right,
                                fontFamily = SemiBoldItalic,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
//                    item {
//                        article.description?.let {
//                            Text(text = it,
//                                fontSize = 16.sp,
//                                fontFamily = MediumItalic,
//                                modifier = Modifier
//                                    .padding(16.dp))
//                        }
//                    }
                    item {
                        article.content?.let {
                            Text(text = it,
                                fontSize = 16.sp,
                                fontFamily = Medium,
                                lineHeight = 32.sp,
                                modifier = Modifier
                                    .padding(16.dp))
                        }
                    }
                }
            }
//            Icon(
//                imageVector = Icons.Default.Add,
//                tint = Primary,
//                contentDescription = "Download the article",
//                modifier = Modifier
//                    .size(36.dp)
//            )
        }

    }


}

@Composable
fun DisplayImageFromUrl(url: String) {
    SubcomposeAsyncImage(
        model = url,
        contentDescription = null,
        loading = {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .size(250.dp)
            .padding(16.dp)
    )
}


fun convertDateString(input: String): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = inputFormat.parse(input)

    val outputFormat = SimpleDateFormat("h:mm a, dd MMM, yyyy", Locale.US)
    outputFormat.timeZone = TimeZone.getDefault()

    return date?.let { outputFormat.format(it) }
}

