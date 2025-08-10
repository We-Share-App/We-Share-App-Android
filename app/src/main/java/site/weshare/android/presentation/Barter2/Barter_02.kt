package site.weshare.android.presentation.Barter2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R

data class RecommendedItem(
    val title: String,
    val location: String,
    val price: String,
    val imageRes: Int = android.R.drawable.ic_menu_gallery // 더미 이미지
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen() {
    // 이 부분을 찾으세요
    val recommendedItems = listOf(
        RecommendedItem(
            title = "엘지트윈스 검니폼",
            location = "[희망 카테고리]",
            price = " 스포츠, 의류",
            imageRes = R.drawable.dpfwl // ← 여기를 본인 이미지로 변경
        ),
        RecommendedItem(
            title = "정품) LG트윈스 야구",
            location = "[희망 카테고리]",
            price = "스포츠, 의류",
            imageRes = R.drawable.dpfwl2 // ← 여기를 본인 이미지로 변경
        ),
        RecommendedItem(
            title = "귀멸의 칼날 만화책",
            location = "[희망 카테고리]",
            price = " 도서/티켓/음반",
            imageRes = R.drawable.rnlzkf
        // ← 여기를 본인 이미지로 변경
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "메뉴"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Column {
                // 확인 버튼
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2FB475)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "확 인",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 제목 텍스트
            Text(
                text = "내가 원하는\n롯데자이언츠 동백 유니폼 어센틱 에\n교환이 요청되었어요 😊",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                fontSize = 24.sp,
                letterSpacing = (-0.5).sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )

            // 메인 상품 이미지
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.busan),
                    contentDescription = "상품 이미지",
                    modifier = Modifier.size(400.dp),
                    contentScale = ContentScale.Crop
                )
                // 여기에 실제 Busan 유니폼 이미지가 들어갈 예정
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 화살표 아이콘들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_up_float),
                    contentDescription = "위쪽 화살표",
                    tint = Color(0xFF2FB475),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = android.R.drawable.arrow_down_float),
                    contentDescription = "아래쪽 화살표",
                    tint = Color(0xFF2FB475),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 추천 상품들
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recommendedItems) { item ->
                    RecommendedItemCard(item = item)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun RecommendedItemCard(item: RecommendedItem) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .clickable { }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2
        )

        Text(
            text = item.location,
            fontSize = 10.sp,
            color = Color.Gray,
            maxLines = 1
        )

        Text(
            text = item.price,
            fontSize = 10.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}


data class BottomNavItem(
    val label: String,
    val icon: Any // ImageVector 또는 Painter를 받을 수 있도록
)

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    MaterialTheme {
        ProductDetailScreen()
    }
}