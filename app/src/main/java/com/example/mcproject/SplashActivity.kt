package com.example.mcproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mcproject.ui.theme.Bold
import com.example.mcproject.ui.theme.ExtraBold
import com.example.mcproject.ui.theme.Primary
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplashScreen()
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun SplashScreen() {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val offset by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = -1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 5000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
                    .padding(48.dp)
                    .padding(bottom = 154.dp)
            ) {
                Text(
                    text = "news",
                    fontSize = 72.sp,
                    fontFamily = ExtraBold,
                    color = Primary,
                    modifier = Modifier.offset(x = Dp(offset * 200))
                )
                Text(
                    text = "xtreme",
                    fontSize = 72.sp,
                    fontFamily = ExtraBold,
                    modifier = Modifier.offset(x = Dp(offset * 200))
                )
            }

            LaunchedEffect(Unit) {
                delay(24000)
                val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
                mainIntent.putExtra("FROM_SPLASH", true)
                startActivity(mainIntent)
                finish()
            }
        }
    }
}