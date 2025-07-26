// 파일명: GongguDetailScreen.kt
package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.weshare.android.R

// ==================== 백엔드 연결을 위한 데이터 모델 ====================
data class GongguDetailResponse(
    val id: Int,
    val title: String,
    val price: Int, // 백엔드에서는 숫자로 받음
    val totalQuantity: Int,
    val remainingQuantity: Int,
    val daysLeft: Int,
    val views: Int,
    val likes: Int,
    val comments: Int,
    val description: String,
    val imageUrl: String? = null,
    val seller: SellerInfoResponse,
    val createdAt: String,
    val updatedAt: String
)

data class SellerInfoResponse(
    val id: Int,
    val name: String,
    val phone: String,
    val rating: Float,
    val reviewCount: Int,
    val profileImageUrl: String? = null
)

data class GongguDetailItem(
    val id: Int,
    val title: String,
    val price: String, // UI에서는 포맷된 문자열
    val totalQuantity: Int,
    val remainingQuantity: Int,
    val daysLeft: Int,
    val views: Int,
    val likes: Int,
    val comments: Int,
    val description: String,
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val seller: SellerInfo
)

data class SellerInfo(
    val name: String,
    val phone: String,
    val rating: Float,
    val reviewCount: Int,
    val profileImageRes: Int? = null
)

// Response를 UI 모델로 변환
fun GongguDetailResponse.toUiModel(): GongguDetailItem {
    return GongguDetailItem(
        id = this.id,
        title = this.title,
        price = "${String.format("%,d", this.price)}원",
        totalQuantity = this.totalQuantity,
        remainingQuantity = this.remainingQuantity,
        daysLeft = this.daysLeft,
        views = this.views,
        likes = this.likes,
        comments = this.comments,
        description = this.description,
        imageUrl = this.imageUrl,
        seller = this.seller.toUiModel()
    )
}

fun SellerInfoResponse.toUiModel(): SellerInfo {
    return SellerInfo(
        name = this.name,
        phone = "휴대폰 번호: ${this.phone.takeLast(1)}", // 보안을 위해 마지막 자리만 표시
        rating = this.rating,
        reviewCount = this.reviewCount,
        profileImageRes = null // 실제로는 imageUrl 사용
    )
}

// ==================== API Service & Repository ====================
interface GongguDetailApiService {
    suspend fun getGongguDetail(itemId: Int): GongguDetailResponse
    suspend fun participateInGonggu(itemId: Int, quantity: Int): Boolean
    suspend fun toggleLike(itemId: Int): Boolean
    suspend fun incrementView(itemId: Int): Boolean
}

class GongguDetailRepository(
    private val apiService: GongguDetailApiService? = null
) {
    suspend fun getGongguDetail(itemId: Int): Result<GongguDetailItem> {
        return try {
            if (apiService != null) {
                val response = apiService.getGongguDetail(itemId)
                Result.success(response.toUiModel())
            } else {
                Result.success(getMockDetailData(itemId))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun participateInGonggu(itemId: Int, quantity: Int): Result<Boolean> {
        return try {
            if (apiService != null) {
                val result = apiService.participateInGonggu(itemId, quantity)
                Result.success(result)
            } else {
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleLike(itemId: Int): Result<Boolean> {
        return try {
            if (apiService != null) {
                val result = apiService.toggleLike(itemId)
                Result.success(result)
            } else {
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementView(itemId: Int): Result<Boolean> {
        return try {
            if (apiService != null) {
                val result = apiService.incrementView(itemId)
                Result.success(result)
            } else {
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getMockDetailData(itemId: Int): GongguDetailItem {
        return when (itemId) {
            1 -> GongguDetailItem(
                id = 1,
                title = "제주삼다수 2L",
                price = "25,920원",
                totalQuantity = 24,
                remainingQuantity = 8,
                daysLeft = 10,
                views = 43,
                likes = 5,
                comments = 13,
                description = "삼다수 공동구매합니다\n2L 총 24개 구매해요\n\n신선하고 깨끗한 제주도의 천연 미네랄 워터입니다.\n대용량으로 구매하여 더욱 저렴하게 제공합니다.",
                imageRes = R.drawable.gonggu_samdasu,
                seller = SellerInfo(
                    name = "홍길동",
                    phone = "휴대폰 번호: 8",
                    rating = 5.0f,
                    reviewCount = 37
                )
            )
            2 -> GongguDetailItem(
                id = 2,
                title = "코카콜라 제로 190ml 60개 공동구매해요~",
                price = "48,000원",
                totalQuantity = 60,
                remainingQuantity = 15,
                daysLeft = 8,
                views = 32,
                likes = 3,
                comments = 6,
                description = "코카콜라 제로 공동구매합니다\n190ml 총 60개 구매해요\n\n칼로리 제로, 설탕 제로의 건강한 콜라입니다.",
                imageRes = R.drawable.gonggu_cola,
                seller = SellerInfo(
                    name = "김영희",
                    phone = "휴대폰 번호: 5",
                    rating = 4.8f,
                    reviewCount = 23
                )
            )
            3 -> GongguDetailItem(
                id = 3,
                title = "리벤스 물티슈 대용량",
                price = "35,600원",
                totalQuantity = 80,
                remainingQuantity = 17,
                daysLeft = 4,
                views = 56,
                likes = 6,
                comments = 10,
                description = "리벤스 물티슈 대용량 공동구매합니다\n총 80개 구매해요\n\n부드럽고 촉촉한 프리미엄 물티슈입니다.",
                imageRes = R.drawable.gonggu_tissue,
                seller = SellerInfo(
                    name = "박철수",
                    phone = "휴대폰 번호: 2",
                    rating = 4.9f,
                    reviewCount = 45
                )
            )
            4 -> GongguDetailItem(
                id = 4,
                title = "신라면 공구해요",
                price = "28,800원",
                totalQuantity = 40,
                remainingQuantity = 5,
                daysLeft = 4,
                views = 67,
                likes = 9,
                comments = 23,
                description = "신라면 공동구매합니다\n총 40개 구매해요\n\n매콤하고 깔끔한 국물 맛의 대표 라면입니다.",
                imageRes = R.drawable.gonggu_ramen,
                seller = SellerInfo(
                    name = "이민수",
                    phone = "휴대폰 번호: 3",
                    rating = 4.7f,
                    reviewCount = 56
                )
            )
            5 -> GongguDetailItem(
                id = 5,
                title = "자취생 필수품 햇반",
                price = "32,400원",
                totalQuantity = 48,
                remainingQuantity = 10,
                daysLeft = 2,
                views = 89,
                likes = 7,
                comments = 15,
                description = "햇반 공동구매합니다\n총 48개 구매해요\n\n간편하고 맛있는 즉석밥입니다.",
                imageRes = R.drawable.gonggu_bab,
                seller = SellerInfo(
                    name = "최수정",
                    phone = "휴대폰 번호: 7",
                    rating = 4.6f,
                    reviewCount = 28
                )
            )
            else -> GongguDetailItem(
                id = itemId,
                title = "상품을 찾을 수 없습니다",
                price = "0원",
                totalQuantity = 0,
                remainingQuantity = 0,
                daysLeft = 0,
                views = 0,
                likes = 0,
                comments = 0,
                description = "해당 상품을 찾을 수 없습니다.",
                seller = SellerInfo("", "", 0f, 0)
            )
        }
    }
}

// ==================== ViewModel ====================
data class GongguDetailUiState(
    val item: GongguDetailItem? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLiked: Boolean = false,
    val participateResult: String? = null
)

class GongguDetailViewModel(
    private val repository: GongguDetailRepository = GongguDetailRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GongguDetailUiState())
    val uiState: StateFlow<GongguDetailUiState> = _uiState.asStateFlow()

    fun loadGongguDetail(itemId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // 조회수 증가
            repository.incrementView(itemId)

            val result = repository.getGongguDetail(itemId)
            result.fold(
                onSuccess = { item ->
                    _uiState.value = _uiState.value.copy(
                        item = item,
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

    fun toggleLike() {
        val currentItem = _uiState.value.item ?: return

        viewModelScope.launch {
            val result = repository.toggleLike(currentItem.id)
            result.fold(
                onSuccess = {
                    val currentLiked = _uiState.value.isLiked
                    val updatedItem = currentItem.copy(
                        likes = if (currentLiked) currentItem.likes - 1 else currentItem.likes + 1
                    )
                    _uiState.value = _uiState.value.copy(
                        item = updatedItem,
                        isLiked = !currentLiked
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "좋아요 실패"
                    )
                }
            )
        }
    }

    fun participateInGonggu(quantity: Int) {
        val currentItem = _uiState.value.item ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = repository.participateInGonggu(currentItem.id, quantity)
            result.fold(
                onSuccess = {
                    val updatedItem = currentItem.copy(
                        remainingQuantity = currentItem.remainingQuantity - quantity
                    )
                    _uiState.value = _uiState.value.copy(
                        item = updatedItem,
                        participateResult = "공동구매 참여가 완료되었습니다!",
                        isLoading = false
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "참여 실패",
                        isLoading = false
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearParticipateResult() {
        _uiState.value = _uiState.value.copy(participateResult = null)
    }
}

// ==================== UI Components ====================
@Composable
fun GongguDetailScreenContainer(
    itemId: Int,
    viewModel: GongguDetailViewModel = remember { GongguDetailViewModel() },
    onBackClick: () -> Unit = {},
    onParticipateClick: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(itemId) {
        viewModel.loadGongguDetail(itemId)
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            viewModel.clearError()
        }
    }

    LaunchedEffect(uiState.participateResult) {
        uiState.participateResult?.let {
            viewModel.clearParticipateResult()
            onParticipateClick(0) // 참여 완료 후 콜백 호출
        }
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        uiState.item != null -> {
            GongguDetailScreen(
                item = uiState.item!!,
                onBackClick = onBackClick,
                onHeartClick = { viewModel.toggleLike() },
                onParticipateClick = { quantity -> viewModel.participateInGonggu(quantity) },
                isLiked = uiState.isLiked
            )
        }
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "오류가 발생했습니다",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = uiState.error!!,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.loadGongguDetail(itemId) }
                    ) {
                        Text("다시 시도")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguDetailScreen(
    item: GongguDetailItem,
    onBackClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onHeartClick: () -> Unit = {},
    onParticipateClick: (Int) -> Unit = {},
    isLiked: Boolean = false
) {
    var selectedQuantity by remember { mutableStateOf(1) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("공동구매 참여하기", fontSize = 16.sp) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                }
            },
            actions = {
                IconButton(onClick = onMoreClick) {
                    Icon(Icons.Default.Menu, contentDescription = "더보기")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                item.imageRes?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: run {
                    // TODO: AsyncImage로 네트워크 이미지 로드
                    Text(
                        text = "이미지 없음",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title and Heart
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = onHeartClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "찜하기",
                        modifier = Modifier.size(24.dp),
                        tint = if (isLiked) Color.Red else Color.Gray
                    )
                }
                IconButton(
                    onClick = onMoreClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "더보기",
                        modifier = Modifier.size(24.dp),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(1.dp))

            // Price
            Text(
                text = item.price,
                fontSize = 21.5.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(5.dp))

            // Quantity Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                ,verticalArrangement = Arrangement.spacedBy(-3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "총 구매 개수 : ${item.totalQuantity}개",
                        fontSize = 18.sp,
                        letterSpacing = (-0.5).sp,
                        color = Color.Black
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "남은 개수 : ${item.remainingQuantity}개",
                        fontSize = 18.sp,
                        letterSpacing = (-0.5).sp,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "마감 D-${item.daysLeft}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (item.daysLeft <= 3) Color.Red else Color.Gray
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = " ${item.views}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = " ${item.likes}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
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

            Spacer(modifier = Modifier.height(8.dp))

// (2) Divider 추가
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp),
                color = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = item.description,
                fontSize = 16.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Contact Info
            Text(
                text = "공동구매 상품 바로가기",
                fontSize = 12.sp,
                color = Color.Blue,
                style = TextStyle(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { }
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Seller Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF8F8F8),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Image
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    item.seller.profileImageRes?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = "프로필",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } ?: run {
                        Text(
                            text = item.seller.name.take(1),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.seller.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = " ${item.seller.phone}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFFFFD700)
                        )
                        Text(
                            text = " ${item.seller.rating}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = " ${item.seller.reviewCount}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp)) // Bottom padding for FAB
        }

        // Bottom Participation Section
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quantity Selector
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .height(40.dp)
                        .width(90.dp)
                ) {
                    // - 버튼 (패딩 없음)
                    IconButton(
                        onClick = {
                            if (selectedQuantity > 1) selectedQuantity--
                        },
                        modifier = Modifier.size(24.dp) // 크기 줄임
                    ) {
                        Text(
                            "-",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // 세로 구분선
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = Color.LightGray
                    )

                    // 숫자 영역 (넉넉한 패딩)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp), // 구분선과 숫자 사이 간격 넓게
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$selectedQuantity",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // 세로 구분선
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp),
                        color = Color.LightGray
                    )

                    // + 버튼 (패딩 없음)
                    IconButton(
                        onClick = {
                            if (selectedQuantity < item.remainingQuantity) selectedQuantity++
                        },
                        modifier = Modifier.size(24.dp) // 크기 줄임
                    ) {
                        Text(
                            "+",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Price and Button
                Column(modifier = Modifier.weight(1f)) {
                    val priceText = item.price.replace(",", "").replace("원", "")
                    val priceInt = priceText.toIntOrNull() ?: 0
                    val totalPrice = selectedQuantity * priceInt

                    Text(
                        text = "${String.format("%,d", totalPrice)}원",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue
                    )
                    Text(
                        text = "무료배송",
                        fontSize = 11.sp,
                        color = Color.Black
                    )
                }
                Button(
                    onClick = { onParticipateClick(selectedQuantity) },
                    enabled = selectedQuantity <= item.remainingQuantity && item.remainingQuantity > 0,
                    modifier = Modifier
                        .height(37.dp)
                        .width(100.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(0.dp) // 내부 패딩 제거
                ) {
                    Text(
                        "참여하기",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GongguDetailScreenPreview() {
    val mockItem = GongguDetailItem(
        id = 1,
        title = "제주삼다수 2L",
        price = "25,920원",
        totalQuantity = 24,
        remainingQuantity = 8,
        daysLeft = 10,
        views = 43,
        likes = 5,
        comments = 13,
        description = "삼다수 공동구매합니다\n2L 총 24개 구매해요",
        imageRes = R.drawable.gonggu_samdasu,
        seller = SellerInfo(
            name = "홍길동",
            phone = "휴대폰 번호: 8",
            rating = 5.0f,
            reviewCount = 37
        )
    )

    MaterialTheme {
        GongguDetailScreen(item = mockItem)
    }
}