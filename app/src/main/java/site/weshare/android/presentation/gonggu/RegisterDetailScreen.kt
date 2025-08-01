package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterDetailScreen() {
    var selectedDate by remember { mutableStateOf("2025-07-10") }
    var guestCount by remember { mutableStateOf(0) }
    var myGuestCount by remember { mutableStateOf(0) }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var shareUrl by remember { mutableStateOf("") }
    var showInfoModal by remember { mutableStateOf(false) }
    var showPriceInfoModal by remember { mutableStateOf(false) }
    var showCalendarModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* 닫기 로직 */ }
                )
            }

            // 상품정보 섹션
            Text(
                text = "상품정보",
                fontSize = 17.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // 사진 등록
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "사진 추가",
                            tint = Color.Gray,
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "사진 추가",
                            fontSize = 12.sp,
                            letterSpacing = (-0.8).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 상품명
            InputField(
                label = "상품명",
                value = "",
                onValueChange = { },
                placeholder = ""
            )

            // 카테고리
            InputField(
                label = "카테고리",
                value = category,
                onValueChange = { category = it },
                placeholder = ""
            )

            // 상품 URL
            InputField(
                label = "상품 URL",
                value = shareUrl,
                onValueChange = { shareUrl = it },
                placeholder = ""
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 설명 섹션
            Text(
                text = "설명",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            BasicTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFF4F4F4), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                decorationBox = { innerTextField ->
                    if (description.isEmpty()) {
                        Row(
                            verticalAlignment = Alignment.Top,      // 아이콘과 첫 줄 맞춤
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.circle_small),
                                contentDescription = "설명 아이콘",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(20.dp)
                                    .offset(x = 2.dp, y = (2).dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "브랜드, 상품명, 구매 개수 등 상품 설명을 최대한\n자세히 적어주세요.",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        Text(
                            text = "0/1000",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 55.dp, bottom = 0.dp)
                        )
                    }
                    innerTextField()
                }
            )

            // 공동구매 마감일
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "공동구매 마감일",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedDate,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_month),
                        contentDescription = "날짜 선택",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                            .offset(y = (-2).dp)
                            .clickable { showCalendarModal = true } // 이 부분 추가
                    )
                }
            }

            // 구매 개수
            CounterRow(
                label = "구매 개수",
                count = guestCount,
                onCountChange = { guestCount = it },
                showInfo = false,
                infoText = ""
            )

            // 내가 구매할 개수
            CounterRow(
                label = "내가 구매할 개수",
                count = myGuestCount,
                onCountChange = { myGuestCount = it },
                showInfo = true,
                infoText = "추가 가능 물품 수: ${guestCount - myGuestCount}",
                maxCount = guestCount,
                onInfoClick = { showInfoModal = true }
            )

            // 가격
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "가격",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = "정보",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp).offset(x = (2).dp, y = (0.5).dp)
                            .clickable { showPriceInfoModal = true }
                    )
                }
            }

            // 가격
            var price by remember { mutableStateOf("") }

            InputField(
                label = "가격",
                value = price,
                onValueChange = { price = it },
                placeholder = "",
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.won),
                        contentDescription = "가격 아이콘",
                        tint = Color.Gray,
                        modifier = Modifier.size(15.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            // 배송비
            var shippingFee by remember { mutableStateOf("") }
            var isFreeShipping by remember { mutableStateOf(false) }

            // 배송비 입력 필드와 무료배송 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // 배송비 입력 필드
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    var isFocused by remember { mutableStateOf(false) }

                    BasicTextField(
                        value = if (isFreeShipping) "" else shippingFee,
                        onValueChange = {
                            if (!isFreeShipping) {
                                shippingFee = it
                            }
                        },
                        enabled = !isFreeShipping,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = if (isFreeShipping) Color.Gray else Color.Black
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .onFocusChanged { focusState ->
                                isFocused = focusState.isFocused
                            },
                        decorationBox = { innerTextField ->
                            Box(modifier = Modifier.fillMaxWidth()) {
                                if (shippingFee.isEmpty() && !isFocused) {
                                    Text(
                                        text = if (isFreeShipping) "무료배송" else "배송비",
                                        color = if (isFreeShipping) Color(0xFF4DB6AC) else Color.Gray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )

                    // 밑줄
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                if (isFocused) Color(0xFF2FB475)
                                else if (isFreeShipping) Color(0xFF4DB6AC)
                                else Color(0xFFE0E0E0)
                            )
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 무료배송 버튼
                Button(
                    onClick = {
                        isFreeShipping = !isFreeShipping
                        if (isFreeShipping) {
                            shippingFee = ""
                        }
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFreeShipping) Color(0xFFf5f5f5) else Color(0xFF2FB475)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = "무료 배송",
                        color = if (isFreeShipping) Color.Gray else Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 등록 버튼
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2FB475)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "등록하기",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        Column {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        leadingIcon?.let { icon ->
                            icon()
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty() && !isFocused) {
                                Text(
                                    text = label,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE0E0E0))
            )
        }
    }
}

@Composable
fun CounterRow(
    label: String,
    count: Int,
    onCountChange: (Int) -> Unit,
    showInfo: Boolean = false,
    infoText: String = "",
    maxCount: Int = Int.MAX_VALUE,
    onInfoClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                if (showInfo) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                            .offset(x = (6).dp, y = (0.5).dp)
                            .clickable { onInfoClick?.invoke() }
                    )
                }
            }

            val canDecrease = count > 0
            val canIncrease = count < maxCount

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(enabled = canDecrease) { onCountChange(count - 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "−",
                        fontSize = 18.sp,
                        color = if (count > 0) Color.Black else Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = count.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .width(24.dp)
                        .padding(horizontal = 4.dp),
                    textAlign = TextAlign.Center
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(enabled = canIncrease) { if (canIncrease) onCountChange(count + 1) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 18.sp,
                        color = if (canIncrease) Color.Black else Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        if (showInfo && infoText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = infoText,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterDetailScreenPreview() {
    MaterialTheme {
        RegisterDetailScreen()
    }
}