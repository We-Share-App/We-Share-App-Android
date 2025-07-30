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
import androidx.compose.runtime.*
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
import site.weshare.android.model.PostingItem
import site.weshare.android.ui.theme.DividerGray
import site.weshare.android.ui.theme.KachiAndroidTheme
import site.weshare.android.ui.theme.TextGray
import site.weshare.android.ui.theme.WeShareGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemExchangePosting(navController: NavController) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableStateOf(0) }

    // 예시 데이터
    val postingItems = listOf(
        PostingItem(
            id = "1",
            detail = ExchangeItemDetail(
                imageUrl = R.drawable.img_exchange_lotte_giants,
                name = "롯데자이언츠 동백 유니폼 어센틱",
                itemStatus = "사용감 없음",
                desiredCategory = "희망 카테고리",
                category = "스포츠, 의류"
            ),
            exchangeStatus = "교환 진행 중",
            likesCount = 13
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {
                            navController.popBackStack()
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
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedTabIndex = 0 }
                ) {
                    Text(
                        text = "공개 물품교환",
                        fontSize = 16.sp,
                        fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTabIndex == 0) Color.Black else TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = if (selectedTabIndex == 0) Color.Black else Color.Transparent,
                        thickness = 2.dp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTabIndex = 1
                            navController.navigate("itemExchangeNominee")
                        }
                ) {
                    Text(
                        text = "교환 후보",
                        fontSize = 16.sp,
                        fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal,
                        color = if (selectedTabIndex == 1) Color.Black else TextGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = if (selectedTabIndex == 1) Color.Black else Color.Transparent,
                        thickness = 2.dp
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(postingItems) { item ->
                    PostingCard(item = item, onClick = {
                        Toast.makeText(context, "게시글 클릭: ${item.detail.name}", Toast.LENGTH_SHORT).show()
                        // TODO: 게시글 상세 화면으로 이동
                    })
                    Divider(color = DividerGray, thickness = 1.dp, modifier = Modifier.padding(vertical = 1.dp))
                }
            }
        }
    }
}

@Composable
fun PostingCard(item: PostingItem, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color.White)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.detail.imageUrl),
            contentDescription = item.detail.name,
            modifier = Modifier
                .size(110.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.detail.name,
                    fontSize = 15.sp,
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
            Text(text = "상품 상태: ${item.detail.itemStatus}", fontSize = 11.sp, color = TextGray)
            Text(text = "${item.detail.desiredCategory} : ${item.detail.category}", fontSize = 11.sp, color = TextGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.exchangeStatus,
                    fontSize = 12.sp,
                    color = WeShareGreen,
                    fontWeight = FontWeight.Bold
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

@Preview(showBackground = true)
@Composable
fun ItemExchangePostingPreview() {
    KachiAndroidTheme {
        ItemExchangePosting(rememberNavController())
    }
}