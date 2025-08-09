package site.weshare.android.presentation.gonggu

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
    GongguItem(1, "제주삼다수 2L", "총 구매 개수 : 24개\n남은 개수 : 8개", "25,920원",16,24,10,5,13,43, R.drawable.gonggu_samdasu),
    GongguItem(2, "코카콜라 제로 190ml 60개 공동구매해요~","총 구매 개수 : 60개\n남은 개수 : 15개","48,000원",45,60,8,3,6,32, R.drawable.gonggu_cola),
    GongguItem(3, "리벤스 물티슈 대용량","총 구매 개수 : 80개\n남은 개수 : 17개","35,600원",63,80,4,6,10,56, R.drawable.gonggu_tissue),
    GongguItem(4, "신라면 공구해요","총 구매 개수 : 40개\n남은 개수 : 5개","28,800원",35,40,4,9,23,67, R.drawable.gonggu_ramen),
    GongguItem(5, "자취생 필수품 햇반","총 구매 개수 : 48개\n남은 개수 : 10개","32,400원",38,48,2,7,15,89, R.drawable.gonggu_bab),
    GongguItem(6, "오뚜기 진라면 매운맛","총 구매 개수 : 36개\n남은 개수 : 12개","19,800원",24,36,6,4,8,28, R.drawable.gonggu_jinramen),
    GongguItem(7, "봉쥬르끌레어 캡슐세제","총 구매 개수 : 200개\n남은 개수 : 7개","42,000원",13,20,12,2,4,19, R.drawable.gonggu_seje),
    GongguItem(8, "동원 참치캔","총 구매 개수 : 50개\n남은 개수 : 18개","38,500원",32,50,1,11,26,78, R.drawable.gonggu_dongwon)
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

        // 더보기 메뉴 컴포넌트 사용
//        GongguMoreMenu(
//            onReportClick = { onMenuAction("report") },
//            onShareClick = { onMenuAction("share") },
//            onInquiryClick = { onMenuAction("inquiry") }
//        )
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