package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.registeroverlay.RegisterOverlay

/**
 * 공동구매 메인 화면 - 완전히 독립적으로 동작
 * MainActivity에서 파라미터 없이 호출 가능
 */
@Composable
fun GongguMainScreen(
    onItemClick: (GongguItem) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onMenuAction: (GongguItem, String) -> Unit = { _, _ -> },
    onFilterClick: () -> Unit = {},
    onLocationClick: () -> Unit = {}
) {
    // 🔥 내부에서 모든 상태 관리
    var showFilterDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showRegisterOverlay by remember { mutableStateOf(false) }

    // 내부 네비게이션 컨트롤러
    val navController = rememberNavController()

    // 🔥 현재 네비게이션 상태 확인
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 헤더용 ViewModel (필터 상태 관리용)
    val headerViewModel = remember {
        GongguViewModel(
            repository = GongguRepository(
                apiService = null,
                getMockData = { emptyList() }
            )
        )
    }

    val headerUiState by headerViewModel.uiState.collectAsState()

    // 🔥 Box로 감싸서 FAB 버튼 배치
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = "gonggu_main"
        ) {
            // 메인 화면
            composable("gonggu_main") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    // 헤더
                    GongguHeaderSection(
                        currentLocation = headerUiState.currentLocation,
                        showLocationDialog = headerUiState.showLocationDialog,
                        appliedFilters = headerUiState.appliedFilters,
                        onLocationClick = {
                            // ViewModel 로직 + 다이얼로그 표시
                            if (headerUiState.showLocationDialog) {
                                headerViewModel.hideLocationDialog()
                            } else {
                                headerViewModel.showLocationDialog()
                            }
                            showLocationDialog = true
                        },
                        onSearchClick = onSearchClick,
                        onFavoriteClick = onFavoriteClick,
                        onNotificationClick = onNotificationClick,
                        onFilterClick = { filterType ->
                            // ViewModel 로직 + 다이얼로그 표시
                            headerViewModel.updateFilter(filterType)
                            showFilterDialog = true
                        }
                    )

                    // 바디
                    GongguProductListBody(
                        onItemClick = { item ->
                            // 🔥 내부 네비게이션으로 상세 화면 이동 (수정됨)
                            navController.navigate("gonggu_detail/${item.id}")
                        },
                        onLikeClick = { itemId ->
                            println("Like clicked for item: $itemId")
                        },
                        onRegisterClick = {
                            // 🔥 여기서 showRegisterOverlay 상태 변경
                            showRegisterOverlay = true
                        },
                        onMenuAction = onMenuAction
                    )
                }

                // 🔥 다이얼로그들을 여기서 직접 처리
                // FilterDialog
                if (showFilterDialog) {
                    var filterSettings by remember { mutableStateOf(FilterSettings()) }

                    FilterDialog(
                        filterSettings = filterSettings,
                        onFilterSettingsChange = { filterSettings = it },
                        onApply = {
                            println("Filter applied: $filterSettings")
                            showFilterDialog = false
                        },
                        onDismiss = {
                            showFilterDialog = false
                        }
                    )
                }

                // LocationSelectionDialog
                if (showLocationDialog) {
                    LocationSelectionDialog(
                        onLocationSelected = { location ->
                            println("Location selected: ${location.name}")
                            showLocationDialog = false
                        },
                        onRegionSettingClick = {
                            println("Navigate to region setting")
                            showLocationDialog = false
                        },
                        onDismiss = {
                            showLocationDialog = false
                        }
                    )
                }

                // RegisterOverlay - 🔥 공동구매 등록하기 버튼 클릭 시 RegisterDetailScreen으로 이동하도록 수정
                if (showRegisterOverlay) {
                    RegisterOverlay(
                        onRegisterClick = {
                            // 🔥 RegisterDetailScreen으로 네비게이션
                            navController.navigate("register_detail")
                            showRegisterOverlay = false
                        },
                        onDismiss = {
                            showRegisterOverlay = false
                        }
                    )
                }
            }

            // 🔥 상세 화면도 내부에서 처리 (수정됨)
            composable("gonggu_detail/{itemId}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")?.toIntOrNull() ?: 0

                GongguDetailScreenContainer(
                    itemId = itemId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onParticipateClick = { quantity ->
                        println("Participate with quantity: $quantity")
                        // 참여 완료 후 메인 화면으로 돌아가기
                        navController.popBackStack()
                    }
                )
            }

            // 🔥 RegisterDetailScreen 네비게이션 추가
            composable("register_detail") {
                RegisterDetailScreen()
            }
        }

        // 🔥 메인 화면이고 RegisterOverlay가 표시되지 않을 때만 등록하기 FAB 표시
        if (currentRoute == "gonggu_main" && !showRegisterOverlay) {
            FloatingActionButton(
                onClick = { showRegisterOverlay = true },
                containerColor = Color(0xFF2FB475),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 35.dp, bottom = 100.dp)
                    .size(width = 100.dp, height = 45.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "등록하기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ==================== Preview ====================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GongguMainScreenPreview() {
    MaterialTheme {
        GongguMainScreen()
    }
}