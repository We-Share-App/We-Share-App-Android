@file:Suppress("DEPRECATION")

package site.weshare.android.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay
import site.weshare.android.R
import site.weshare.android.data.MockData
import site.weshare.android.model.ExchangeProduct
import site.weshare.android.model.GroupPurchaseProduct

@Composable
fun HomeScreen() {
    val adImages = listOf(
        R.drawable.adv_1 // 광고 이미지 리소스 ID
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // 전체 화면 스크롤 가능하도록
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 검색창과 아이콘
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.ui_searchbar),
                contentDescription = "Search Bar",
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = R.drawable.ring),
                contentDescription = "Ring Icon",
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 3초 간격 광고 배너
        val adPagerState = rememberPagerState(pageCount = { adImages.size })

        LaunchedEffect(Unit) {
            while (true) {
                delay(3000)
                val nextPage = (adPagerState.currentPage + 1) % adPagerState.pageCount
                adPagerState.animateScrollToPage(nextPage)
            }
        }

        HorizontalPager(
            state = adPagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp) // 광고 이미지 비율에 맞게 높이 조절
        ) { page ->
            Image(
                painter = painterResource(id = adImages[page]),
                contentDescription = "Advertisement",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
        // 광고 배너 하단 PagerIndicator
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPagerIndicator(
            pagerState = adPagerState,
            pageCount = adImages.size,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            activeColor = Color.Black, // 활성 페이지 색상
            inactiveColor = Color.LightGray, // 비활성 페이지 색상
            indicatorWidth = 8.dp, // 인디케이터 개별 너비
            indicatorHeight = 8.dp, // 인디케이터 개별 높이
            spacing = 4.dp // 인디케이터 간 간격
        )


        // 공동구매 섹션
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(title = "공동구매 가치할래?") {
            // TODO: 추후에 화면 이동 코드를 추가해야 합니다.
            // 공동구매 화면으로 이동하는 로직 (NavHostController 사용)
            // navController.navigate("groupPurchaseScreen")
            println("공동구매 더보기 클릭") // 임시 확인용
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalProductList(products = MockData.groupPurchaseProducts) { product ->
            GroupPurchaseProductItem(product = product)
        }

        // 물품교환 섹션
        Spacer(modifier = Modifier.height(24.dp))
        SectionHeader(title = "물품교환 가치할래?") {
            // TODO: 추후에 화면 이동 코드를 추가해야 합니다.
            // 물품교환 화면으로 이동하는 로직 (NavHostController 사용)
            // navController.navigate("exchangeScreen")
            println("물품교환 더보기 클릭") // 임시 확인용
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalProductList(products = MockData.exchangeProducts) { product ->
            ExchangeProductItem(product = product)
        }
    }
}

@Composable
fun SectionHeader(title: String, onMoreClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)

        // "더보기" 이미지를 클릭 가능하게 만듭니다.
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = "더보기",
            modifier = Modifier
                .size(width = 50.dp, height = 20.dp)
                .clickable { onMoreClick() }
        )
    }
}



@Composable
fun <T> HorizontalProductList(products: List<T>, itemContent: @Composable (T) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { products.size })

    Column(modifier = Modifier.fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            pageSpacing = 16.dp
        ) { page ->
            Box(modifier = Modifier.width(110.dp)) { // 각 아이템의 너비를 지정
                itemContent(products[page])
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // 커스텀 슬라이드 바 Pager Indicator 구현
        if (products.size > 1) { // 페이지가 1개 이상일 때만 인디케이터 표시
            val totalIndicatorWidth = 60.dp
            val indicatorHeight = 4.dp
            val activeIndicatorWidth = totalIndicatorWidth / products.size.toFloat() // 활성 바의 너비 (Float으로 계산)

            Box(
                modifier = Modifier
                    .width(totalIndicatorWidth) // 전체 트랙 너비 고정
                    .height(indicatorHeight) // 트랙 높이 고정
                    .clip(RoundedCornerShape(2.dp)) // 바의 둥근 모서리
                    .background(Color.LightGray) // 비활성 트랙 색상
                    .align(Alignment.CenterHorizontally) // 중앙 정렬
            ) {
                val animatedOffset = pagerState.currentPage + pagerState.currentPageOffsetFraction
                val translateX = animatedOffset * activeIndicatorWidth

                Box(
                    modifier = Modifier
                        .width(activeIndicatorWidth) // 활성 바의 계산된 너비
                        .height(indicatorHeight) // 활성 바의 높이 고정
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Black) // 활성 바 색상
                        .align(Alignment.CenterStart) // 시작점에 정렬
                        .offset(x = translateX) // 계산된 X 오프셋 적용
                )
            }
        }
    }
}

@Composable
fun GroupPurchaseProductItem(product: GroupPurchaseProduct) {
    Column(
        modifier = Modifier
            .width(100.dp) // 아이템의 너비는 HorizontalPager의 contentPadding과 함께 조정
    ) {
        // 이미지는 리소스 ID를 사용하도록 변경
        val imageResId = product.imageUrl.toIntOrNull() ?: R.drawable.ic_launcher_foreground // 기본 이미지
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = product.name,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.name, fontSize = 16.sp, fontWeight = FontWeight.Medium, maxLines = 1)
        Text(text = "총 구매 개수 : ${product.totalQuantity}개", fontSize = 12.sp, color = Color.Gray)
        val remainingItems = product.totalQuantity - product.quantitySold
        Text(text = "남은 개수: ${remainingItems}개", fontSize = 12.sp, color = Color.Gray)

    }
}

@Composable
fun ExchangeProductItem(product: ExchangeProduct) {
    Column(
        modifier = Modifier
            .width(100.dp) // 아이템의 너비는 HorizontalPager의 contentPadding과 함께 조정
    ) {
        // 이미지는 리소스 ID를 사용하도록 변경
        val imageResId = product.imageUrl.toIntOrNull() ?: R.drawable.ic_launcher_foreground // 기본 이미지
        Image(
            painter = painterResource(id = imageResId),
            contentDescription = product.name,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = product.name, fontSize = 16.sp, fontWeight = FontWeight.Medium, maxLines = 1)
        Text(text = product.category, fontSize = 12.sp, color = Color.Gray)
        Text(text = product.exchangeCondition, fontSize = 12.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Preview(showBackground = true)
@Composable
fun SectionHeaderPreview() {
    SectionHeader(title = "공동구매 가치할래?") {}
}

@Preview(showBackground = true)
@Composable
fun GroupPurchaseProductItemPreview() {
    GroupPurchaseProductItem(
        product = GroupPurchaseProduct(
            id = "gp_001",
            imageUrl = R.drawable.img_product_samdasoo.toString(),
            name = "제주삼다수 2L",
            quantitySold = 24,
            totalQuantity = 32,
            remainingDays = 8
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ExchangeProductItemPreview() {
    ExchangeProductItem(
        product = ExchangeProduct(
            id = "ex_001",
            imageUrl = R.drawable.img_exchange_lotte_giants.toString(),
            name = "롯데자이언츠",
            category = "스포츠, 의류",
            exchangeCondition = "희망 카테고리"
        )
    )
}