package site.weshare.android.presentation.mypage // 패키지명 변경

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
import site.weshare.android.R // R.drawable 리소스를 사용하기 위함
import site.weshare.android.ui.theme.DividerGray // 기존 테마 색상 임포트
import site.weshare.android.ui.theme.IconGray // 기존 테마 색상 임포트
import site.weshare.android.ui.theme.KachiAndroidTheme // 기존 테마명 임포트



// 데이터 모델 정의 (이전과 동일)
data class CommunityPurchase(
    val id: String,
    val name: String,
    val imageUrl: Int, // drawable resource ID
    val totalCount: Int,
    val participantCount: Int,
    val recoveryRate: Int, // 회수율 (퍼센트)
    val reward: Int?, // 리워드가 없는 경우도 있으므로 nullable
    val date: String,
    val statusIconUrl: Int // drawable resource ID for status icon
)

 @OptIn(ExperimentalMaterial3Api::class) @Composable
fun GongguChongdaeHistoryScreen(navController: NavController) {
    val context = LocalContext.current

    // 예시 데이터 (스크린샷 내용 기반)
    val purchaseItems = listOf(
        CommunityPurchase(
            id = "1",
            name = "제주삼다수 2L",
            imageUrl = R.drawable.gonggu_samdasu, // 변경: ic_item_samdasu_2l -> gonggu_samdasu
            totalCount = 24,
            participantCount = 5,
            recoveryRate = 100,
            reward = null, // 스크린샷에서 리워드 없음
            date = "2025.07.07",
            statusIconUrl = R.drawable.ic_status_progress // 진행 중 아이콘
        ),
        CommunityPurchase(
            id = "2",
            name = "제주삼다수 550ml",
            imageUrl = R.drawable.ic_item_samdasu_550ml, // 실제 drawable 파일명 확인
            totalCount = 120,
            participantCount = 12,
            recoveryRate = 100,
            reward = 512,
            date = "2025.05.07",
            statusIconUrl = R.drawable.ic_status_completed // 완료 아이콘
        ),
        CommunityPurchase(
            id = "3",
            name = "트레비 350ml",
            imageUrl = R.drawable.ic_item_trevi_350ml, // 실제 drawable 파일명 확인
            totalCount = 60,
            participantCount = 8,
            recoveryRate = 100,
            reward = 168,
            date = "2025.03.07",
            statusIconUrl = R.drawable.ic_status_completed // 완료 아이콘
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
                            text = "공동구매 총대 내역",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp, // 스크린샷 TopAppBar 제목 크기에 맞춰 조정
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    // 뒤로가기 버튼이 title Box 안으로 이동했으므로 비워둡니다.
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        // bottomBar 제거
        containerColor = Color(0xFFF0F0F0) // 전체 배경색 - 스크린샷 배경색에 맞춰 조정 (나중에 테마 색상으로 변경 권장)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White) // 배경색을 흰색으로 변경
        ) {
            items(purchaseItems) { item ->
                CommunityPurchaseItem(item = item, onClick = {
                    Toast.makeText(context, "${item.name} 항목 클릭", Toast.LENGTH_SHORT).show()
                    // TODO: 상세 화면으로 이동 (예: navController.navigate("purchaseDetail/${item.id}"))
                })
            }
        }
    }
}

 @Composable
fun CommunityPurchaseItem(item: CommunityPurchase, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.White) // 배경색 추가
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 좌측 제품 이미지와 수량 표시
            Box(
                modifier = Modifier
                    .size(140.dp) // 이미지 전체 크기 (스크린샷에 맞게 조정)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White) // 이미지 배경색 (스크린샷에서 보이는 밝은 회색)
            ) {
                Image(
                    painter = painterResource(id = item.imageUrl),
                    contentDescription = item.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit // 이미지 비율 유지
                )
                // 수량 표시 Box
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 4.dp, y = (-4).dp) // 좌하단에 약간 띄워서 배치
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF616161)) // 어두운 회색 배경 (스크린샷과 유사)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${item.totalCount}개",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal // 스크린샷 폰트 두께
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 중앙 정보 섹션
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(0.dp))
                Text(text = "총 구매 개수: ${item.totalCount}", fontSize = 13.sp, color = Color.DarkGray)
                Text(text = "참여 인원: ${item.participantCount}명", fontSize = 13.sp, color = Color.DarkGray)
                Text(text = "회수율: ${item.recoveryRate}%", fontSize = 13.sp, color = Color.DarkGray)
                if (item.reward == null && item.statusIconUrl == R.drawable.ic_status_progress) {
                    Text(text = "진행 중", fontSize = 13.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold) // 진행 중 텍스트 초록색
                } else {
                    item.reward?.let {
                        Text(text = "리워드: $it", fontSize = 13.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold) // 스크린샷 리워드 초록색
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 우측 날짜 및 상태 아이콘
            Column(
                horizontalAlignment = Alignment.End, // 오른쪽 정렬
                verticalArrangement = Arrangement.Center, // 세로 중앙 정렬
                modifier = Modifier.fillMaxHeight() // Column이 Row의 높이를 채우도록
            ) {
                Text(
                    text = item.date,
                    fontSize = 10.sp,
                    color = Color.Gray, // 스크린샷 색상
                    textAlign = TextAlign.End // 텍스트 오른쪽 정렬
                )
                Spacer(modifier = Modifier.height(4.dp)) // 날짜와 아이콘 사이 간격
                Image(
                    painter = painterResource(id = item.statusIconUrl),
                    contentDescription = "상태 아이콘",
                    modifier = Modifier.size(65.dp) // 아이콘 크기 (스크린샷과 유사하게 조정)
                )
            }
        }
        Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = DividerGray) // 구분선 추가
    }
}



 @Preview(showBackground = true)
 @Composable
fun GongguChongdaeHistoryScreenPreview() {
    KachiAndroidTheme { // 테마명 변경 적용
        GongguChongdaeHistoryScreen(rememberNavController())
    }
}