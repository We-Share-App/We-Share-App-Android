package site.weshare.android

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import site.weshare.android.presentation.location.LocationSettingScreen
import site.weshare.android.presentation.sign.EmailInputScreen
import site.weshare.android.presentation.sign.NicknameInputScreen
import site.weshare.android.presentation.sign.VerificationCodeScreen
import site.weshare.android.presentation.sign.login.LoginScreen
import site.weshare.android.presentation.sign.login.NaverLoginWebViewScreen
import site.weshare.android.presentation.splash.SplashScreen
import site.weshare.android.ui.theme.KachiAndroidTheme

class SplashActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // SharedPreferences 초기화 (로그인 상태 확인용)
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        setContent {
            KachiAndroidTheme {
                navController = rememberNavController()

                Surface(color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(
                                navToLogin = {
                                    // 로그인 화면으로 이동하는 Navigation Component 동작
                                    navController.navigate("login")
                                },
                                // MainActivity로 이동하는 콜백 추가
                                navToMain = {
                                    // MainActivity로 이동하면서 현재 SplashActivity 종료
                                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                                    finish()
                                },
                                // SharedPreferences를 SplashScreen에 전달 (여기서 로그인 상태 확인)
                                isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                navToWebLogin = { navController.navigate("web_login") },
                                onLoginSuccess = { // 로그인 성공 시 MainActivity로 이동하는 콜백 추가
                                    // 로그인 상태 저장
                                    sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                                    // MainActivity로 이동 후 이전 스택 모두 제거
                                    startActivity(Intent(this@SplashActivity, MainActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    })
                                    finish() // SplashActivity 종료
                                }
                            )
                        }
                        composable("web_login") {
                            NaverLoginWebViewScreen(
                                context = this@SplashActivity,
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

//                            VerificationCodeScreen(
//                                email = email,
//                                onNext = { /* 다음 화면 ( 회원가입 완료 및 로그인 처리 후 MainActivity로) */
//                                    sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
//                                    startActivity(Intent(this@SplashActivity, MainActivity::class.java).apply {
//                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                    })
//                                    finish()
//                                },
                            VerificationCodeScreen(
                                email = email,
                                onNext = {
                                    navController.navigate("nickname_input") {
                                        popUpTo("email_input") { inclusive = true } // 이메일 인증 스택 제거
                                    }
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }

//                        composable("nickname_input") {
//                            NicknameInputScreen(
//                                onNicknameSet = {
//                                    sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
//                                    startActivity(Intent(this@SplashActivity, MainActivity::class.java).apply {
//                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                    })
//                                    finish()
//                                }
//                            )
//                        }

                        composable("nickname_input") {
                            NicknameInputScreen(
                                onNicknameSet = {
                                    navController.navigate("location_setting") {
                                        popUpTo("verification_code?email={email}") { inclusive = true } // 이전 인증 스택 제거 (선택)
                                    }
                                }
                            )
                        }

                        composable("location_setting") {
                            LocationSettingScreen(
                                onLocationSet = {
                                    // 위치 설정이 완료되면 MainActivity로 이동
                                    sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                                    startActivity(Intent(this@SplashActivity, MainActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    })
                                    finish()
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}

