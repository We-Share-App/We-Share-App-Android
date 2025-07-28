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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import site.weshare.android.presentation.barter.BarterScreen
import site.weshare.android.presentation.gonggu.GongguMainScreen
import site.weshare.android.presentation.home.HomeScreen
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
                AppMain()
            }
        }
    }
}

@Composable
fun AppMain(){
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

                    navItems.forEach { nav ->

                        // 현재 route
                        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                navController.navigate(nav.route)
                            }
                        ) {

                            // Ture -> 현재 클릭된 탭
                            // Flase -> 현재 클릭되지 않은 탭
                            val isCurrentRoute = nav.route == currentRoute

                            Icon(
                                imageVector = nav.icon,
                                contentDescription = nav.name,
                                tint = if (isCurrentRoute) Color.Green else Color.Black
                            )
                            Text(
                                text = nav.name,
                                color = if (isCurrentRoute) Color.Green else Color.Black
                            )
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
        ){
            composable("tab1") {
                HomeScreen()
            }
            composable("tab2") {
                GongguMainScreen()
            }
            composable("tab3") {
                BarterScreen()
            }
//            composable("tab4") {
//                ChatScreen()
//            }
//            composable("tab5"){
//                MyPageScreen()
//            }


        }

    }



}


data class NavigationItem(
    val name : String,
    val icon : ImageVector,
    val route : String
)
