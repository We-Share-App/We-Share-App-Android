package site.weshare.android

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import site.weshare.android.presentation.sign.login.LoginScreen
import site.weshare.android.presentation.splash.SplashScreen
import site.weshare.android.ui.theme.KachiAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 시스템 UI(상단바, 하단바)와 겹치지 않도록 Edge-to-Edge 설정
        enableEdgeToEdge()

        setContent {
            KachiAndroidTheme {
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(navToLogin = { navController.navigate("login") })
                        }
                        composable("login") {
                            LoginScreen()
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


//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    // 간단한 텍스트 컴포넌트 (UI 확인용)
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    // 테마가 적용된 프리뷰
//    KachiAndroidTheme {
//        Greeting("Android")
//    }
//
//}