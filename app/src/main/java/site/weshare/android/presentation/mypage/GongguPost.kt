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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import site.weshare.android.R
import site.weshare.android.model.GongguPostItem
import site.weshare.android.ui.theme.KachiAndroidTheme
import site.weshare.android.ui.theme.TextGray
import site.weshare.android.ui.theme.WeShareGreen


 @OptIn(ExperimentalMaterial3Api::class) @Composable
fun GongguPost(navController: NavController) {
    val context = LocalContext.current

    // 예시 데이터
    val gongguItems = listOf(
        GongguPostItem(
            id = "1",
            imageUrl = R.drawable.ic_icetea, // 이미지 이름
            name = "티오 아이스티 복숭아맛",
            totalPurchaseCount = 160,
            participantCount = 4,
            myPurchaseCount = 15,
            status = "진행 중",
            date = "2025.07.10",
            displayCountOverlay = "40" // "40" 또는 "x4"
        ),
        GongguPostItem(
            id = "2",
            imageUrl = R.drawable.gonggu_samdasu, // 이미지 이름
            name = "제주삼다수 2L",
            totalPurchaseCount = 24,
            participantCount = 5,
            myPurchaseCount = 6,
            status = "진행 중",
            date = "2025.07.07",
            displayCountOverlay = "24개"
        ),
        GongguPostItem(
            id = "3",
            imageUrl = R.drawable.ic_toothpaste, // 이미지 이름
            name = "페리오 뉴 후레쉬 플러스치약",
            totalPurchaseCount = 96,
            participantCount = 30,
            myPurchaseCount = 3,
            status = "완료",
            date = "2025.06.08",
            displayCountOverlay = "30개" // 스크린샷에는 없지만, 일관성을 위해 추가
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
                            text = "나의 공동구매 게시글",
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
        containerColor = Color.White // 전체 배경색
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
                            GongguPostCard(item = item, onClick = {
                                Toast.makeText(context, "공동구매 게시글 클릭: ${item.name}", Toast.LENGTH_SHORT).show()
                                // TODO: 게시글 상세 화면으로 이동
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
fun GongguPostCard(item: GongguPostItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // 이미지 및 오버레이
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(id = item.imageUrl),
                contentDescription = item.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
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
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 정보 섹션
        val detailTextStyle = androidx.compose.ui.text.TextStyle(
            lineHeight = 12.sp,
            platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false)
        )

        Text(
            text = item.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            style = androidx.compose.ui.text.TextStyle(platformStyle = androidx.compose.ui.text.PlatformTextStyle(includeFontPadding = false))
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
                color = if (item.status == "진행 중") WeShareGreen else TextGray,
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

 @Preview(showBackground = true)
 @Composable
fun GongguPostPreview() {
    KachiAndroidTheme {
        GongguPost(rememberNavController())
    }
}