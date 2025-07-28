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
import site.weshare.android.presentation.sign.EmailInputScreen
import site.weshare.android.presentation.sign.VerificationCodeScreen
import site.weshare.android.presentation.sign.login.LoginScreen
import site.weshare.android.presentation.sign.login.NaverLoginWebViewScreen
import site.weshare.android.presentation.splash.SplashScreen
import site.weshare.android.ui.theme.KachiAndroidTheme

class MainActivity : ComponentActivity() {

//    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KachiAndroidTheme {

            }
        }
    }
}
