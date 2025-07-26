package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.BorderStroke
import site.weshare.android.R
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle

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

// ==================== 새로운 필터 데이터 모델 ====================
data class FilterSettings(
    val minPrice: String = "",
    val maxPrice: String = "",
    val minCount: String = "",
    val maxCount: String = "",
    val selectedCategory: String = ""
)

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

    fun searchItems(query: String) {
        if (query.isBlank()) {
            loadGongguItems()
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.searchItems(query).fold(
                onSuccess = { items ->
                    _uiState.value = _uiState.value.copy(items = items, totalCount = items.size, isLoading = false)
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "Search failed",
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

    fun updateSort(sort: SortType) {
        _uiState.value = _uiState.value.copy(sortBy = sort)
        loadGongguItems() }

    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(currentLocation = location, showLocationDialog = false)
        loadGongguItems() }

    fun showLocationDialog() {
        _uiState.value = _uiState.value.copy(showLocationDialog = true) }

    fun hideLocationDialog() {
        _uiState.value = _uiState.value.copy(showLocationDialog = false) }

    fun showFilterDialog() {
        _uiState.value = _uiState.value.copy(showFilterDialog = true, filterSettings = _uiState.value.appliedFilters)
    }

    fun hideFilterDialog() {
        _uiState.value = _uiState.value.copy(showFilterDialog = false) }

    fun updateFilterSettings(settings: FilterSettings) {
        _uiState.value = _uiState.value.copy(filterSettings = settings) }

    fun applyFilters() {
        _uiState.value = _uiState.value.copy(appliedFilters = _uiState.value.filterSettings, showFilterDialog = false)
        loadGongguItems()
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

// ==================== UI Components ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguMainScreen(
    viewModel: GongguViewModel = remember { GongguViewModel() },
    onItemClick: (GongguItem) -> Unit = {},
    onRegisterClick: () -> Unit = {}
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

        // RegisterOverlay (오버레이가 보일 때만)
        if (uiState.showRegisterOverlay) {
            RegisterOverlay(
                onRegisterClick = {
                    viewModel.hideRegisterOverlay()
                    onRegisterClick()
                },
                onDismiss = { viewModel.hideRegisterOverlay() }
            )
        }

        // Location Selection Dialog
        if (uiState.showLocationDialog) {
            LocationSelectionDialog(currentLocation = uiState.currentLocation,
                onLocationSelected = { location -> viewModel.updateLocation(location) },
                onDismiss = { viewModel.hideLocationDialog() }
            )
        }

        // Filter Dialog
        if (uiState.showFilterDialog) {
            FilterDialog(filterSettings = uiState.filterSettings,
                onFilterSettingsChange = { viewModel.updateFilterSettings(it) },
                onApply = { viewModel.applyFilters() }, onDismiss = { viewModel.hideFilterDialog() }
            )
        }
    }
}

@Composable
fun RegisterOverlay(
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

        // 공동구매 등록하기 버튼
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 35.dp, bottom = 160.dp) // FAB 위에 위치
        ) {
            Button(
                onClick = onRegisterClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2FB475),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(160.dp)
                    .height(47.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    "공동구매 등록하기",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
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

@Composable
fun FilterDialog(
    filterSettings: FilterSettings, onFilterSettingsChange: (FilterSettings) -> Unit, onApply: () -> Unit, onDismiss: () -> Unit) {
    val backgroundAlpha = 0.3f

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 배경
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha))
                .clickable { onDismiss() }
        )

        // 필터 다이얼로그
        Card(
            modifier = Modifier
                .width(325.dp)
                .padding(horizontal = 17.dp, vertical = 5.dp)
                .wrapContentHeight()
                .offset(x = (-20).dp)
                .align(Alignment.Center),

            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
            ) {
                // 가격 텍스트
                Text(
                    text = "가격",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

// 상태
                var minPriceSlider by remember { mutableFloatStateOf(0f) }
                var maxPriceSlider by remember { mutableFloatStateOf(1000000f) }

                // 가격 포맷 함수
                fun formatPrice(price: Float): String = "%,d원".format(price.toInt())

// 가격 박스
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = if (minPriceSlider == 0f) "0원" else formatPrice(minPriceSlider),
                        onValueChange = {},
                        placeholder = { Text("최소 가격", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(33.dp),
                        singleLine = true, readOnly = true,
                        textStyle = TextStyle(fontSize = 13.sp, textAlign = TextAlign.End, color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2FB475),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Text("~", fontSize = 16.sp, color = Color.Gray)

                    OutlinedTextField(
                        value = if (maxPriceSlider == 1000000f) "1,000,000원" else formatPrice(maxPriceSlider),
                        onValueChange = {},
                        placeholder = { Text("최대 가격", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(33.dp),
                        singleLine = true, readOnly = true,
                        textStyle = TextStyle(fontSize = 13.sp, textAlign = TextAlign.End, color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2FB475),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                }

// 슬라이드 바 + 슬라이더
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    val trackHeight = 4.dp

                    Canvas(modifier = Modifier.matchParentSize()) {
                        val trackY = size.height / 2
                        val trackWidth = size.width

                        val minPx = (minPriceSlider / 1000000f) * trackWidth
                        val maxPx = (maxPriceSlider / 1000000f) * trackWidth

                        // 전체 트랙 (회색)
                        drawRoundRect(
                            color = Color.LightGray,
                            topLeft = Offset(0f, trackY - trackHeight.toPx() / 2),
                            size = Size(trackWidth, trackHeight.toPx()),
                            cornerRadius = CornerRadius(trackHeight.toPx())
                        )

                        // 활성 구간 (초록색) - 최소값과 최대값 사이
                        if (maxPx > minPx) {
                            drawRoundRect(
                                color = Color(0xFF2FB475),
                                topLeft = Offset(minPx, trackY - trackHeight.toPx() / 2),
                                size = Size(maxPx - minPx, trackHeight.toPx()),
                                cornerRadius = CornerRadius(trackHeight.toPx())
                            )
                        }

                        // 썸 - 최소값 (초록색)
                        drawCircle(Color(0xFF2FB475), 10.dp.toPx(), Offset(minPx, trackY))
                        drawCircle(Color.White, 6.dp.toPx(), Offset(minPx, trackY))

                        // 썸 - 최대값 (초록색)
                        drawCircle(Color(0xFF2FB475), 10.dp.toPx(), Offset(maxPx, trackY))
                        drawCircle(Color.White, 6.dp.toPx(), Offset(maxPx, trackY))
                    }

                    // 최소값 슬라이더
                    Slider(
                        value = minPriceSlider,
                        onValueChange = { newValue ->
                            minPriceSlider = newValue.coerceAtMost(maxPriceSlider)
                            onFilterSettingsChange(filterSettings.copy(minPrice = minPriceSlider.toInt().toString()))
                        },
                        valueRange = 0f..1000000f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(thumbColor = Color.Transparent, activeTrackColor = Color.Transparent, inactiveTrackColor = Color.Transparent)
                    )

                    // 최대값 슬라이더
                    Slider(
                        value = maxPriceSlider,
                        onValueChange = { newValue ->
                            maxPriceSlider = newValue.coerceAtLeast(minPriceSlider)
                            onFilterSettingsChange(filterSettings.copy(maxPrice = maxPriceSlider.toInt().toString()))
                        },
                        valueRange = 0f..1000000f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(thumbColor = Color.Transparent, activeTrackColor = Color.Transparent, inactiveTrackColor = Color.Transparent
                        )
                    )
                }

                // 개수 필터
                Text(text = "개수", fontSize =17.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(bottom = 8.dp))

// 개수 입력 UI
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .height(33.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // [ - 버튼 ]
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .fillMaxHeight()
                            .clickable {
                                val currentCount = filterSettings.minCount.toIntOrNull() ?: 0
                                if (currentCount > 0) {
                                    onFilterSettingsChange(filterSettings.copy(minCount = (currentCount - 1).toString()))
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "−", fontSize = 20.sp, color = Color.Black)
                    }

                    // [ 구분선 1 ]
                    Box(
                        modifier = Modifier
                            .height(30.dp)           // 적절한 길이의 세로선
                            .width(1.dp)
                            .background(Color.LightGray)
                    )

                    // [ 숫자 중앙 표시 ]
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = filterSettings.minCount.ifEmpty { "0" }, fontSize = 16.sp, color = Color.Gray)
                    }

                    // [ 구분선 2 ]
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp)
                            .background(Color.LightGray)
                    )

                    // [ + 버튼 ]
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .fillMaxHeight()
                            .clickable {
                                val currentCount = filterSettings.minCount.toIntOrNull() ?: 0
                                onFilterSettingsChange(filterSettings.copy(minCount = (currentCount + 1).toString()))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+", fontSize = 20.sp, color = Color.Black)
                    }
                }

// 개수 선택 버튼들
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val countOptions = listOf("+1개", "+5개", "+10개", "+100개")
                    countOptions.forEach { option ->
                        val incrementValue = option.replace("+", "").replace("개", "").toInt()
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp)) // FilterChip의 기본 모양
                                .background(Color(0xffE8E8E8))
                                .clickable {
                                    val currentCount = filterSettings.minCount.toIntOrNull() ?: 0
                                    val newCount = currentCount + incrementValue
                                    onFilterSettingsChange(filterSettings.copy(minCount = newCount.toString()))
                                }
                                .padding(horizontal = 12.7.dp, vertical = 3.dp), // 최소한의 패딩만 적용
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = option, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2FB475), letterSpacing = -0.1.sp)
                        }
                    }
                }

                // 카테고리 필터
                Text(text = "카테고리", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(bottom = 8.dp))

                var selectedCategories by remember { mutableStateOf(setOf<String>()) }

                val categories = listOf(
                    listOf("의류", "신발", "가구"),
                    listOf("디지털기기", "생활가전", "게임"),
                    listOf("피규어/인형", "스포츠", "도서/티켓/음반"),
                    listOf("뷰티/미용")
                )

                val categoryLetterSpacings = mapOf(
                    "의류" to 0.sp, "신발" to 0.sp, "디지털기기" to (-0.9).sp,
                    "가구" to 0.sp, "생활가전" to (-0.3).sp, "게임" to 0.sp,
                    "피규어/인형" to (-1).sp, "스포츠" to 0.sp, "도서/티켓/음반" to (-1.2).sp,
                    "뷰티/미용" to (-0.9).sp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    categories.forEach { rowCategories ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                        ) {
                            rowCategories.forEach { category ->
                                val isSelected = selectedCategories.contains(category)

                                Box(
                                    modifier = Modifier
                                        .width(75.dp)
                                        .height(27.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(
                                            if (isSelected)
                                                Color(0xFFD6F0C7)
                                            else
                                                Color.White
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected)
                                                Color(0xFF2FB475)
                                            else
                                                Color.LightGray,
                                            RoundedCornerShape(15.dp)
                                        )
                                        .clickable {
                                            selectedCategories = if (isSelected) {
                                                selectedCategories - category // 선택 해제
                                            } else {
                                                selectedCategories + category // 선택 추가
                                            }
                                            // 필터 설정 업데이트 (선택된 카테고리들을 쉼표로 구분하여 전달)
                                            onFilterSettingsChange(
                                                filterSettings.copy(
                                                    selectedCategory = selectedCategories.joinToString(",")
                                                )
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = category, fontSize = 12.sp, fontWeight = FontWeight.Medium, letterSpacing = categoryLetterSpacings[category] ?: 0.sp,
                                        textAlign = TextAlign.Center, maxLines = 1,
                                        color = if (isSelected)
                                            Color.Black
                                        else
                                            Color.Black
                                    )
                                }
                            }
                            // 빈 공간 채우기 (3개 미만인 경우)
                            repeat(3 - rowCategories.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // 버튼들을 나란히 배치
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 버튼들을 나란히 배치
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 초기화 버튼
                        OutlinedButton(
                            onClick = {
                                onFilterSettingsChange(
                                    FilterSettings(minPrice = "", maxPrice = "", minCount = "",maxCount = "", selectedCategory = "")
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Black, containerColor = Color.White),
                            border = BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("초기화", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }

                        // 결과보기 버튼
                        Button(onClick = onApply,
                            modifier = Modifier
                                .weight(2f)
                                .height(38.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2FB475),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "512개 결과보기",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationSelectionDialog(
    currentLocation: String,
    onLocationSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val locations = listOf("흑석동", "이태원2동", "재설정하기")
    val backgroundAlpha = 0.3f

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha))
                .clickable { onDismiss() }
        )

        Card(
            modifier = Modifier
                .width(200.dp)
                .wrapContentHeight()
                .padding(start = 20.dp, top = 80.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(0.dp)
            ) {
                locations.forEach { location ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLocationSelected(location) }
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = location, fontSize = 16.sp,
                            color = if (location == currentLocation) Color.Black else Color.Gray,
                            fontWeight = if (location == currentLocation) FontWeight.ExtraBold else FontWeight.Normal
                        )
                    }
                    if (location != locations.last()) {
                        Divider(
                            color = Color.LightGray.copy(alpha = 0.5f),
                            thickness = 0.5.dp
                        )
                    }
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

@Preview(showBackground = true)
@Composable
fun FilterDialogPreview() {
    MaterialTheme {
        FilterDialog(
            filterSettings = FilterSettings(
                minPrice = "10000",
                maxPrice = "50000",
                selectedCategory = "식료품"
            ),
            onFilterSettingsChange = {},
            onApply = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GongguItemCardMenuPreview() {
    CompositionLocalProvider(LocalInspectionMode provides true) {
        MaterialTheme {
            GongguItemCard(
                item = GongguItem(
                    id = 1,
                    title = "제주삼다수 2L",
                    description = "총 구매 개수 : 24개\n남은 개수 : 8개",
                    price = "25,920원",
                    currentCount = 16,
                    maxCount = 24,
                    daysLeft = 10,
                    likes = 5,
                    comments = 13,
                    views = 43,
                    imageRes = R.drawable.gonggu_samdasu
                ),
                onClick = {},
                onLikeClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationSelectionDialogPreview() {
    MaterialTheme {
        LocationSelectionDialog(
            currentLocation = "흑석동",
            onLocationSelected = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterOverlayPreview() {
    MaterialTheme {
        RegisterOverlay(
            onRegisterClick = {},
            onDismiss = {}
        )
    }
}