package site.weshare.android.presentation.gonggu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==================== 백엔드 연결을 위한 데이터 모델 ====================
data class GongguItemResponse(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val currentCount: Int,
    val maxCount: Int,
    val daysLeft: Int,
    val likes: Int,
    val comments: Int,
    val views: Int,
    val imageUrl: String? = null,
    val tag: String? = null,
    val createdAt: String,
    val location: String,
    val sellerId: Int
)

data class GongguItem(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val currentCount: Int,
    val maxCount: Int,
    val daysLeft: Int,
    val likes: Int,
    val comments: Int,
    val views: Int,
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val tag: String? = null
)

fun GongguItemResponse.toUiModel(): GongguItem {
    return GongguItem(
        id = id,
        title = title,
        description = description,
        price = String.format("%,d원", price),
        currentCount = currentCount,
        maxCount = maxCount,
        daysLeft = daysLeft,
        likes = likes,
        comments = comments,
        views = views,
        imageUrl = imageUrl,
        tag = tag
    )
}

// ==================== API Service & Repository ====================
interface GongguApiService {
    suspend fun getGongguItems(
        location: String,
        sortBy: String = "latest",
        category: String? = null,
        paymentType: String? = null
    ): List<GongguItemResponse>

    suspend fun searchGongguItems(query: String): List<GongguItemResponse>
    suspend fun likeGongguItem(itemId: Int): Boolean
    suspend fun unlikeGongguItem(itemId: Int): Boolean
}

class GongguRepository(
    private val apiService: GongguApiService? = null,
    private val getMockData: () -> List<GongguItem> = { emptyList() }
) {
    suspend fun getGongguItems(
        location: String,
        sortBy: String = "latest",
        category: String? = null,
        paymentType: String? = null
    ): Result<List<GongguItem>> = try {
        if (apiService != null) {
            val resp = apiService.getGongguItems(location, sortBy, category, paymentType)
            Result.success(resp.map { it.toUiModel() })
        } else {
            Result.success(getMockData())
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun searchItems(query: String): Result<List<GongguItem>> = try {
        if (apiService != null) {
            Result.success(apiService.searchGongguItems(query).map { it.toUiModel() })
        } else {
            Result.success(getMockData().filter { it.title.contains(query, true) })
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun toggleLike(itemId: Int, isLiked: Boolean): Result<Boolean> = try {
        if (apiService != null) {
            val result = if (isLiked) apiService.unlikeGongguItem(itemId)
            else apiService.likeGongguItem(itemId)
            Result.success(result)
        } else {
            Result.success(true)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

// ==================== UI State & Enums ====================
data class FilterSettings(
    val minPrice: String = "",
    val maxPrice: String = "",
    val minCount: String = "",
    val maxCount: String = "",
    val selectedCategory: String = ""
)

data class GongguUiState(
    val items: List<GongguItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentLocation: String = "흑석동",
    val selectedFilter: FilterType = FilterType.ALL,
    val sortBy: SortType = SortType.LATEST,
    val totalCount: Int = 0,
    val showLocationDialog: Boolean = false,
    val showFilterDialog: Boolean = false,
    val showRegisterOverlay: Boolean = false,
    val filterSettings: FilterSettings = FilterSettings(),
    val appliedFilters: FilterSettings = FilterSettings()
)

enum class FilterType(val displayName: String, val apiValue: String?) {
    ALL("전체", null),
    DISTANCE("가격", "distance"),
    PAYMENT("개수", "payment"),
    CATEGORY("카테고리", "category")
}

enum class SortType(val displayName: String, val apiValue: String) {
    LATEST("최신순", "latest"),
    POPULARITY("인기순", "popularity"),
    DEADLINE("마감순", "deadline"),
    DISTANCE("거리순", "distance")
}

// ==================== ViewModel ====================
class GongguViewModel(
    private val repository: GongguRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(GongguUiState())
    val uiState: StateFlow<GongguUiState> = _uiState.asStateFlow()

    init {
        loadGongguItems()
    }

    fun loadGongguItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getGongguItems(
                location = _uiState.value.currentLocation,
                sortBy = _uiState.value.sortBy.apiValue,
                category = if (_uiState.value.selectedFilter == FilterType.CATEGORY) "food" else null,
                paymentType = if (_uiState.value.selectedFilter == FilterType.PAYMENT) "card" else null
            ).fold(
                onSuccess = { items ->
                    _uiState.value = _uiState.value.copy(items = items, totalCount = items.size, isLoading = false)
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "Unknown error",
                        isLoading = false
                    )
                }
            )
        }
    }

    fun updateFilter(filter: FilterType) {
        if (filter == FilterType.ALL) {
            _uiState.value = _uiState.value.copy(showFilterDialog = true)
        } else {
            _uiState.value = _uiState.value.copy(selectedFilter = filter)
            loadGongguItems()
        }
    }

    fun showLocationDialog() {
        _uiState.value = _uiState.value.copy(showLocationDialog = true)
    }

    fun hideLocationDialog() {
        _uiState.value = _uiState.value.copy(showLocationDialog = false)
    }

    fun toggleLike(itemId: Int) {
        viewModelScope.launch {
            val currentItems = _uiState.value.items
            val idx = currentItems.indexOfFirst { it.id == itemId }
            if (idx == -1) return@launch
            val item = currentItems[idx]
            repository.toggleLike(itemId, isLiked = false)
            val updated = currentItems.toMutableList().apply { this[idx] = item.copy(likes = item.likes + 1) }
            _uiState.value = _uiState.value.copy(items = updated)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun showRegisterOverlay() {
        _uiState.value = _uiState.value.copy(showRegisterOverlay = true)
    }

    fun hideRegisterOverlay() {
        _uiState.value = _uiState.value.copy(showRegisterOverlay = false)
    }
}

// ==================== Header UI Components ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguHeaderSection(
    currentLocation: String = "흑석동",
    showLocationDialog: Boolean = false,
    appliedFilters: FilterSettings = FilterSettings(),
    onLocationClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onFilterClick: (FilterType) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // 상단 앱바
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onLocationClick() }
                ) {
                    Text(currentLocation, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer {
                                rotationZ = if (showLocationDialog) 180f else 0f
                            }
                    )
                }
            },
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(Icons.Default.Search, contentDescription = "검색")
                }
                IconButton(onClick = onFavoriteClick) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "찜")
                }
                IconButton(onClick = onNotificationClick) {
                    Icon(Icons.Default.Notifications, contentDescription = "알림")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        // 필터 칩들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 전체 버튼
            FilterChip(
                selected = false,
                onClick = { onFilterClick(FilterType.ALL) },
                label = {
                    Text("전체", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = Color.White,
                    labelColor = Color.Black
                )
            )

            // 가격 버튼
            FilterChip(
                selected = appliedFilters.minPrice.isNotEmpty() || appliedFilters.maxPrice.isNotEmpty(),
                onClick = { onFilterClick(FilterType.DISTANCE) },
                label = {
                    Text("가격", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2FB475),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.Black
                )
            )

            // 개수 버튼
            FilterChip(
                selected = appliedFilters.minCount.isNotEmpty() || appliedFilters.maxCount.isNotEmpty(),
                onClick = { onFilterClick(FilterType.PAYMENT) },
                label = {
                    Text("개수", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2FB475),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.Black
                )
            )

            // 카테고리 버튼
            FilterChip(
                selected = appliedFilters.selectedCategory.isNotEmpty(),
                onClick = { onFilterClick(FilterType.CATEGORY) },
                label = {
                    Text("카테고리", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2FB475),
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = Color.Black
                )
            )
        }
    }
}

// ==================== Complete Main Screen (전체 화면) ====================
@Composable
fun GongguCompleteMainScreen(
    viewModel: GongguViewModel = remember {
        GongguViewModel(
            repository = GongguRepository(
                apiService = null,
                getMockData = { emptyList() } // 헤더에는 빈 목록
            )
        )
    },
    onItemClick: (GongguItem) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) viewModel.clearError()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 헤더 섹션
        GongguHeaderSection(
            currentLocation = uiState.currentLocation,
            showLocationDialog = uiState.showLocationDialog,
            appliedFilters = uiState.appliedFilters,
            onLocationClick = {
                if (uiState.showLocationDialog) {
                    viewModel.hideLocationDialog()
                } else {
                    viewModel.showLocationDialog()
                }
            },
            onFilterClick = { filterType ->
                viewModel.updateFilter(filterType)
            }
        )
    }
}

// ==================== Preview ====================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GongguHeaderSectionPreview() {
    MaterialTheme {
        GongguHeaderSection(
            currentLocation = "흑석동",
            showLocationDialog = false,
            appliedFilters = FilterSettings(

            )
        )
    }
}