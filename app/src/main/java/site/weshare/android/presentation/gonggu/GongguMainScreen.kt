package site.weshare.android.presentation.gonggu

import site.weshare.android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    private val apiService: GongguApiService? = null
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

    private fun getMockData(): List<GongguItem> = listOf(
        GongguItem(1, "제주삼다수 2L", "총 구매 개수 : 24개\n남은 개수 : 8개", "25,920원",16,24,10,5,13,43, R.drawable.gonggu_samdasu),
        GongguItem(2, "코카콜라 제로 190ml 60개 공동구매해요~","총 구매 개수 : 60개\n남은 개수 : 15개","48,000원",45,60,8,3,6,32, R.drawable.gonggu_cola),
        GongguItem(3, "리벤스 물티슈 대용량","총 구매 개수 : 80개\n남은 개수 : 17개","35,600원",63,80,4,6,10,56, R.drawable.gonggu_tissue),
        GongguItem(4, "신라면 공구해요","총 구매 개수 : 40개\n남은 개수 : 5개","28,800원",35,40,4,9,23,67, R.drawable.gonggu_ramen),
        GongguItem(5, "자취생 필수품 햇반","총 구매 개수 : 48개\n남은 개수 : 10개","32,400원",38,48,2,7,15,89, R.drawable.gonggu_bab),
        GongguItem(6, "오뚜기 진라면 매운맛","총 구매 개수 : 36개\n남은 개수 : 12개","19,800원",24,36,6,4,8,28, R.drawable.gonggu_jinramen),
        GongguItem(7, "봉쥬르끌레어 캡슐세제","총 구매 개수 : 200개\n남은 개수 : 7개","42,000원",13,20,12,2,4,19, R.drawable.gonggu_seje),
        GongguItem(8, "동원 참치캔","총 구매 개수 : 50개\n남은 개수 : 18개","38,500원",32,50,1,11,26,78, R.drawable.gonggu_dongwon)
    )
}

// ==================== UI State & ViewModel ====================
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

class GongguViewModel(
    private val repository: GongguRepository = GongguRepository()
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
        _uiState.value = _uiState.value.copy(showLocationDialog = true) }

    fun hideLocationDialog() {
        _uiState.value = _uiState.value.copy(showLocationDialog = false) }

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

// ==================== UI Components ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguMainScreen(
    viewModel: GongguViewModel = remember { GongguViewModel() },
    onItemClick: (GongguItem) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) viewModel.clearError()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 7.dp)
        ) {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            if (uiState.showLocationDialog) {
                                viewModel.hideLocationDialog()
                            } else {
                                viewModel.showLocationDialog()
                            }
                        }
                    ) {
                        Text(uiState.currentLocation, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .graphicsLayer {
                                    rotationZ = if (uiState.showLocationDialog) 180f else 0f
                                }
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "검색") }
                    IconButton(onClick = {}) { Icon(Icons.Default.FavoriteBorder, contentDescription = "찜") }
                    IconButton(onClick = {}) { Icon(Icons.Default.Notifications, contentDescription = "알림") }
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
                // 전체 버튼 (특별 처리)
                FilterChip(
                    selected = false, onClick = { viewModel.updateFilter(FilterType.ALL) },
                    label = {
                        Text("전체", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(containerColor = Color.White, labelColor = Color.Black)
                )

                // 가격 버튼
                FilterChip(
                    selected = uiState.appliedFilters.minPrice.isNotEmpty() || uiState.appliedFilters.maxPrice.isNotEmpty(),
                    onClick = { /* 개별 가격 필터 동작 */ },
                    label = {
                        Text("가격", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2FB475),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White, labelColor = Color.Black)
                )

                // 개수 버튼
                FilterChip(
                    selected = uiState.appliedFilters.minCount.isNotEmpty() || uiState.appliedFilters.maxCount.isNotEmpty(),
                    onClick = { /* 개별 개수 필터 동작 */ },
                    label = {
                        Text("개수", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2FB475),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White, labelColor = Color.Black)
                )

                // 카테고리 버튼
                FilterChip(
                    selected = uiState.appliedFilters.selectedCategory.isNotEmpty(),
                    onClick = { /* 개별 카테고리 필터 동작 */ },
                    label = {
                        Text("카테고리", fontSize = 14.sp, fontWeight = FontWeight.Bold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF2FB475),
                        selectedLabelColor = Color.White,
                        containerColor = Color.White, labelColor = Color.Black),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Text("상품 ${uiState.totalCount}", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
                    Text(uiState.sortBy.displayName, fontSize = 12.sp, color = Color.Gray)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                }
            }

            if (uiState.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(uiState.items, key = { it.id }) { item ->
                        GongguItemCard(item, onClick = { onItemClick(item) }, onLikeClick = { viewModel.toggleLike(item.id) })
                        if (item != uiState.items.last()) {
                            Divider(color = Color.LightGray, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }

        if (!uiState.showRegisterOverlay) {
            // 기본 등록하기 FAB (오버레이가 보이지 않을 때만)
            FloatingActionButton(
                onClick = { viewModel.showRegisterOverlay() },
                containerColor = Color(0xFF2FB475),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 35.dp, bottom = 100.dp)
                    .size(width = 100.dp, height = 45.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("등록하기", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}


@Composable
fun GongguItemCard(
    item: GongguItem,
    onClick: () -> Unit,
    onLikeClick: () -> Unit = {}
) {
    val isPreview = LocalInspectionMode.current
    var menuExpanded by remember { mutableStateOf(isPreview) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            when {
                item.imageRes != null -> {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                item.imageUrl != null -> {
                    /* TODO: AsyncImage 로 네트워크 이미지 로드 */
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = item.title, fontSize = 16.5.sp, fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp, letterSpacing = (-0.05).sp, maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(0.5.dp))
                Text(text = item.description, fontSize = 13.sp, color = Color.DarkGray,
                    maxLines = 2, overflow = TextOverflow.Ellipsis, letterSpacing = (-0.5).sp,
                    lineHeight = 16.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("마감 D-${item.daysLeft}", fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item.daysLeft <= 3) Color.Red else Color.Gray
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null,
                        modifier = Modifier.size(12.dp), tint = Color.Gray
                    )
                    Text(" ${item.likes}", fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.FavoriteBorder, contentDescription = null,
                        modifier = Modifier
                            .size(12.dp)
                            .clickable { onLikeClick() },
                        tint = Color.Gray
                    )
                    Text(" ${item.comments}", fontSize = 11.sp, color = Color.Gray)
                }
            }
        }

        Box {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = null)
            }

            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false },
                shape = RoundedCornerShape(0.dp), shadowElevation = 5.dp, tonalElevation = 0.dp,
                containerColor = Color.White, modifier = Modifier.padding(0.dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.report),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("신고하기", fontSize = 14.sp)
                        }
                    },
                    onClick = { menuExpanded = false },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(30.dp)
                )
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.share),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("공유하기", fontSize = 14.sp)
                        }
                    },
                    onClick = { menuExpanded = false },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(30.dp)
                )
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ask),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text("문의하기", fontSize = 14.sp)
                        }
                    },
                    onClick = { menuExpanded = false },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(30.dp)
                )
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