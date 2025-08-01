package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPriceInfo() {
    var showPriceInfoModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {


        if (showPriceInfoModal) {
            PriceInfoModal(onDismiss = { showPriceInfoModal = false })
        }
    }
}

@Composable
fun PriceInfoModal(
    onDismiss: () -> Unit
) {
    // 투명 배경 + 클릭 시 닫기
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }, contentAlignment = Alignment.Center
    ) {
        // 팝업 카드
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd)    // 가격 아이콘 바로 위에 띄우려면 위치 조정
                .offset(x = (-16).dp, y = 620.dp) // 필요에 따라 x,y 조정
                .size(width = 360.dp, height = 125.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // 상단: info 아이콘 + 제목 + 닫기 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.close),
                        contentDescription = "닫기",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(17.dp)
                            .clickable { onDismiss() }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .horizontalScroll(rememberScrollState()),  // ★화면에 다 안 들어올 때 스크롤
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "등록 상품이 무료 배송 상품이예요 →",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.8).sp,
                        fontSize = 15.sp,
                        // weight 제거!
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Image(
                        painter = painterResource(id = R.drawable.freedelivery),  // PNG 혹은 벡터 원본
                        contentDescription = "무료 배송(모형)",
                        modifier = Modifier
                            .height(30.dp)
                            .wrapContentWidth()
                            .alignByBaseline()  // 텍스트 기준선 맞춤
                    )
                }

                // 예시 문구
                Text(
                    text = "등록 상품의 배송비가 있어요 → 배송비를 입력해주세요",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.8).sp,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PriceInfoModalPreview() {
    MaterialTheme {
        PriceInfoModal(onDismiss = { })
    }
}

