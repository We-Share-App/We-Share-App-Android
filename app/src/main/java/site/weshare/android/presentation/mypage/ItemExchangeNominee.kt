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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import site.weshare.android.R
import site.weshare.android.model.ExchangeItemDetail
import site.weshare.android.model.NomineeItem
import site.weshare.android.ui.theme.DividerGray
import site.weshare.android.ui.theme.KachiAndroidTheme
import site.weshare.android.ui.theme.TextGray
import site.weshare.android.ui.theme.WeShareGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemExchangeNominee(navController: NavController) {
    val context = LocalContext.current

    // 예시 데이터
    val nomineeItems = listOf(
        NomineeItem(
            id = "1",
            detail = ExchangeItemDetail(
                imageUrl = R.drawable.ic_new_balance_990v5, // 이미지 이름 변경
                name = "뉴발란스 990v5 트리플블랙 USA eu43 275mm",
                itemStatus = "사용감 없음",
                desiredCategory = "희망 카테고리",
                category = "스포츠, 신발"
            ),
            exchangeStatus = "교환 완료",
            likesCount = 3,
            postingDate = "2025.07.07"
        ),
        NomineeItem(
            id = "2",
            detail = ExchangeItemDetail(
                imageUrl = R.drawable.ic_exchange_clock, // 이미지 이름 변경
                name = "빈티지 IWC 스퀘어 오토매틱 여성 18mm",
                itemStatus = "사용감 없음",
                desiredCategory = "희망 카테고리",
                category = "의류, 뷰티/미용" // 스크린샷 텍스트에 맞춤
            ),
            exchangeStatus = "교환 대기 중",
            likesCount = 3
        ),
        NomineeItem(
            id = "3",
            detail = ExchangeItemDetail(
                imageUrl = R.drawable.ic_exchange_pokemon, // 이미지 이름 변경
                name = "포켓몬 가이스트 고스트 팬텀 제일복권 B상 따라큐 무드등 미개봉",
                itemStatus = "새상품", // 스크린샷 텍스트에 맞춤
                desiredCategory = "희망 카테고리",
                category = "피규어/인형"
            ),
            exchangeStatus = "보관 중",
            likesCount = 3
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
                            text = "나의 물품교환 게시글",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            // "공개 물품교환" / "교환 후보" 탭 섹션
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // "공개 물품교환" (선택 안됨)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            // TODO: 네비게이션 로직 구현
                            Toast.makeText(context, "공개 물품교환 클릭", Toast.LENGTH_SHORT).show()
                        }
                ) {
                    Text(
                        text = "공개 물품교환",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Transparent, // 선택 안된 탭 밑줄 없음
                        thickness = 2.dp
                    )
                }

                // "교환 후보" (선택됨)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { Toast.makeText(context, "교환 후보 클릭", Toast.LENGTH_SHORT).show() }
                ) {
                    Text(
                        text = "교환 후보",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black, // 선택된 탭 밑줄
                        thickness = 2.dp
                    )
                }
            }

            // 스크롤 가능한 교환 후보 목록
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(nomineeItems) { item ->
                    NomineeCard(item = item, onClick = {
                        Toast.makeText(context, "교환 후보 클릭: ${item.detail.name}", Toast.LENGTH_SHORT).show()
                        // TODO: 교환 후보 상세 화면으로 이동
                    })
                    // 각 아이템 사이에 디바이더 추가
                    Divider(color = DividerGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 1.dp))
                }
            }
        }
    }
}

@Composable
fun NomineeCard(item: NomineeItem, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(vertical = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 좌측 이미지
        Image(
            painter = painterResource(id = item.detail.imageUrl),
            contentDescription = item.detail.name,
            modifier = Modifier
                .size(110.dp) // 이미지 크기 조정
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentScale = ContentScale.Crop // 이미지 비율을 채우도록
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 중앙 정보 섹션
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.detail.name,
                    fontSize = 15.sp, // 스크린샷과 유사하게 조정
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Icon(
                    painter = painterResource(id = R.drawable.dots_vertical),
                    contentDescription = "더보기",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { Toast.makeText(context, "더보기 클릭", Toast.LENGTH_SHORT).show() },
                    tint = TextGray
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "상품 상태: ${item.detail.itemStatus}", fontSize = 13.sp, color = TextGray)
            Text(text = "${item.detail.desiredCategory} : ${item.detail.category}", fontSize = 13.sp, color = TextGray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.exchangeStatus,
                    fontSize = 12.sp,
                    color = getStatusColor(item.exchangeStatus), // 상태에 따른 색상 변경
                    fontWeight = FontWeight.Medium
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.heart),
                        contentDescription = "좋아요",
                        modifier = Modifier.size(16.dp),
                        tint = TextGray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = item.likesCount.toString(), fontSize = 11.sp, color = TextGray)
                }
            }
        }
    }
}

@Composable
private fun getStatusColor(status: String): Color {
    return when (status) {
        "교환 진행 중", "교환 완료", "교환 대기 중", "보관 중" -> WeShareGreen // 스크린샷에 맞춰 초록색으로 통일
        else -> Color.Black
    }
}

@Preview(showBackground = true)
@Composable
fun ItemExchangeNomineePreview() {
    KachiAndroidTheme {
        ItemExchangeNominee(rememberNavController())
    }
}