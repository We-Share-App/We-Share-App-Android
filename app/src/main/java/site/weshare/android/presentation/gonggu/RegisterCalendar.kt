package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterCalendar() {
    var selectedDate by remember { mutableStateOf("2025-07-10") }
    var showCalendarModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

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
fun CalendarModalPreview() {
    MaterialTheme {
        CalendarModal(
            selectedDate = "2025-07-15",
            onDateSelected = { },
            onDismiss = { }
        )
    }
}