////2025 소프트웨어 공모전 팀 까치
//
//package site.weshare.android
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountCircle
//import androidx.compose.material.icons.filled.AddCircle
//import androidx.compose.material.icons.filled.Build
//import androidx.compose.material.icons.filled.CheckCircle
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material3.BottomAppBar
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import site.weshare.android.presentation.Barter2.MainScreen
//import site.weshare.android.presentation.barter.BarterDetailScreen
//import site.weshare.android.presentation.barter.BarterPostRegister
//import site.weshare.android.presentation.barter.BarterScreen
//import site.weshare.android.presentation.chat.ChatScreen
//import site.weshare.android.presentation.gonggu.GongguMainScreen
//import site.weshare.android.presentation.home.HomeScreen
//import site.weshare.android.presentation.mypage.MyPageScreen
//import site.weshare.android.presentation.mypage.EditMyInfoScreen
//import site.weshare.android.presentation.sign.EmailInputScreen
//import site.weshare.android.presentation.sign.VerificationCodeScreen
//import site.weshare.android.presentation.sign.login.LoginScreen
//import site.weshare.android.presentation.sign.login.NaverLoginWebViewScreen
//import site.weshare.android.presentation.splash.SplashScreen
//import site.weshare.android.ui.theme.KachiAndroidTheme
//
//class MainActivity : ComponentActivity() {
//
////    private lateinit var navController: NavHostController
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        enableEdgeToEdge()
//
//        setContent {
//            KachiAndroidTheme {
//                AppMain()
//            }
//        }
//    }
//}
//
//@Composable
//fun AppMain(){
//    val navController = rememberNavController()
//
//    val navItems = listOf(
//        NavigationItem("홈", Icons.Default.Home, "tab1"),
//        NavigationItem("공동구매", Icons.Default.AddCircle, "tab2"),
//        NavigationItem("물품교환", Icons.Default.Build, "tab3"),
//        NavigationItem("채팅", Icons.Default.CheckCircle, "tab4"),
//        NavigationItem("마이페이지", Icons.Default.AccountCircle, "tab5")
//    )
//
//    Scaffold(
//        bottomBar = {
//
//            BottomAppBar {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceAround
//                ) {
//
//                    navItems.forEach { nav ->
//
//                        // 현재 route
//                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//
//                        Column(
//                            verticalArrangement = Arrangement.Center,
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier.clickable {
//                                navController.navigate(nav.route)
//                            }
//                        ) {
//
//                            // Ture -> 현재 클릭된 탭
//                            // Flase -> 현재 클릭되지 않은 탭
//                            val isCurrentRoute = nav.route == currentRoute
//
//                            Icon(
//                                imageVector = nav.icon,
//                                contentDescription = nav.name,
//                                tint = if (isCurrentRoute) Color.Green else Color.Black
//                            )
//                            Text(
//                                text = nav.name,
//                                color = if (isCurrentRoute) Color.Green else Color.Black
//                            )
//                        }
//
//                    }
//                }
//            }
//
//
//        }
//    ) { paddingValues ->
//
//        NavHost(
//            navController = navController,
//            startDestination = navItems.first().route,
//            modifier = Modifier.padding(paddingValues)
//        ){
//            composable("tab1") {
//                HomeScreen()
//            }
//            composable("tab2") {
//                GongguMainScreen()
//            }
//            composable("tab3") {
////                BarterScreen()
//                MainScreen()
////                BarterDetailScreen(productId = "sample_product_id")
////                ExchangeProposalScreen()
//
//            }
//
//            composable("tab4") {
//                ChatScreen()
//            }
//            composable("tab5") {
//                MyPageScreen(navController)   // 파라미터 꼭 넘겨야 함
//            }
//
//            // ✅ 프로필 수정 화면
//            composable("edit_profile") {
//                EditMyInfoScreen(navController)
//            }
//
//
//        }
//
//    }
//
//
//
//}
//
//
//data class NavigationItem(
//    val name : String,
//    val icon : ImageVector,
//    val route : String
//)


//2025 소프트웨어 공모전 팀 까치

package site.weshare.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import site.weshare.android.presentation.Barter2.MainScreen
import site.weshare.android.presentation.chat.ChatScreen
import site.weshare.android.presentation.gonggu.GongguMainScreen
import site.weshare.android.presentation.home.HomeScreen
import site.weshare.android.presentation.mypage.EditMyInfoScreen
import site.weshare.android.presentation.mypage.MyPageScreen
import site.weshare.android.ui.theme.KachiAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { KachiAndroidTheme { AppMain() } }
    }
}

object BarterRoutes {
    const val RegisterOpen = "barter/openRegister"       // 오픈 레지스터
    const val RegisterPrivate = "barter/privateRegister" // 프라이빗 레지스터
}


// ✅ 추가
object GongguRoutes {
    const val RegisterDetail = "gonggu/registerDetail"
}


@Composable
fun AppMain() {
    val navController = rememberNavController()

    val navItems = listOf(
        NavigationItem("홈", Icons.Default.Home, "tab1"),
        NavigationItem("공동구매", Icons.Default.AddCircle, "tab2"),
        NavigationItem("물품교환", Icons.Default.Build, "tab3"),
        NavigationItem("채팅", Icons.Default.CheckCircle, "tab4"),
        NavigationItem("마이페이지", Icons.Default.AccountCircle, "tab5")
    )

    Scaffold(
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                    navItems.forEach { nav ->
                        val isSelected = nav.route == currentRoute
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                navController.navigate(nav.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        ) {
                            Icon(
                                imageVector = nav.icon,
                                contentDescription = nav.name,
                                tint = if (isSelected) Color.Green else Color.Black
                            )
                            Text(text = nav.name, color = if (isSelected) Color.Green else Color.Black)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = navItems.first().route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("tab1") { HomeScreen() }
//            composable("tab2") { GongguMainScreen() }
            // ✅ tab2에서 등록 화면으로 이동 콜백 전달
            composable("tab2") {
                GongguMainScreen(
                    onRegisterClick = { navController.navigate(GongguRoutes.RegisterDetail) }
                )
            }

            composable("tab3") {
                // MainScreen 쪽으로 콜백 전달
                MainScreen(
                    onOpenRegister = { navController.navigate(BarterRoutes.RegisterOpen) },
                    onPrivateRegister = { navController.navigate(BarterRoutes.RegisterPrivate) }
                )
            }
            composable("tab4") { ChatScreen() }
            composable("tab5") { MyPageScreen(navController) }

            // 서브 라우트
            composable("edit_profile") { EditMyInfoScreen(navController) }

            // 오픈 레지스터
            composable(BarterRoutes.RegisterOpen) {
                site.weshare.android.presentation.Barter2.ProductRegistrationScreen(
                    onCloseClick = { navController.popBackStack() },
                    onSubmitClick = {
                        navController.navigate("tab3") {
                            popUpTo("tab3") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }

            // 프라이빗 레지스터
            composable(BarterRoutes.RegisterPrivate) {
                site.weshare.android.presentation.Barter2.RegistrationScreen(
                    onCloseClick = { navController.popBackStack() },
                    onSubmitClick = {
                        navController.navigate("tab3") {
                            popUpTo("tab3") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }


            // ✅ 공구 등록 화면 라우트 추가 (등록/닫기 시 tab2로 복귀)
            composable(GongguRoutes.RegisterDetail) {
                site.weshare.android.presentation.gonggu.RegisterDetailScreen(
                    onCloseClick = { navController.popBackStack() },
                    onSubmitClick = {
                        navController.navigate("tab2") {
                            popUpTo("tab2") { inclusive = false }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

data class NavigationItem(
    val name: String,
    val icon: ImageVector,
    val route: String
)