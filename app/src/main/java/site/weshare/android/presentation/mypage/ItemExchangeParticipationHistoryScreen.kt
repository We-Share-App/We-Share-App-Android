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
import site.weshare.android.model.ExchangeItemDetail
import site.weshare.android.model.ItemExchange
import site.weshare.android.model.ItemExchangeStatus
import site.weshare.android.ui.theme.DividerGray
import site.weshare.android.ui.theme.KachiAndroidTheme
import site.weshare.android.ui.theme.TextGray


 @OptIn(ExperimentalMaterial3Api::class) @Composable
fun ItemExchangeParticipationHistoryScreen(navController: NavController) {
    val context = LocalContext.current

    // 예시 데이터
    val exchangeItems = listOf(
        ItemExchange(
            id = "1",
            status = ItemExchangeStatus.IN_PROGRESS,
            myItem = ExchangeItemDetail(
                imageUrl = R.drawable.img_exchange_lotte_giants,
                name = "롯데자이언츠 동백 유니폼",
                itemStatus = "사용감 없음",
                desiredCategory = "희망 카테고리",
                category = "스포츠, 의류"
            ),
            targetItem = ExchangeItemDetail(
                imageUrl = R.drawable.ic_jersey_blue,
                name = "롯데자이언츠 이대호 아디",
                itemStatus = "사용감 없음",
                desiredCategory = "희망 카테고리",
                category = "스포츠, 의류"
            )
        ),
        ItemExchange(
            id = "2",
            status = ItemExchangeStatus.COMPLETED,
            exchangeDate = "2025.05.07",
            myItem = ExchangeItemDetail(
                imageUrl = R.drawable.ic_new_balance_990v5,
                name = "뉴발란스 990v5 트리플블랙",
                itemStatus = "사용감 있음",
                desiredCategory = "희망 카테고리",
                category = "스포츠, 신발"
            ),
            targetItem = ExchangeItemDetail(
                imageUrl = R.drawable.ic_new_balance_993,
                name = "뉴발란스 993 USA 그레이",
                itemStatus = "사용감 없음",
                desiredCategory = "희망 카테고리",
                category = "신발, 의류"
            )
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
                            text = "물품교환 참여 내역",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = { /* 뒤로가기 버튼이 title Box 안으로 이동했으므로 비워둡니다. */ },
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
                .background(Color.White)
        ) {
            val inProgressItems = exchangeItems.filter { it.status == ItemExchangeStatus.IN_PROGRESS }
            val completedItems = exchangeItems.filter { it.status == ItemExchangeStatus.COMPLETED }

            if (inProgressItems.isNotEmpty()) {
                item {
                    Text(
                        text = "교환 진행 중",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2FB475), // 초록색으로 변경
                        modifier = Modifier.padding(start = 26.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
                    )
                }
                items(inProgressItems) { item ->
                    ItemExchangeCard(item = item, onClick = {
                        Toast.makeText(context, "교환 진행 중 항목 클릭: ${item.myItem.name} - ${item.targetItem.name}", Toast.LENGTH_SHORT).show()
                        // TODO: 상세 화면으로 이동
                    })
                    Divider(color = DividerGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 0.dp))
                }
            }

            if (completedItems.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 26.dp, end = 16.dp, top = 16.dp, bottom = 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween, // 날짜를 오른쪽으로 정렬
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "교환 완료",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2FB475) // 초록색으로 변경
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        completedItems.firstOrNull()?.exchangeDate?.let { date ->
                            Text(
                                text = date,
                                fontSize = 12.sp,
                                color = TextGray
                            )
                        }
                    }
                }
                items(completedItems) { item ->
                    ItemExchangeCard(item = item, onClick = {
                        Toast.makeText(context, "교환 완료 항목 클릭: ${item.myItem.name} - ${item.targetItem.name}", Toast.LENGTH_SHORT).show()
                        // TODO: 상세 화면으로 이동
                    })
                    Divider(color = DividerGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 0.dp))
                }
            }
        }
    }
}


 @Composable
fun ItemExchangeCard(item: ItemExchange, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Keep this for overall row alignment
        ) {
            ExchangeItem(detail = item.myItem, modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.ic_exchange_arrow),
                contentDescription = "교환",
                modifier = Modifier
                    .size(55.dp)
                    .padding(horizontal = 8.dp)
                    .offset(y = (-50).dp) // Adjust offset to move up
            )

            ExchangeItem(detail = item.targetItem, modifier = Modifier.weight(1f))
        }
    }
}

 @Composable
fun ExchangeItem(detail: ExchangeItemDetail, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start // 좌정렬
    ) {
        Image(
            painter = painterResource(id = detail.imageUrl),
            contentDescription = detail.name,
            modifier = Modifier
                .size(145.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp)) // Reduced spacing
        Text(text = detail.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black, textAlign = TextAlign.Start) // 좌정렬
        Text(text = "상품 상태: ${detail.itemStatus}", fontSize = 11.sp, color = TextGray, textAlign = TextAlign.Start) // 좌정렬
        Text(text = "[${detail.desiredCategory}]", fontSize = 11.sp, color = TextGray, textAlign = TextAlign.Start) // 좌정렬
        Text(text = detail.category, fontSize = 11.sp, color = TextGray, textAlign = TextAlign.Start) // 좌정렬
    }
}


 @Preview(showBackground = true)
 @Composable
fun ItemExchangeParticipationHistoryScreenPreview() {
    KachiAndroidTheme {
        ItemExchangeParticipationHistoryScreen(rememberNavController())
    }
}