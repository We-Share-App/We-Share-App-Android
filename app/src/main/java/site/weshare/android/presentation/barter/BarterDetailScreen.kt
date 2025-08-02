package site.weshare.android.presentation.barter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

// 가상의 상품 데이터 모델
data class ProductDetail(
    val id: String,
    val name: String,
    val imageUrls: List<String>,
    val status: String,
    val desiredCategories: List<String>,
    val views: Int,
    val comments: Int,
    val likes: Int,
    val description: String,
    val deadline: String,
    val sellerProfileImage: String,
    val sellerName: String,
    val isLiked: Boolean
)

// 가상의 데이터 로딩 함수 (백엔드에서 데이터를 가져오는 것을 시뮬레이션)
suspend fun fetchProductDetail(productId: String): ProductDetail {
    // 실제 앱에서는 여기서 Retrofit 등을 사용하여 백엔드 API 호출
    kotlinx.coroutines.delay(500) // 네트워크 지연 시뮬레이션
    return ProductDetail(
        id = productId,
        name = "롯데자이언츠 동백 유니폼 어센틱",
        imageUrls = listOf(
            "https://placehold.co/600x400/FF0000/FFFFFF?text=Busan+1",
            "https://placehold.co/600x400/00FF00/000000?text=Busan+2",
            "https://placehold.co/600x400/0000FF/FFFFFF?text=Busan+3"
        ),
        status = "사용감 없음",
        desiredCategories = listOf("스포츠", "의류"),
        views = 43,
        comments = 5,
        likes = 13,
        description = "사이즈 XL이고 상태 좋아요\n실착 2번 했어요",
        deadline = "마감 12시간 전",
        sellerProfileImage = "https://placehold.co/40x40/CCCCCC/FFFFFF?text=P",
        sellerName = "판매자 닉네임",
        isLiked = false
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BarterDetailScreen(productId: String, modifier: Modifier = Modifier) {
    var productDetail by remember { mutableStateOf<ProductDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isLiked by remember { mutableStateOf(false) } // 좋아요 상태

    // 데이터 로딩
    LaunchedEffect(productId) {
        isLoading = true
        productDetail = fetchProductDetail(productId)
        productDetail?.let {
            isLiked = it.isLiked
        }
        isLoading = false
    }

    if (isLoading) {
        // 로딩 중일 때 표시할 UI
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (productDetail != null) {
        val pagerState = rememberPagerState(pageCount = { productDetail!!.imageUrls.size })

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("물품교환 공개 상세", fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = { /* 뒤로 가기 액션 */ }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* 메뉴 액션 */ }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = { /* 교환 요청하기 액션 */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("교환 요청하기", color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // 이미지 슬라이더
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // 이미지 슬라이더 높이
                ) {
                    HorizontalPager(state = pagerState) { page ->
                        Image(
                            painter = rememberAsyncImagePainter(productDetail!!.imageUrls[page]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    // 이미지 인덱스 표시
                    Text(
                        text = "${pagerState.currentPage + 1}/${pagerState.pageCount}",
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // 상품 정보 섹션
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = productDetail!!.name,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // 좋아요 버튼
                        IconButton(onClick = { isLiked = !isLiked }) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.FavoriteBorder else Icons.Filled.FavoriteBorder, // TODO: Filled.Favorite 아이콘으로 변경 필요
                                contentDescription = "Like",
                                tint = if (isLiked) Color.Red else Color.Gray
                            )
                        }
                        // 더보기 버튼
                        IconButton(onClick = { /* 더보기 액션 */ }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "상품 상태",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = productDetail!!.status,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "희망 카테고리",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = productDetail!!.desiredCategories.joinToString(", "),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = "Views", modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = productDetail!!.views.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        // 댓글 아이콘 (피그마에 없지만 일반적인 UI에 포함)
                        // Icon(Icons.Default.Comment, contentDescription = "Comments", modifier = Modifier.size(16.dp), tint = Color.Gray)
                        // Spacer(modifier = Modifier.width(4.dp))
                        Text(text = productDetail!!.comments.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Likes", modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = productDetail!!.likes.toString(), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }

                    Divider(modifier = Modifier.padding(vertical = 16.dp))

                    // 상품 상세 설명
                    Text(
                        text = productDetail!!.description,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = productDetail!!.deadline,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.End)
                    )

                    Divider(modifier = Modifier.padding(vertical = 16.dp))

                    // 판매자 정보 (피그마에 없지만 일반적인 UI에 포함)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(productDetail!!.sellerProfileImage),
                            contentDescription = "Seller Profile",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.Gray),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = productDetail!!.sellerName, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    } else {
        // 데이터 로딩 실패 또는 데이터 없음
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("상품 정보를 불러오지 못했습니다.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BarterDetailScreenPreview() {
    MaterialTheme {
        BarterDetailScreen(productId = "sample_product_id")
    }
}
