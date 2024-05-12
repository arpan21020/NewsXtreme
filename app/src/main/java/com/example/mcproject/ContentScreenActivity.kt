package com.example.mcproject
import java.text.SimpleDateFormat
import java.util.*
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.mcproject.database.NewsDatabase
import com.example.mcproject.ui.theme.BackgroundColor
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
        var mode= intent.getStringExtra("mode")
        Log.d("mode",mode.toString())
        setContent {
            if (mode != null) {
                mode="online"
            }
            mode?.let { NewsContent(article, it, this) }
        }
    }

    @Composable
    fun NewsContent(article: Article, mode:String = "online", contentScreenActivity: ComponentActivity) {
        Box(
            modifier = Modifier
                .background(Color.White)
        ) {


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
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .background(BackgroundColor)
                    ) {
                        item {
                            article.publishedAt?.let {
                                convertDateString(it)?.let { it1 ->
                                    Text(
                                        text = it1,
                                        fontSize = 16.sp,
                                        color = Primary,
                                        fontFamily = SemiBoldItalic,
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 8.dp
                                        )
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
                                    url = it,
                                    mode = mode
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
                                Text(
                                    text = "$it \n\n\n\n\n",
                                    fontSize = 16.sp,
                                    fontFamily = Medium,
                                    lineHeight = 32.sp,
                                    modifier = Modifier
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }

            }
            Icon(
                imageVector = Icons.Default.AddCircle,
                tint = Primary,

                contentDescription = "Add the article",
                modifier = Modifier
                    .size(164.dp)
                    .clip(RoundedCornerShape(50)) // This will make the shape circular
                    .padding(48.dp)
                    .border(8.dp, Primary, RoundedCornerShape(50)) // Add a red border
                    .background(Color.White, RoundedCornerShape(50))
                    .align(Alignment.BottomEnd)
                    .clickable {
                        // Add the article to the database
//                        val db = NewsDatabase.getInstance(contentScreenActivity)
//                        db.articleDao().insert(article)
                    }



            )

        }

    }


}

@Composable
fun DisplayImageFromUrl(url: String, mode: String = "online") {
    if (mode == "online"){
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
                .size(250.dp, 230.dp)
                .padding(16.dp)
        )
    }
    else{
        // Load the image from the local storage
    }
}


fun convertDateString(input: String): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val date = inputFormat.parse(input)

    val outputFormat = SimpleDateFormat("h:mm a, dd MMM, yyyy", Locale.US)
    outputFormat.timeZone = TimeZone.getDefault()

    return date?.let { outputFormat.format(it) }
}

