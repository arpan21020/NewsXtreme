package com.example.mcproject

import java.text.SimpleDateFormat
import java.util.*
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import coil.compose.SubcomposeAsyncImage
import com.example.mcproject.NewsViewModel.DatabaseViewModel
import com.example.mcproject.api.Article
import com.example.mcproject.database.NewsData
import com.example.mcproject.ui.theme.BackgroundColor
import com.example.mcproject.ui.theme.Bold
import com.example.mcproject.ui.theme.ExtraBold
import com.example.mcproject.ui.theme.Medium
import com.example.mcproject.ui.theme.Primary
import com.example.mcproject.ui.theme.SemiBoldItalic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class ContentScreenActivity : ComponentActivity() {
        private lateinit var DBviewModel: DatabaseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        DBviewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]

        val article = intent.getSerializableExtra("article") as Article
        var mode = intent.getStringExtra("mode")

        var text: MutableState<String?> = mutableStateOf("Loading...")
        if(mode=="downloaded"){
            text.value=article.content
        }
        if(mode=="online"){

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val doc = Jsoup.connect(article.url).get()
                        text.value = doc.text()
                    } catch (e: Exception) {
                        text.value = "Some problem occurred! \n The content can't be displayed now."
                        Log.d("NO_CONTENT", "${e}")
                        e.printStackTrace()
                    } finally {
                        if (text.value == null) {
                            text.value = "Null firstParagraph"
                        }
                    }
                }
            }
        }

            Log.d("mode", mode.toString())
            setContent {
                if(mode=="downloaded"){
                    mode?.let { NewsContent(DBviewModel,article, mode!!, text, this) }
                }
                else{

                    if (mode != null) {
                        mode = "online"
                    }
                    mode?.let { NewsContent(DBviewModel,article, it, text, this) }
                }
            }
        }

    @Composable
    fun NewsContent(DBviewModel:DatabaseViewModel,article: Article, mode:String = "online", text: MutableState<String?>, contentScreenActivity: ComponentActivity) {
        val showDialog = remember{ mutableStateOf(false) }

        Box(
            modifier = Modifier
                .background(Color.White)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)

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
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                            .background(BackgroundColor)
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 1.dp)
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
                                    text = text.value!!,
                                    //text = "$it \n\n\n\n\n",
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
            if(mode == "online"){

                Icon(
                    painter = painterResource(id = R.drawable.download_selected),
                    tint = Primary,

                    contentDescription = "Add the article",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(48.dp)
                        .align(Alignment.BottomEnd)
                        .clickable {
                            showDialog.value=true;
                            DBviewModel.upsert(
                                NewsData(
                                    source = article.source.name,
                                    author = article.author,
                                    title = article.title,
                                    description = text.value,
                                    image = article.urlToImage,
                                    publishedAt = article.publishedAt,
                                )
                            )
                            // Add the article to the database
//                        val db = NewsDatabase.getInstance(contentScreenActivity)
//                        db.articleDao().insert(article)
                        }



                )
            }
            if (showDialog.value){
                AlertDialog(

                    title = {
                        Text(text = "news Xtreme")
                    },
                    text = {
                        Text(text = "Article saved to Downloads")
                    },
                    onDismissRequest = {
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog.value=false
                            }
                        ) {
                            Text("Confirm")
                        }
                    },

                )
            }

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
            error = {
                Image(
                    painter = painterResource(id = R.drawable.error__image),
                    contentDescription = "Error image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .size(400.dp, 230.dp)
                .padding(8.dp)
        )
    }
    else{
        Image(
            painter = painterResource(id = R.drawable.error__image),
            contentDescription = "Error image",
                    modifier = Modifier
                        .size(400.dp, 230.dp)
                        .padding(8.dp)
        )
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