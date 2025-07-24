@file:Suppress("DEPRECATION")

package site.weshare.android.presentation.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyListState
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
//import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay
import site.weshare.android.R
import site.weshare.android.data.MockData
import site.weshare.android.model.ExchangeProduct
import site.weshare.android.model.GroupPurchaseProduct
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp

//import androidx.compose.ui.unit.toPx // toPx()를 사용하기 위해 필요


@Composable
fun HomeScreen() {
    val adImages = listOf(
        R.drawable.adv_1 // 광고 이미지 리소스 ID
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
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
                .size(width = 45.dp, height = 20.dp)
                .clickable { onMoreClick() }
        )
    }
}



@Composable
fun <T> HorizontalProductList(
    products: List<T>,
    itemWidth: Dp = 110.dp,                      // 기본값
    itemContent: @Composable (T) -> Unit
) {
    // 1) LazyRow 상태 객체
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxWidth()) {

        // 2) 가로 스크롤 상품 리스트
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(products) { item ->
                Box(Modifier.width(itemWidth)) {
                    itemContent(item)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // 3) 커스텀 인디케이터
        LazyRowSlideBarIndicator(
            listState = listState,
            itemWidth = itemWidth,
            itemSpacing = 10.dp,
            itemCount = products.size,
            modifier = Modifier
                .padding(horizontal = 160.dp)          // 좌우 여백 조정
                .align(Alignment.CenterHorizontally)  // 가운데 정렬
        )
    }
}

/**
 * LazyRow용 슬라이드‑바 인디케이터
 */
@Composable
fun LazyRowSlideBarIndicator(
    listState: LazyListState,
    itemWidth: Dp,
    itemSpacing: Dp,
    itemCount: Int,
    modifier: Modifier = Modifier,
    height: Dp = 6.dp,
    trackColor: Color = Color(0xFFE0E0E0),
    barColor: Color = Color.DarkGray,
) {
    val density = LocalDensity.current
    // 진행률(0f‒1f) 계산 ---------------------------------------------------
    val progress by remember (
        listState, density, itemWidth, itemSpacing, itemCount   // ② key에 포함
    ){
        derivedStateOf {
            val viewportWidthPx =
                listState.layoutInfo.viewportEndOffset - listState.layoutInfo.viewportStartOffset

            val itemWidthPx  = with(density) { itemWidth.roundToPx() }
            val spacingPx    = with(density) { itemSpacing.roundToPx() }

            val totalContentWidth =
                (itemWidthPx + spacingPx) * itemCount - spacingPx

            val firstIndex  = listState.firstVisibleItemIndex
            val firstOffset = listState.firstVisibleItemScrollOffset
            val scrolledPx  = (itemWidthPx + spacingPx) * firstIndex + firstOffset

            if (totalContentWidth <= viewportWidthPx) 0f
            else (scrolledPx / (totalContentWidth - viewportWidthPx).toFloat())
                .coerceIn(0f, 1f)
        }
    }
    val animatedProgress by animateFloatAsState(progress, label = "slideBarAnim")

    // UI -----------------------------------------------------------------
    BoxWithConstraints(
        modifier
            .fillMaxWidth()
            .height(height)
    ) {
        val fullWidth = constraints.maxWidth.toFloat()

        // 뒤쪽 트랙
        Box(
            Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(50))
                .background(trackColor)
        )
        // 앞쪽 바
        Box(
            Modifier
                .height(height)
                .width(with(LocalDensity.current) { (fullWidth * animatedProgress).toDp() })
                .clip(RoundedCornerShape(50))
                .background(barColor)
        )
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
                .size(95.dp)
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
                .size(95.dp)
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