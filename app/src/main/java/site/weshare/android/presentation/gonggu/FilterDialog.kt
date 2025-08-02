package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.presentation.gonggu.FilterSettings

//// ==================== 필터 설정 데이터 모델 ====================
//data class FilterSettings(
//    val minPrice: String = "",
//    val maxPrice: String = "",
//    val minCount: String = "",
//    val maxCount: String = "",
//    val selectedCategory: String = ""
//)

// ==================== 필터 다이얼로그 ====================
@Composable
fun FilterDialog(
    filterSettings: FilterSettings,
    onFilterSettingsChange: (FilterSettings) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    val backgroundAlpha = 0.3f

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 배경
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha))
                .clickable { onDismiss() }
        )

        // 필터 다이얼로그
        Card(
            modifier = Modifier
                .width(325.dp)
                .padding(horizontal = 17.dp, vertical = 5.dp)
                .wrapContentHeight()
                .offset(x = (-20).dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
            ) {
                // 가격 텍스트
                Text(
                    text = "가격",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                // 상태
                var minPriceSlider by remember { mutableFloatStateOf(0f) }
                var maxPriceSlider by remember { mutableFloatStateOf(1000000f) }

                // 가격 포맷 함수
                fun formatPrice(price: Float): String = "%,d원".format(price.toInt())

                // 가격 박스
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = if (minPriceSlider == 0f) "0원" else formatPrice(minPriceSlider),
                        onValueChange = {},
                        placeholder = { Text("최소 가격", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(33.dp),
                        singleLine = true,
                        readOnly = true,
                        textStyle = TextStyle(fontSize = 13.sp, textAlign = TextAlign.End, color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2FB475),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Text("~", fontSize = 16.sp, color = Color.Gray)

                    OutlinedTextField(
                        value = if (maxPriceSlider == 1000000f) "1,000,000원" else formatPrice(maxPriceSlider),
                        onValueChange = {},
                        placeholder = { Text("최대 가격", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(33.dp),
                        singleLine = true,
                        readOnly = true,
                        textStyle = TextStyle(fontSize = 13.sp, textAlign = TextAlign.End, color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2FB475),
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                }

                // 슬라이드 바 + 슬라이더
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    val trackHeight = 4.dp

                    Canvas(modifier = Modifier.matchParentSize()) {
                        val trackY = size.height / 2
                        val trackWidth = size.width

                        val minPx = (minPriceSlider / 1000000f) * trackWidth
                        val maxPx = (maxPriceSlider / 1000000f) * trackWidth

                        // 전체 트랙 (회색)
                        drawRoundRect(
                            color = Color.LightGray,
                            topLeft = Offset(0f, trackY - trackHeight.toPx() / 2),
                            size = Size(trackWidth, trackHeight.toPx()),
                            cornerRadius = CornerRadius(trackHeight.toPx())
                        )

                        // 활성 구간 (초록색) - 최소값과 최대값 사이
                        if (maxPx > minPx) {
                            drawRoundRect(
                                color = Color(0xFF2FB475),
                                topLeft = Offset(minPx, trackY - trackHeight.toPx() / 2),
                                size = Size(maxPx - minPx, trackHeight.toPx()),
                                cornerRadius = CornerRadius(trackHeight.toPx())
                            )
                        }

                        // 썸 - 최소값 (초록색)
                        drawCircle(Color(0xFF2FB475), 10.dp.toPx(), Offset(minPx, trackY))
                        drawCircle(Color.White, 6.dp.toPx(), Offset(minPx, trackY))

                        // 썸 - 최대값 (초록색)
                        drawCircle(Color(0xFF2FB475), 10.dp.toPx(), Offset(maxPx, trackY))
                        drawCircle(Color.White, 6.dp.toPx(), Offset(maxPx, trackY))
                    }

                    // 최소값 슬라이더
                    Slider(
                        value = minPriceSlider,
                        onValueChange = { newValue ->
                            minPriceSlider = newValue.coerceAtMost(maxPriceSlider)
                            onFilterSettingsChange(filterSettings.copy(minPrice = minPriceSlider.toInt().toString()))
                        },
                        valueRange = 0f..1000000f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Transparent,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent
                        )
                    )

                    // 최대값 슬라이더
                    Slider(
                        value = maxPriceSlider,
                        onValueChange = { newValue ->
                            maxPriceSlider = newValue.coerceAtLeast(minPriceSlider)
                            onFilterSettingsChange(filterSettings.copy(maxPrice = maxPriceSlider.toInt().toString()))
                        },
                        valueRange = 0f..1000000f,
                        modifier = Modifier.fillMaxWidth(),
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Transparent,
                            activeTrackColor = Color.Transparent,
                            inactiveTrackColor = Color.Transparent
                        )
                    )
                }

                // 개수 필터
                Text(
                    text = "개수",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // 개수 입력 UI
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .height(33.dp)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // [ - 버튼 ]
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .fillMaxHeight()
                            .clickable {
                                val currentCount = filterSettings.minCount.toIntOrNull() ?: 0
                                if (currentCount > 0) {
                                    onFilterSettingsChange(filterSettings.copy(minCount = (currentCount - 1).toString()))
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "−", fontSize = 20.sp, color = Color.Black)
                    }

                    // [ 구분선 1 ]
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp)
                            .background(Color.LightGray)
                    )

                    // [ 숫자 중앙 표시 ]
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filterSettings.minCount.ifEmpty { "0" },
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }

                    // [ 구분선 2 ]
                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(1.dp)
                            .background(Color.LightGray)
                    )

                    // [ + 버튼 ]
                    Box(
                        modifier = Modifier
                            .width(48.dp)
                            .fillMaxHeight()
                            .clickable {
                                val currentCount = filterSettings.minCount.toIntOrNull() ?: 0
                                onFilterSettingsChange(filterSettings.copy(minCount = (currentCount + 1).toString()))
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+", fontSize = 20.sp, color = Color.Black)
                    }
                }

                // 개수 선택 버튼들
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val countOptions = listOf("+1개", "+5개", "+10개", "+100개")
                    countOptions.forEach { option ->
                        val incrementValue = option.replace("+", "").replace("개", "").toInt()
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xffE8E8E8))
                                .clickable {
                                    val currentCount = filterSettings.minCount.toIntOrNull() ?: 0
                                    val newCount = currentCount + incrementValue
                                    onFilterSettingsChange(filterSettings.copy(minCount = newCount.toString()))
                                }
                                .padding(horizontal = 12.7.dp, vertical = 3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF2FB475),
                                letterSpacing = -0.1.sp
                            )
                        }
                    }
                }

                // 카테고리 필터
                Text(
                    text = "카테고리",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                var selectedCategories by remember { mutableStateOf(setOf<String>()) }

                val categories = listOf(
                    listOf("의류", "신발", "가구"),
                    listOf("디지털기기", "생활가전", "게임"),
                    listOf("피규어/인형", "스포츠", "도서/티켓/음반"),
                    listOf("뷰티/미용")
                )

                val categoryLetterSpacings = mapOf(
                    "의류" to 0.sp, "신발" to 0.sp, "디지털기기" to (-0.9).sp,
                    "가구" to 0.sp, "생활가전" to (-0.3).sp, "게임" to 0.sp,
                    "피규어/인형" to (-1).sp, "스포츠" to 0.sp, "도서/티켓/음반" to (-1.2).sp,
                    "뷰티/미용" to (-0.9).sp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    categories.forEach { rowCategories ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
                        ) {
                            rowCategories.forEach { category ->
                                val isSelected = selectedCategories.contains(category)

                                Box(
                                    modifier = Modifier
                                        .width(75.dp)
                                        .height(27.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(
                                            if (isSelected)
                                                Color(0xFFD6F0C7)
                                            else
                                                Color.White
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected)
                                                Color(0xFF2FB475)
                                            else
                                                Color.LightGray,
                                            RoundedCornerShape(15.dp)
                                        )
                                        .clickable {
                                            selectedCategories = if (isSelected) {
                                                selectedCategories - category
                                            } else {
                                                selectedCategories + category
                                            }
                                            onFilterSettingsChange(
                                                filterSettings.copy(
                                                    selectedCategory = selectedCategories.joinToString(",")
                                                )
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = category,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        letterSpacing = categoryLetterSpacings[category] ?: 0.sp,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        color = Color.Black
                                    )
                                }
                            }
                            // 빈 공간 채우기 (3개 미만인 경우)
                            repeat(3 - rowCategories.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                // 버튼들을 나란히 배치
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 초기화 버튼
                    OutlinedButton(
                        onClick = {
                            onFilterSettingsChange(
                                FilterSettings(
                                    minPrice = "",
                                    maxPrice = "",
                                    minCount = "",
                                    maxCount = "",
                                    selectedCategory = ""
                                )
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(38.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black,
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color.LightGray),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("초기화", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }

                    // 결과보기 버튼
                    Button(
                        onClick = onApply,
                        modifier = Modifier
                            .weight(2f)
                            .height(38.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2FB475),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "512개 결과보기",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ==================== 데모용 화면 ====================
@Composable
fun FilterDialogDemo() {
    var filterSettings by remember {
        mutableStateOf(
            FilterSettings(
                minPrice = "10000",
                maxPrice = "50000",
                selectedCategory = "식료품"
            )
        )
    }

    FilterDialog(
        filterSettings = filterSettings,
        onFilterSettingsChange = { filterSettings = it },
        onApply = {
            println("필터 적용: $filterSettings")
        },
        onDismiss = {
            println("필터 다이얼로그 닫기")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun FilterDialogPreview() {
    MaterialTheme {
        FilterDialogDemo()
    }
}