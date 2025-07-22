package site.weshare.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import site.weshare.android.presentation.sign.EmailInputScreen
import site.weshare.android.presentation.sign.VerificationCodeScreen
import site.weshare.android.presentation.sign.login.LoginScreen
import site.weshare.android.presentation.splash.SplashScreen
import site.weshare.android.ui.theme.KachiAndroidTheme
import site.weshare.android.util.saveAccessToken
import site.weshare.android.util.saveRefreshToken

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 시스템 UI(상단바, 하단바)와 겹치지 않도록 Edge-to-Edge 설정
        enableEdgeToEdge()

        setContent {
            KachiAndroidTheme {
                navController = rememberNavController()

                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(navToLogin = { navController.navigate("login") })
                        }
                        composable("login") {
                            LoginScreen()
                        }
                        composable("email_input") {
                            EmailInputScreen(
                                onNext = { email ->
                                    navController.navigate("verification_code?email=$email") // ✨ 이메일을 쿼리 파라미터로 전달
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("verification_code?email={email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""

                            VerificationCodeScreen(
                                email = email, // 💡 전달
                                onNext = { /* 다음 화면으로 */ },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
            // ✨ 앱에 URI 스킴으로 진입한 경우 토큰 저장 + 이메일 인증 화면 이동
            handleLoginCallback(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleLoginCallback(intent)
    }

    // ✨ 로그인 콜백 처리 함수
    private fun handleLoginCallback(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data != null && data.scheme == "weshare" && data.host == "callback") {
            val accessToken = data.getQueryParameter("accessToken")
            val refreshToken = data.getQueryParameter("refreshToken")

            if (!accessToken.isNullOrBlank()) {
                saveAccessToken(this, accessToken)
            }
            if (!refreshToken.isNullOrBlank()) {
                saveRefreshToken(this, refreshToken)
            }

            // ✨ 이메일 인증 화면으로 이동
            if (::navController.isInitialized) {
                navController.navigate("email_input")
            }
        }
    }
}


        // 앱의 루트 Compose UI 설정
//        setContent {
//            KachiAndroidTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // 간단한 텍스트 컴포넌트 (UI 확인용)
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
//
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    // 테마가 적용된 프리뷰
    KachiAndroidTheme {
        Greeting("Android")
    }
}