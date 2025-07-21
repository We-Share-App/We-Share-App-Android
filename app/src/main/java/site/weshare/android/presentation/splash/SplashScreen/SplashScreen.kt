package site.weshare.android.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import site.weshare.android.R

@Composable
fun SplashScreen(navToLogin: () -> Unit) {
    // 일정 시간 후 로그인 화면으로 이동
    LaunchedEffect(Unit) {
        delay(2000L) // 2초 대기
        navToLogin()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // 배경 흰색으로 일단 설정함
        contentAlignment = Alignment.Center
    ) {
        // 중앙 이미지 (텍스트 포함된 이미지)
        Image(
            painter = painterResource(id = R.drawable.splash_kachi),
            contentDescription = "까치 스플래쉬 이미지",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        )
    }
}