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
fun SplashScreen(
    navToLogin: () -> Unit, // 로그인 화면으로 이동하는 콜백
    navToMain: () -> Unit,   // 메인 화면 (MainActivity)으로 이동하는 콜백
    isLoggedIn: Boolean      // 로그인 상태를 받아오는 파라미터 추가
)  {
    // 일정 시간 후 로그인 화면으로 이동
    LaunchedEffect(Unit) {
        delay(1000L) // 1초 대기


        //테스트 용 (실제배포 시 비활성화)
        navToLogin()

        //실제 배포시 아래 코드를 활성화 위의 코드를 비활성화

//        // 로그인 상태에 따라 분기
//        if (isLoggedIn) {
//            navToMain() // 로그인 되어 있으면 MainActivity로 이동
//        } else {
//            navToLogin() // 로그인 되어 있지 않으면 로그인 화면으로 이동
//        }
//    }

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