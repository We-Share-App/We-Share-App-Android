package site.weshare.android.presentation.mypage

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import site.weshare.android.R
import site.weshare.android.ui.theme.KachiAndroidTheme
import site.weshare.android.ui.theme.TextGray
import site.weshare.android.ui.theme.WeShareGreen

// 이 화면을 위한 데이터 클래스 정의
data class GongguParticipationItem(
    val id: String,
    val imageUrl: Int,
    val name: String,
    val totalPurchaseCount: Int,
    val participantCount: Int,
    val myPurchaseCount: Int,
    val status: String,
    val date: String,
    val displayCountOverlay: String?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GongguParticipation(navController: NavController) {
    val context = LocalContext.current

    // 예시 데이터 (스크린샷 내용 기반)
    val gongguItems = listOf(
        GongguParticipationItem(
            id = "1",
            imageUrl = R.drawable.gonggu_cola,
            name = "코카콜라 제로 190ml 60개",
            totalPurchaseCount = 60,
            participantCount = 4,
            myPurchaseCount = 15,
            status = "진행 중",
            date = "2025.07.07",
            displayCountOverlay = "60개"
        ),
        GongguParticipationItem(
            id = "2",
            imageUrl = R.drawable.gonggu_ramen,
            name = "신라면 공구해요",
            totalPurchaseCount = 40,
            participantCount = 6,
            myPurchaseCount = 5,
            status = "완료",
            date = "2025.07.04",
            displayCountOverlay = "5개"
        ),
        GongguParticipationItem(
            id = "3",
            imageUrl = R.drawable.ic_orangejuice,
            name = "대사랑 오렌지 드링크, 185ml",
            totalPurchaseCount = 120,
            participantCount = 9,
            myPurchaseCount = 20,
            status = "완료",
            date = "2025.06.08",
            displayCountOverlay = "120개"
        ),
        GongguParticipationItem(
            id = "4",
            imageUrl = R.drawable.ic_capri_sun,
            name = "카프리썬 오렌지망고 200ml",
            totalPurchaseCount = 100,
            participantCount = 3,
            myPurchaseCount = 20,
            status = "취소",
            date = "2025.04.21",
            displayCountOverlay = "100개"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {
                            // TODO: 뒤로가기 동작 구현 (navController.popBackStack() 등)
                            Toast.makeText(context, "뒤로가기 클릭", Toast.LENGTH_SHORT).show()
                        }, modifier = Modifier.align(Alignment.CenterStart)) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "뒤로가기")
                        }
                        Text(
                            text = "공동구매 참여 내역",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(gongguItems.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { item ->
                        Box(modifier = Modifier.weight(1f)) {
                            GongguParticipationCard(item = item, onClick = {
                                Toast.makeText(context, "공동구매 참여 항목 클릭: ${item.name}", Toast.LENGTH_SHORT).show()
                                // TODO: 상세 화면으로 이동
                            })
                        }
                    }
                    if (rowItems.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun GongguParticipationCard(item: GongguParticipationItem, onClick: () -> Unit) {
    val detailTextStyle = androidx.compose.ui.text.TextStyle(
        lineHeight = 12.sp,
        platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = item.imageUrl),
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // 이미지 비율을 유지하며 채우도록 변경
            )
            item.displayCountOverlay?.let {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 4.dp, y = (-4).dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF616161))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "총 구매 개수: ${item.totalPurchaseCount}", fontSize = 11.sp, color = TextGray, style = detailTextStyle)
        Text(text = "참여 인원: ${item.participantCount}명", fontSize = 11.sp, color = TextGray, style = detailTextStyle)
        Text(text = "나의 구매 개수: ${item.myPurchaseCount}개", fontSize = 11.sp, color = TextGray, style = detailTextStyle)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.status,
                fontSize = 11.sp,
                color = getGongguParticipationStatusColor(item.status),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.date,
                fontSize = 11.sp,
                color = TextGray,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun getGongguParticipationStatusColor(status: String): Color {
    return when (status) {
        "진행 중", "완료" -> WeShareGreen
        "취소" -> Color.Red
        else -> TextGray
    }
}

@Preview(showBackground = true)
@Composable
fun GongguParticipationPreview() {
    KachiAndroidTheme {
        GongguParticipation(rememberNavController())
    }
}