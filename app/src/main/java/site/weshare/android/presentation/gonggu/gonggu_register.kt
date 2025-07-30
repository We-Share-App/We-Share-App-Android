package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import site.weshare.android.R
import java.text.SimpleDateFormat
import java.util.*

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

        if (showCalendarModal) {
            CalendarModal(
                selectedDate = selectedDate,
                onDateSelected = { date ->
                    selectedDate = date
                    showCalendarModal = false
                },
                onDismiss = { showCalendarModal = false }
            )
        }

        if (showPriceInfoModal) {
            PriceInfoModal(onDismiss = { showPriceInfoModal = false })
        }

        // 정보 모달
        if (showInfoModal) {
            InfoModal(
                onDismiss = { showInfoModal = false },
                guestCount = guestCount,
                myGuestCount = myGuestCount
            )
        }
    }
}

@Composable
fun CalendarModal(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // 현재 날짜 파싱
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = try {
        dateFormat.parse(selectedDate) ?: Date()
    } catch (e: Exception) {
        Date()
    }

    val calendar = Calendar.getInstance().apply { time = currentDate }
    var currentYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var currentMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var tempSelectedDate by remember { mutableStateOf(selectedDate) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        // 통합 카드 (상하단만 둥근 모서리)
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .padding(24.dp)
                .offset(x = (5).dp, y = 200.dp)
                .clickable(enabled = false) { }
        ) {
            Column {
                // 달력 영역 (상단 둥근 모서리)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFD6F0C7),
                            RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // 헤더 - 년월 네비게이션
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.KeyboardArrowLeft,
                                contentDescription = "이전 달",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        if (currentMonth == 0) {
                                            currentMonth = 11
                                            currentYear--
                                        } else {
                                            currentMonth--
                                        }
                                    }
                            )

                            Text(
                                text = "${currentYear}년",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 13.dp)
                            )

                            Text(
                                text = "${currentMonth + 1}월",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(end = 13.dp)
                            )

                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                contentDescription = "다음 달",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable {
                                        if (currentMonth == 11) {
                                            currentMonth = 0
                                            currentYear++
                                        } else {
                                            currentMonth++
                                        }
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 요일 헤더
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
                                Text(
                                    text = day,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.width(40.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 달력 그리드 생성
                        val calendarDays = generateCalendarDays(currentYear, currentMonth)

                        calendarDays.chunked(7).forEach { week ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                week.forEach { dayInfo ->
                                    val (day, isCurrentMonth) = dayInfo
                                    val dateString = String.format("%04d-%02d-%02d", currentYear, currentMonth + 1, day)
                                    val isSelected = isCurrentMonth && tempSelectedDate == dateString

                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clickable(enabled = isCurrentMonth) {
                                                if (isCurrentMonth) {
                                                    tempSelectedDate = dateString
                                                }
                                            }
                                            .background(
                                                if (isSelected) Color(0xFF2FB475) else Color.Transparent,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (day > 0) day.toString() else "",
                                            fontSize = 14.sp,
                                            color = when {
                                                isSelected -> Color.White
                                                isCurrentMonth -> Color.Black
                                                else -> Color.Gray
                                            },
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }

                // 구분선 (선택사항)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE0E0E0))
                )

                // 버튼 영역 (하단 둥근 모서리)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF73CDA2)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "취  소",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Button(
                            onClick = {
                                onDateSelected(tempSelectedDate)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2FB475)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "선  택",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
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

@Composable
fun InfoModal(
    onDismiss: () -> Unit,
    guestCount: Int,
    myGuestCount: Int
) {
    // 반투명 배경 + 클릭시 닫기
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { onDismiss() }
    ) {
        // 가운데 박스
        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .size(width = 290.dp, height = 165.dp)
                .padding(24.dp)
                .offset(x = 39.dp, y = 150.dp)
                .wrapContentWidth()
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

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "공동 구매에서 내가 가져갈 개수를 선택해주세요",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.8).sp,
                    fontSize = 15.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                // 예시 문구
                Text(
                    text = "예) 총 구매 개수 $guestCount, 내가 가져가고 싶은 개수 $myGuestCount \n \t\t\t→ ${myGuestCount}개를 선택해주세요",
                    fontSize = 11.sp,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 13.sp,
                    color = Color.DarkGray
                )
            }
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

fun generateCalendarDays(year: Int, month: Int): List<Pair<Int, Boolean>> {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)

    // 해당 월의 첫 날이 무슨 요일인지 (일요일=1, 월요일=2, ...)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1

    // 해당 월의 마지막 날
    val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // 이전 달의 마지막 날
    calendar.add(Calendar.MONTH, -1)
    val lastDayOfPrevMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    val days = mutableListOf<Pair<Int, Boolean>>()

    // 이전 달의 날짜들 (회색으로 표시)
    for (i in firstDayOfWeek - 1 downTo 0) {
        days.add(Pair(lastDayOfPrevMonth - i, false))
    }

    // 현재 달의 날짜들
    for (day in 1..lastDayOfMonth) {
        days.add(Pair(day, true))
    }

    // 다음 달의 날짜들 (6주 * 7일 = 42개를 맞추기 위해)
    val remainingDays = 42 - days.size
    for (day in 1..remainingDays) {
        days.add(Pair(day, false))
    }

    return days
}


@Preview(showBackground = true)
@Composable
fun RegisterDetailScreenPreview() {
    MaterialTheme {
        RegisterDetailScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun InfoModalOnlyPreview() {
    InfoModal(
        onDismiss    = {},
        guestCount   = 100,
        myGuestCount = 10
    )
}

@Preview(showBackground = true)
@Composable
fun PriceInfoModalPreview() {
    MaterialTheme {
        PriceInfoModal(onDismiss = { })
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarModalPreview() {
    MaterialTheme {
        CalendarModal(
            selectedDate = "2025-07-15",
            onDateSelected = { },
            onDismiss = { }
        )
    }
}