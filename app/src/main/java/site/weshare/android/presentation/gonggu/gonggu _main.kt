// 파일명: GongguMainScreen.kt
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
import androidx.compose.ui.layout.ContentScale
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

// ==================== 수정된 메인화면 (모델들 제거하고 간소화) ====================
data class GongguItemResponse(
    val id: Int,
    val title: String,
    val description: String,
    val currentCount: Int,
    val maxCount: Int,
    val daysLeft: Int,
    val likes: Int,
    val comments: Int,
    val imageUrl: String? = null,
    val tag: String? = null,
    val createdAt: String? = null,
    val location: String? = null
)

data class GongguItem(
    val id: Int,
    val title: String,
    val description: String,
    val currentCount: Int,
    val maxCount: Int,
    val daysLeft: Int,
    val likes: Int,
    val comments: Int,
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val tag: String? = null
)

fun GongguItemResponse.toUiModel(): GongguItem {
    return GongguItem(
        id = this.id,
        title = this.title,
        description = this.description,
        currentCount = this.currentCount,
        maxCount = this.maxCount,
        daysLeft = this.daysLeft,
        likes = this.likes,
        comments = this.comments,
        imageUrl = this.imageUrl,
        tag = this.tag
    )
}

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
    ): Result<List<GongguItem>> {
        return try {
            if (apiService != null) {
                val response = apiService.getGongguItems(location, sortBy, category, paymentType)
                Result.success(response.map { it.toUiModel() })
            } else {
                Result.success(getMockData())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchItems(query: String): Result<List<GongguItem>> {
        return try {
            if (apiService != null) {
                val response = apiService.searchGongguItems(query)
                Result.success(response.map { it.toUiModel() })
            } else {
                val filteredData = getMockData().filter {
                    it.title.contains(query, ignoreCase = true)
                }
                Result.success(filteredData)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleLike(itemId: Int, isLiked: Boolean): Result<Boolean> {
        return try {
            if (apiService != null) {
                val result = if (isLiked) {
                    apiService.unlikeGongguItem(itemId)
                } else {
                    apiService.likeGongguItem(itemId)
                }
                Result.success(result)
            } else {
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getMockData(): List<GongguItem> {
        return listOf(
            GongguItem(
                id = 1,
                title = "제주삼다수 2L",
                description = "총 구매 개수 : 24개\n남은 개수 : 8개",
                currentCount = 16,
                maxCount = 24,
                daysLeft = 10,
                likes = 5,
                comments = 13,
                imageRes = R.drawable.gonggu_samdasu
            ),
            GongguItem(
                id = 2,
                title = "코카콜라 제로 190ml 60개 공동구매해요~",
                description = "총 구매 개수 : 60개\n남은 개수 : 15개",
                currentCount = 45,
                maxCount = 60,
                daysLeft = 8,
                likes = 3,
                comments = 6,
                imageRes = R.drawable.gonggu_cola
            ),
            GongguItem(
                id = 3,
                title = "리벤스 물티슈 대용량",
                description = "총 구매 개수 : 80개\n남은 개수 : 17개",
                currentCount = 63,
                maxCount = 80,
                daysLeft = 4,
                likes = 6,
                comments = 10,
                imageRes = R.drawable.gonggu_tissue
            ),
            GongguItem(
                id = 4,
                title = "신라면 공구해요",
                description = "총 구매 개수 : 40개\n남은 개수 : 5개",
                currentCount = 35,
                maxCount = 40,
                daysLeft = 4,
                likes = 9,
                comments = 23,
                imageRes = R.drawable.gonggu_ramen
            ),
            GongguItem(
                id = 5,
                title = "자취생 필수품 햇반",
                description = "총 구매 개수 : 48개\n남은 개수 : 10개",
                currentCount = 38,
                maxCount = 48,
                daysLeft = 2,
                likes = 7,
                comments = 15,
                imageRes = R.drawable.gonggu_bab
            )
        )
    }
}

data class GongguUiState(
    val items: List<GongguItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentLocation: String = "흑석동",
    val selectedFilter: FilterType = FilterType.ALL,
    val sortBy: SortType = SortType.LATEST,
    val totalCount: Int = 0
)

enum class FilterType(val displayName: String, val apiValue: String?) {
    ALL("전체", null),
    DISTANCE("거리", "distance"),
    PAYMENT("결제", "payment"),
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

            val result = repository.getGongguItems(
                location = _uiState.value.currentLocation,
                sortBy = _uiState.value.sortBy.apiValue,
                category = if (_uiState.value.selectedFilter == FilterType.CATEGORY) "food" else null,
                paymentType = if (_uiState.value.selectedFilter == FilterType.PAYMENT) "card" else null
            )

            result.fold(
                onSuccess = { items ->
                    _uiState.value = _uiState.value.copy(
                        items = items,
                        totalCount = items.size,
                        isLoading = false
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Unknown error",
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

            val result = repository.searchItems(query)
            result.fold(
                onSuccess = { items ->
                    _uiState.value = _uiState.value.copy(
                        items = items,
                        totalCount = items.size,
                        isLoading = false
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Search failed",
                        isLoading = false
                    )
                }
            )
        }
    }

    fun updateFilter(filter: FilterType) {
        _uiState.value = _uiState.value.copy(selectedFilter = filter)
        loadGongguItems()
    }

    fun updateSort(sort: SortType) {
        _uiState.value = _uiState.value.copy(sortBy = sort)
        loadGongguItems()
    }

    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(currentLocation = location)
        loadGongguItems()
    }

    fun toggleLike(itemId: Int) {
        viewModelScope.launch {
            val currentItems = _uiState.value.items
            val itemIndex = currentItems.indexOfFirst { it.id == itemId }
            if (itemIndex == -1) return@launch

            val currentItem = currentItems[itemIndex]
            val isCurrentlyLiked = false

            repository.toggleLike(itemId, isCurrentlyLiked)

            val updatedItems = currentItems.toMutableList().apply {
                this[itemIndex] = currentItem.copy(
                    likes = if (isCurrentlyLiked) currentItem.likes - 1 else currentItem.likes + 1
                )
            }
            _uiState.value = _uiState.value.copy(items = updatedItems)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

// ==================== 메인화면 UI (기존과 동일) ====================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguMainScreen(
    viewModel: GongguViewModel = remember { GongguViewModel() },
    onItemClick: (GongguItem) -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 7.dp)
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = uiState.currentLocation,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Search, contentDescription = "검색")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "찜")
                }
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = "알림")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterType.values().forEach { filter ->
                FilterChip(
                    onClick = { viewModel.updateFilter(filter) },
                    label = { Text(filter.displayName, fontSize = 12.sp) },
                    selected = uiState.selectedFilter == filter
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "상품 ${uiState.totalCount}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { }
            ) {
                Text(
                    text = uiState.sortBy.displayName,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 80.dp)
            ) {
                items(
                    items = uiState.items,
                    key = { it.id }
                ) { item ->
                    GongguItemCard(
                        item = item,
                        onClick = { onItemClick(item) },
                        onLikeClick = { viewModel.toggleLike(item.id) }
                    )
                    if (item != uiState.items.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 0.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = onRegisterClick,
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "등록하기",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
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
                    // TODO: AsyncImage
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
                Text(
                    text = item.title,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                    letterSpacing = (-0.05).sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(0.5.dp))

                Text(
                    text = item.description,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    letterSpacing = (-0.5).sp,
                    lineHeight = 16.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "마감 D-${item.daysLeft}",
                    fontSize = 12.sp,
                    color = if (item.daysLeft <= 3) Color.Red else Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = " ${item.likes}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .size(12.dp)
                            .clickable { onLikeClick() },
                        tint = Color.Gray
                    )
                    Text(
                        text = " ${item.comments}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        IconButton(
            onClick = { },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "더보기",
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GongguMainScreenPreview() {
    MaterialTheme {
        GongguMainScreen()
    }
}