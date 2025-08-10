package site.weshare.android.presentation.Barter2

import site.weshare.android.presentation.gonggu.GongguItem

import site.weshare.android.R
import site.weshare.android.presentation.gonggu.components.GongguMoreMenu
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

// ==================== Mock Data ====================
private fun getMockGongguData(): List<GongguItem> = listOf(
    GongguItem(1, "롯데자이언츠 동백 유니폼 어센틱", "상품 상태 : 사용감 없음\n희망 카테고리 : 스포츠, 의류", "25,920원",16,24,1,5,13,43, R.drawable.busan),
    GongguItem(2, "프리미에 디칼시파잉 방 케라스타즈 샴푸 3개","상품 상태 : 사용감 없음\n희망 카테고리 : 뷰티/미용, 도서/티켓/음반","48,000원",45,60,4,3,6,32, R.drawable.shampoo),
    GongguItem(3, "레노버 게이밍 노트북 Legion 5 15arh6","상품 상태 : 사용감 없음\n희망 카테고리 : 디지털기기, 게임","35,600원",63,80,8,6,10,56, R.drawable.gamebook),
    GongguItem(4, "태그호이어 링크 청판 CBC2112","상품 상태 : 사용감 없음\n희망 카테고리 : 의류, 디지털기기","28,800원",35,40,12,9,23,67, R.drawable.watch),
    GongguItem(5, "(신상) 폴로 슬림핏 린넨셔츠 L","상품 상태 : 새 상품\n희망 카테고리 : 의류","32,400원",38,48,20,7,15,89, R.drawable.polo),
    GongguItem(6, "가면라이더 리바이스 데몬즈 세트","상품 상태 : 사용감 없음\n희망 카테고리 : 피규어/인형, 디지털기기","19,800원",24,36,24,4,8,28, R.drawable.rkausfkdlej)
)

// ==================== Product List Body (상품 리스트만) ====================
@Composable
fun GongguProductListBody(
    items: List<GongguItem> = getMockGongguData(),
    isLoading: Boolean = false,
    totalCount: Int = items.size,
    onItemClick: (GongguItem) -> Unit = {},
    onLikeClick: (Int) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onMenuAction: (GongguItem, String) -> Unit = { _, _ -> }
) {
    // 🔥 Box 제거하고 Column만 사용 (FAB는 GongguMainScreen에서 관리)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상품 개수 표시
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("상품 $totalCount", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
                Text("최신순", fontSize = 12.sp, color = Color.Gray)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
            }
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    GongguItemCard(
                        item = item,
                        onClick = { onItemClick(item) },
                        onLikeClick = { onLikeClick(item.id) },
                        onMenuAction = { action -> onMenuAction(item, action) }
                    )

                    if (item != items.last()) {
                        Divider(color = Color.LightGray, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
    // 🔥 FAB 버튼 제거 - GongguMainScreen에서 관리
}

// ==================== Item Card Component ====================
@Composable
fun GongguItemCard(
    item: GongguItem,
    onClick: () -> Unit,
    onLikeClick: () -> Unit = {},
    onMenuAction: (String) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable { onClick() }
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
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
                Text("${item.daysLeft}시간전", fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (item.daysLeft <= 6) Color.Red else Color.Gray
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

        // 🔥 더보기 메뉴 컴포넌트 - 주석 해제하고 수정
        GongguMoreMenu(
            onMenuAction = onMenuAction
        )
    }
}

// ==================== Preview ====================
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GongguProductListBodyPreview() {
    MaterialTheme {
        GongguProductListBody()
    }
}