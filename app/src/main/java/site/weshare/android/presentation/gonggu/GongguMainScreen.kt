package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/**
 * 공동구매 메인 화면
 * GongguHeaderSection()과 GongguProductListBody() 컴포넌트들을 호출
 */
@Composable
fun GongguMainScreen(
    onItemClick: (GongguItem) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onMenuAction: (GongguItem, String) -> Unit = { _, _ -> }
) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 헤더 (첫 번째 파일의 GongguHeaderSection 컴포넌트)
        GongguHeaderSection(
            currentLocation = headerUiState.currentLocation,
            showLocationDialog = headerUiState.showLocationDialog,
            appliedFilters = headerUiState.appliedFilters,
            onLocationClick = {
                if (headerUiState.showLocationDialog) {
                    headerViewModel.hideLocationDialog()
                } else {
                    headerViewModel.showLocationDialog()
                }
            },
            onSearchClick = onSearchClick,
            onFavoriteClick = onFavoriteClick,
            onNotificationClick = onNotificationClick,
            onFilterClick = { filterType ->
                headerViewModel.updateFilter(filterType)
            }
        )

        // 바디 (두 번째 파일의 GongguProductListBody 컴포넌트)
        GongguProductListBody(
            onItemClick = onItemClick,
            onLikeClick = { itemId ->
                // 좋아요 처리 로직
                println("Like clicked for item: $itemId")
            },
            onRegisterClick = onRegisterClick,
            onMenuAction = onMenuAction
        )
    }
}

// ==================== Preview ====================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GongguMainScreenPreview() {
    MaterialTheme {
        GongguMainScreen(
            onItemClick = { item ->
                println("Item clicked: ${item.title}")
            },
            onSearchClick = {
                println("Search clicked")
            },
            onFavoriteClick = {
                println("Favorite clicked")
            },
            onNotificationClick = {
                println("Notification clicked")
            },
            onRegisterClick = {
                println("Register clicked")
            },
            onMenuAction = { item, action ->
                println("Menu action: $action for item: ${item.title}")
            }
        )
    }
}