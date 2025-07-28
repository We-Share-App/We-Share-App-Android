package site.weshare.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import site.weshare.android.presentation.gonggu.GongguMainScreen
import site.weshare.android.presentation.home.HomeScreen
import site.weshare.android.presentation.sign.EmailInputScreen
import site.weshare.android.presentation.sign.VerificationCodeScreen
import site.weshare.android.presentation.sign.login.LoginScreen
import site.weshare.android.presentation.sign.login.NaverLoginWebViewScreen
import site.weshare.android.presentation.splash.SplashScreen
import site.weshare.android.ui.theme.KachiAndroidTheme

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                            LoginScreen(
                                navToWebLogin = { navController.navigate("web_login") }
                            )
                        }
                        composable("web_login") {
                            NaverLoginWebViewScreen(
                                context = this@MainActivity,
                                onLoginSuccess = {
                                    navController.navigate("email_input") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("email_input") {
                            EmailInputScreen(
                                onNext = { email ->
                                    navController.navigate("verification_code?email=$email")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("verification_code?email={email}") { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email") ?: ""

                            VerificationCodeScreen(
                                email = email,
                                onNext = { /* 다음 화면으로 */ },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("home") {
                            HomeScreen(navController = navController)
                        }
                        composable("gonggu_main") {
                            GongguMainScreen(navController = navController)
                        }
                    }
                }
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