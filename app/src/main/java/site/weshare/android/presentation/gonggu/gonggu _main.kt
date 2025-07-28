package site.weshare.android.presentation.gonggu

import site.weshare.android.R
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val tag: String? = null
)

// 메인 앱 컴포저블 - 화면 전환 관리
@Composable
fun GongguApp() {
    var selectedItem by remember { mutableStateOf<GongguItem?>(null) }

    if (selectedItem == null) {
        // 메인 화면
        GongguMainScreen(
            onItemClick = { item ->
                selectedItem = item // 아이템 클릭하면 상세 화면으로
            }
        )
    } else {
        // 상세 화면
        GongguDetailScreen(
            item = selectedItem!!,
            onBackClick = {
                selectedItem = null // 뒤로가기 버튼 누르면 메인으로
            }
        )
    }
}

// 상세 화면 추가
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguDetailScreen(
    item: GongguItem,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 바
        TopAppBar(
            title = { Text("공구 상세") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // 상세 내용
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 상품 이미지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (item.imageRes != null) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 제목
            Text(
                text = item.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 설명
            Text(
                text = item.description,
                fontSize = 16.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 진행 상황
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "공구 진행 상황",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("현재 참여: ${item.currentCount}명")
                        Text("목표: ${item.maxCount}명")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 진행률 바
                    LinearProgressIndicator(
                        progress = item.currentCount.toFloat() / item.maxCount.toFloat(),
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xff2FB475)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "마감까지 ${item.daysLeft}일 남음",
                        color = if (item.daysLeft <= 3) Color.Red else Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 좋아요, 댓글 수
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.account_multiple),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = " ${item.likes}명 참여",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = " ${item.comments}개 댓글",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 참여하기 버튼
            Button(
                onClick = { /* 참여 로직 */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff2FB475)
                )
            ) {
                Text(
                    text = "공구 참여하기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguMainScreen(
    onItemClick: (GongguItem) -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val gongguItems = remember {
        listOf(
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 7.dp)
    ) {
        // 상단 헤더
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "흑석동",
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
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Search, contentDescription = "검색")
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "찜")
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Notifications, contentDescription = "알림")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // 필터 버튼들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                onClick = {},
                label = { Text("전체", fontSize = 12.sp) },
                selected = true
            )
            FilterChip(
                onClick = {},
                label = { Text("거리", fontSize = 12.sp) },
                selected = false
            )
            FilterChip(
                onClick = {},
                label = { Text("결제", fontSize = 12.sp) },
                selected = false
            )
            FilterChip(
                onClick = {},
                label = { Text("카테고리", fontSize = 12.sp) },
                selected = false
            )
        }

        // 상품 개수와 정렬
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "상품 127",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "최신순",
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

        // 상품 목록
        Box(
            modifier = Modifier.weight(1f)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 16.dp)
            ) {
                items(gongguItems) { item ->
                    GongguItemCard(
                        item = item,
                        onClick = { onItemClick(item) }
                    )
                    if (item != gongguItems.last()) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 0.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = onRegisterClick,
                containerColor = Color(0xff2FB475),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 34.dp, bottom = 95.dp)
                    .size(width = 100.dp, height = 40.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
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
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            if (item.imageRes != null) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
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
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.offset(x = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.account_multiple),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = " ${item.likes}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = " ${item.comments}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        IconButton(
            onClick = {},
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "점 세개",
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
        }
    }
}

// Preview에서는 GongguApp을 호출하도록 변경
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GongguMainScreenPreview() {
    MaterialTheme {
        GongguApp() // 메인 화면 대신 전체 앱 호출
    }
}