package site.weshare.android.presentation.productdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R
import site.weshare.android.presentation.Barter2.MainScreen
import site.weshare.android.presentation.gonggu.GongguItem

// 물품교환 상세 데이터 모델
data class ProductDetailItem(
    val id: Int,
    val title: String,
    val category: String,
    val location: String,
    val timeAgo: String,
    val description: String,
    val imageRes: Int? = null,
    val isLiked: Boolean = false,
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val chatCount: Int = 0,
    val relatedImages: List<Int> = emptyList(),
    val exchangeOptions: List<ExchangeOption> = emptyList(),
    val sellerProducts: List<SellerProduct> = emptyList()
)

data class ExchangeOption(
    val id: Int,
    val name: String,
    val description: String,
    val imageRes: Int? = null
)

data class SellerProduct(
    val id: Int,
    val title: String,
    val imageRes: Int
)

// GongguItem을 ProductDetailItem으로 변환
fun GongguItem.toProductDetail(): ProductDetailItem {
    return ProductDetailItem(
        id = this.id,
        title = this.title,
        category = "스포츠, 레저", // 기본값
        location = "서울시 중랑구", // 기본값
        timeAgo = "${this.daysLeft}일 전",
        description = this.description,
        imageRes = this.imageRes,
        isLiked = false,
        viewCount = this.views,
        likeCount = this.likes,
        chatCount = this.comments,
        relatedImages = listOf(
            R.drawable.busan, R.drawable.shampoo, R.drawable.gamebook, R.drawable.watch
        ),
        exchangeOptions = listOf(
            ExchangeOption(1, "축구용품", "축구화이드로 등등 유니폼 어센틱", R.drawable.busan),
            ExchangeOption(2, "서울시1", "축구화이드로등등", R.drawable.shampoo),
            ExchangeOption(3, "서울시2", "축구화는텍", R.drawable.gamebook)
        )
    )
}

// Mock 데이터 함수
private fun getMockProductDetailData(productId: Int): ProductDetailItem {
    val commonSellerProducts = listOf(
        SellerProduct(1, "엔젤리오스 컵", R.drawable.busan),
        SellerProduct(2, "정품 LG트윈스", R.drawable.shampoo),
        SellerProduct(3, "(새상품) 스탠", R.drawable.gamebook),
        SellerProduct(4, "롯데자이언츠 옴", R.drawable.watch),
        SellerProduct(5, "사용자 1", R.drawable.polo),
        SellerProduct(6, "롯데자이언츠 옴", R.drawable.rkausfkdlej)
    )

    return when (productId) {
        1 -> ProductDetailItem(
            id = 1,
            title = "롯데자이언츠 동백 유니폼 어센틱",
            category = "스포츠, 레저",
            location = "서울시 중랑구",
            timeAgo = "1일 전",
            description = "사이즈 XL이고 상태 좋아요\n실착 2번 정도에요",
            imageRes = R.drawable.busan,
            isLiked = false,
            viewCount = 43,
            likeCount = 5,
            chatCount = 13,
            // ✅ 1번에는 관련이미지 있음
            relatedImages = listOf(
                R.drawable.dpfwl,
                R.drawable.dpfwl2,
                R.drawable.rnlzkf,
                R.drawable.rldk
            ),
            exchangeOptions = listOf(
                ExchangeOption(1, "축구용품", "축구화이드로 등등 유니폼 어센틱", R.drawable.busan),
                ExchangeOption(2, "서울시1", "축구화이드로등등", R.drawable.shampoo),
                ExchangeOption(3, "서울시2", "축구화는텍", R.drawable.gamebook)
            ),
            sellerProducts = commonSellerProducts
        )
        2 -> ProductDetailItem(
            id = 2,
            title = "프리미에 디칼시파잉 방 케라스타즈 샴푸 3개",
            category = "뷰티/미용",
            location = "서울시 강남구",
            timeAgo = "3시간 전",
            description = "새 제품이고 3개 세트입니다\n미개봉 상태예요",
            imageRes = R.drawable.shampoo,
            isLiked = false,
            viewCount = 32,
            likeCount = 3,
            chatCount = 6,
            relatedImages = listOf(R.drawable.shampoo, R.drawable.busan, R.drawable.watch, R.drawable.polo),
            exchangeOptions = listOf(
                ExchangeOption(1, "헤어케어", "다른 브랜드 샴푸도 괜찮아요", R.drawable.shampoo),
                ExchangeOption(2, "뷰티용품", "스킨케어 제품", R.drawable.gamebook),
                ExchangeOption(3, "생활용품", "기타 생활용품", R.drawable.watch)
            ),
            sellerProducts = commonSellerProducts
        )
        3 -> ProductDetailItem(
            id = 3,
            title = "레노버 게이밍 노트북 Legion 5 15arh6",
            category = "디지털기기",
            location = "서울시 송파구",
            timeAgo = "2일 전",
            description = "게임용으로 구매했는데 사용 빈도가 낮아요\n스펙 좋고 깔끔한 상태입니다",
            imageRes = R.drawable.gamebook,
            isLiked = false,
            viewCount = 56,
            likeCount = 6,
            chatCount = 10,
            // ✅ 3번에는 관련이미지 있음
            relatedImages = listOf(
                R.drawable.rlxk,
                R.drawable.zlqhem,
                R.drawable.polo,
                R.drawable.rkausfkdlej
            ),
            exchangeOptions = listOf(
                ExchangeOption(1, "데스크탑", "게이밍 데스크탑으로 교환", R.drawable.gamebook),
                ExchangeOption(2, "태블릿", "아이패드나 갤탭", R.drawable.watch),
                ExchangeOption(3, "모니터", "게이밍 모니터", R.drawable.polo)
            ),
            sellerProducts = commonSellerProducts
        )
        4 -> ProductDetailItem(
            id = 4,
            title = "태그호이어 링크 청판 CBC2112",
            category = "패션/의류",
            location = "서울시 마포구",
            timeAgo = "2일 전",
            description = "정품이고 구매한지 1년 정도 됐어요\n스크래치 거의 없는 깔끔한 상태입니다",
            imageRes = R.drawable.watch,
            isLiked = false,
            viewCount = 67,
            likeCount = 9,
            chatCount = 23,
            relatedImages = listOf(R.drawable.watch, R.drawable.polo, R.drawable.rkausfkdlej, R.drawable.busan),
            exchangeOptions = listOf(
                ExchangeOption(1, "명품시계", "다른 브랜드 시계", R.drawable.watch),
                ExchangeOption(2, "액세서리", "명품 액세서리", R.drawable.polo),
                ExchangeOption(3, "전자제품", "고가 전자제품", R.drawable.gamebook)
            ),
            sellerProducts = commonSellerProducts
        )
        5 -> ProductDetailItem(
            id = 5,
            title = "(신상) 폴로 슬림핏 린넨셔츠 L",
            category = "패션/의류",
            location = "서울시 용산구",
            timeAgo = "2일 전",
            description = "새 상품이고 택 달린 상태입니다\n사이즈가 안 맞아서 교환하려고 해요",
            imageRes = R.drawable.polo,
            isLiked = false,
            viewCount = 89,
            likeCount = 7,
            chatCount = 15,
            relatedImages = listOf(R.drawable.polo, R.drawable.rkausfkdlej, R.drawable.busan, R.drawable.shampoo),
            exchangeOptions = listOf(
                ExchangeOption(1, "의류", "같은 브랜드 M사이즈", R.drawable.polo),
                ExchangeOption(2, "캐주얼", "다른 캐주얼 의류", R.drawable.rkausfkdlej),
                ExchangeOption(3, "액세서리", "패션 액세서리", R.drawable.watch)
            ),
            sellerProducts = commonSellerProducts
        )
        6 -> ProductDetailItem(
            id = 6,
            title = "가면라이더 리바이스 데몬즈 세트",
            category = "취미/수집",
            location = "서울시 노원구",
            timeAgo = "3일 전",
            description = "피규어 상태 좋고 박스도 있어요\n취미 바뀌어서 정리하려고 합니다",
            imageRes = R.drawable.rkausfkdlej,
            isLiked = false,
            viewCount = 28,
            likeCount = 4,
            chatCount = 8,
            relatedImages = listOf(R.drawable.rkausfkdlej, R.drawable.busan, R.drawable.shampoo, R.drawable.gamebook),
            exchangeOptions = listOf(
                ExchangeOption(1, "피규어", "다른 시리즈 피규어", R.drawable.rkausfkdlej),
                ExchangeOption(2, "게임", "닌텐도 게임", R.drawable.gamebook),
                ExchangeOption(3, "만화책", "관련 만화책이나 소설", R.drawable.polo)
            ),
            sellerProducts = commonSellerProducts
        )
        else -> ProductDetailItem(
            id = productId,
            title = "상품을 찾을 수 없습니다",
            category = "기타",
            location = "알 수 없음",
            timeAgo = "0일 전",
            description = "해당 상품을 찾을 수 없습니다.",
            imageRes = null,
            isLiked = false,
            viewCount = 0,
            likeCount = 0,
            chatCount = 0,
            relatedImages = emptyList(),
            exchangeOptions = emptyList(),
            sellerProducts = emptyList()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int = 1,
    onBackClick: () -> Unit = {},
    onExchangeClick: () -> Unit = {}
) {
    var isLiked by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // 해당 상품 데이터 로드
    val product = remember(productId) { getMockProductDetailData(productId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "더보기",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.height(80.dp)
            ) {
                Button(
                    onClick = onExchangeClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2FB475)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "교환 요청하기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .verticalScroll(scrollState)
        ) {
            // 메인 이미지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(Color.Gray.copy(alpha = 0.2f))
            ) {
                product.imageRes?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } ?: Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "상품 이미지",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            // 상품 정보
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 제목과 하트
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = product.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    IconButton(
                        onClick = { isLiked = !isLiked },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "찜하기",
                            modifier = Modifier.size(24.dp),
                            tint = if (isLiked) Color.Red else Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(9.dp))

                // 상품 상태와 희망 카테고리
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 상품 상태
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "상품 상태",
                            fontSize = 15.sp,
                            color = Color(0xff787878),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(110.dp)
                        )

                        Text(
                            text = if (product.id == 5) "새 상품" else "사용감 없음",
                            fontSize = 14.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // 희망 카테고리
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "희망 카테고리",
                            fontSize = 15.sp,
                            color = Color(0xff787878),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(110.dp)
                        )

                        Text(
                            text = when (product.id) {
                                1 -> "스포츠, 의류"
                                2 -> "뷰티/미용, 도서/티켓/음반"
                                3 -> "디지털기기, 게임"
                                4 -> "의류, 디지털기기"
                                5 -> "의류"
                                6 -> "피규어/인형, 디지털기기"
                                else -> "기타"
                            },
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 통계 정보
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.timeAgo,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.Gray
                        )
                        Text(
                            text = " ${product.viewCount}",
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
                            text = " ${product.likeCount}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "💬 ${product.chatCount}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 설명
                Text(
                    text = product.description,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 관련 이미지들
                if (product.relatedImages.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 0.dp)
                    ) {
                        items(product.relatedImages) { imageRes ->
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.Gray.copy(alpha = 0.2f))
                            ) {
                                Image(
                                    painter = painterResource(id = imageRes),
                                    contentDescription = "관련 이미지",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 판매자 정보와 물건 그리드

                }
                Spacer(modifier = Modifier.height(100.dp)) // 하단 바 여백
            }
        }
    }


@Composable
fun SellerProductItem(
    product: SellerProduct,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.2f))
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = product.title,
            fontSize = 10.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
    }
}

@Composable
fun ExchangeOptionItem(
    option: ExchangeOption,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            option.imageRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = option.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?: Icon(
                Icons.Default.Person,
                contentDescription = option.name,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = option.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = option.description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// MainScreen과 연결하기 위한 통합 컴포넌트
@Composable
fun ProductDetailContainer(
    productId: Int,
    onBackClick: () -> Unit = {},
    onExchangeClick: () -> Unit = {}
) {
    ProductDetailScreen(
        productId = productId,
        onBackClick = onBackClick,
        onExchangeClick = onExchangeClick
    )
}

// MainScreen에서 사용할 수 있도록 상품 리스트와 상세 화면을 관리하는 컴포넌트
@Composable
fun MainScreenWithDetail(
    initialProductId: Int? = null,
    onNavigateBack: () -> Unit = {}
) {
    var currentProductId by remember { mutableStateOf<Int?>(initialProductId) }

    if (currentProductId != null) {
        // 상품 상세 화면 표시
        ProductDetailContainer(
            productId = currentProductId!!,
            onBackClick = {
                currentProductId = null
                onNavigateBack()
            },
            onExchangeClick = {
                // 교환 요청 로직
                println("교환 요청 - Product ID: $currentProductId")
            }
        )
    } else {
        // 메인 화면 표시 (기존 MainScreen 호출)
        MainScreen(
            onItemClick = { item ->
                currentProductId = item.id
            }
        )
    }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailScreenPreview() {
    MaterialTheme {
        ProductDetailScreen(productId = 1)
    }
}