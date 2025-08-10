package site.weshare.android.presentation.Barter2

import site.weshare.android.presentation.gonggu.GongguItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import site.weshare.android.presentation.gonggu.GongguRepository
import site.weshare.android.presentation.gonggu.GongguViewModel
import site.weshare.android.presentation.gonggu.FilterSettings

/**
 * 메인 화면 - 헤더와 바디를 분리해서 호출하고 오버레이 다이얼로그 표시
 */
@Composable
fun MainScreen(
    onItemClick: (GongguItem) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onMenuAction: (GongguItem, String) -> Unit = { _, _ -> },
    onFilterClick: () -> Unit = {},
    onLocationClick: () -> Unit = {}
) {
    var showFilterDialog by remember { mutableStateOf(false) }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showRegisterOverlay by remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val headerViewModel = remember {
        GongguViewModel(
            repository = GongguRepository(
                apiService = null,
                getMockData = { emptyList() }
            )
        )
    }

    val headerUiState by headerViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // 헤더 호출
            MainHeader(
                onLocationClick = { showLocationDialog = true },
                onSearchClick = onSearchClick,
                onFavoriteClick = onFavoriteClick,
                onNotificationClick = onNotificationClick,
                onFilterClick = { showFilterDialog = true }
            )

            // 바디 호출
            MainBody(
                onItemClick = onItemClick,
                onLikeClick = { itemId ->
                    println("Like clicked for item: $itemId")
                },
                onRegisterClick = { showRegisterOverlay = true },
                onMenuAction = onMenuAction
            )
        }

        // FAB 버튼
        if (!showRegisterOverlay) {
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

        // 위치 선택 다이얼로그 오버레이
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

        // 필터 다이얼로그 오버레이
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

        // 등록하기 오버레이
        if (showRegisterOverlay) {
            RegisterOverlayScreen(
                onParticipateClick = {
                    println("Participate clicked")
                    showRegisterOverlay = false
                },
                onRegisterClick = {
                    println("Register clicked")
                    showRegisterOverlay = false
                },
                onDismiss = {
                    showRegisterOverlay = false
                }
            )
        }
    }
}

// 헤더 함수 - 기존 GongguHeaderSection 호출
@Composable
fun MainHeader(
    onLocationClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onFilterClick: () -> Unit = {}
) {
    GongguHeaderSection(
        onLocationClick = onLocationClick,
        onSearchClick = onSearchClick,
        onFavoriteClick = onFavoriteClick,
        onNotificationClick = onNotificationClick,
        onFilterClick = { onFilterClick() }
    )
}

// 바디 함수 - 기존 GongguProductListBody 호출
@Composable
fun MainBody(
    onItemClick: (GongguItem) -> Unit = {},
    onLikeClick: (Int) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onMenuAction: (GongguItem, String) -> Unit = { _, _ -> }
) {
    GongguProductListBody(
        onItemClick = onItemClick,
        onLikeClick = onLikeClick,
        onRegisterClick = onRegisterClick,
        onMenuAction = onMenuAction
    )
}

// 등록하기 오버레이 화면
@Composable
fun RegisterOverlayScreen(
    onParticipateClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 어두운 배경
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() }
        )

        // 하나의 박스 안에 두 개의 버튼 영역
        Card(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 35.dp, bottom = 160.dp)
                .width(160.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2FB475))
        ) {
            Column {
                // 물품교환 참여하기 버튼
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp)
                        .clickable { onParticipateClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "물품교환 참여하기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // 구분선
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.3f),
                    thickness = 1.dp
                )

                // 물품교환 등록하기 버튼
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp)
                        .clickable { onRegisterClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "물품교환 등록하기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // X 버튼 (FAB 스타일)
        FloatingActionButton(
            onClick = onDismiss,
            containerColor = Color(0xFF2FB475),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 35.dp, bottom = 100.dp)
                .size(width = 45.dp, height = 45.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "닫기",
                modifier = Modifier.size(18.dp),
                tint = Color.White
            )
        }
    }
}

// ==================== Preview ====================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}