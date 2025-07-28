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

import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class GongguItem(
    val id: Int,
    val title: String,
    val description: String,
    val currentCount: Int,
    val maxCount: Int,
    val daysLeft: Int,
    val likes: Int,
    val comments: Int,
    val imageRes: Int? = null, // 실제로는 이미지 URL을 사용하시면 됩니다
    val tag: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguMainScreen(
    navController: NavController,
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
            .padding(start = 7.dp) // 전체를 오른쪽으로 8dp 이동
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
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(0.dp, 0.dp, 0.dp, 80.dp)
        ) {
            items(gongguItems) { item ->
                GongguItemCard(
                    item = item,
                    onClick = { onItemClick(item) }
                )
                // 마지막 아이템이 아닐 때만 구분선 추가
                if (item != gongguItems.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 0.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray
                    )
                }
            }
        }

        // 등록하기 버튼
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
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // 고정 높이 설정
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        // 상품 이미지
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // 이미지가 있으면 표시, 없으면 회색 배경
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

        // 상품 정보
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(), // 전체 높이 사용
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 상단 정보 (제목 + 설명)
            Column {
                Text(
                    text = item.title,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 18.sp,
                    letterSpacing = (-0.05).sp,
                    maxLines = 2, // 최대 2줄로 제한
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

            // 하단 정보 (마감일 + 아이콘들)
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

                // 좋아요, 댓글 수
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

        // 더보기 버튼
        IconButton(
            onClick = {},
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

@Preview(
    showBackground = true,
    showSystemUi = true  // 상태바까지 보고 싶다면
)
@Composable
fun GongguMainScreenPreview() {
    // 테마를 적용하고 싶다면
    MaterialTheme {
        GongguMainScreen(navController = rememberNavController())
    }
}