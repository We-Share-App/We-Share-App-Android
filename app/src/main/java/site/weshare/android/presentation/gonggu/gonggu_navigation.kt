package site.weshare.android.presentation.gonggu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import site.weshare.android.presentation.gonggu.GongguDetailScreenContainer
import site.weshare.android.presentation.gonggu.GongguMainScreen
import site.weshare.android.presentation.gonggu.GongguViewModel

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Int) = "detail/$itemId"
    }
}

@Composable
fun GongguNavGraph(
    onRegisterClick: () -> Unit = {}
) {
    // ViewModel 초기화는 Composable 내부에서 remember를 이용해 처리합니다.
    val viewModel: GongguViewModel = remember { GongguViewModel() }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            GongguMainScreen(
                viewModel = viewModel,
                onItemClick = { item ->
                    navController.navigate(Screen.Detail.createRoute(item.id))
                },
                onRegisterClick = onRegisterClick
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            GongguDetailScreenContainer(
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                onParticipateClick = { /* 필요 시 콜백 처리 */ }
            )
        }
    }
}
