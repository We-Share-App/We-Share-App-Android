package site.weshare.android.presentation.Barter2

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
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
fun ProductRegistrationScreen() {
    var productName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var selectedCondition by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf(0) }
    var quantityCount by remember { mutableStateOf("0") }

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
            modifier = Modifier.padding(bottom = 15.dp)
        )

        // 사진 등록
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 9.dp),
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
            value = productName,
            onValueChange = { productName = it },
            placeholder = ""
        )
        Spacer(modifier = Modifier.height(15.dp))
        // 교환 희망 카테고리
        Text(
            text = "교환 희망 카테고리",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(9.dp))

        // 카테고리 태그들
        val categories = listOf(
            listOf("의류", "신발", "디지털기기", "뷰티/미용"),
            listOf("가구", "생활가전", "게임", "도서/티켓/음반"),
            listOf("피규어/인형", "스포츠")
        )

        categories.forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowCategories.forEach { category ->
                    CategoryChip(
                        text = category,
                        isSelected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        modifier = Modifier.weight(1f)
                    )
                }
                // 빈 공간 채우기
                repeat(4 - rowCategories.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(22.dp))

        // 상품상태
        Text(
            text = "상품상태",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val conditions = listOf("새상품", "사용감 없음", "사용감 적음")
            conditions.forEach { condition ->
                CategoryChip(
                    text = condition,
                    isSelected = selectedCondition == condition,
                    onClick = { selectedCondition = condition },
                    modifier = Modifier.weight(1f).height(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

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
                        verticalAlignment = Alignment.Top,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.circle_small),
                            contentDescription = "설명 아이콘",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(20.dp)
                                .offset(x = 2.dp, y = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "브랜드, 상품명, 상품상태 등 상품 설명을 최대한\n자세히 적어주세요.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = "${description.length}/1000",
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
        Spacer(modifier = Modifier.height(30.dp))


        Text(
            text = "물품교환 등록하기가 아닌 물품교환 참여하기입니다. ",
            color = Color(0xff27A06A),
            fontSize = 16.sp
        )


        Spacer(modifier = Modifier.height(30.dp))


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

@Composable
fun InputFieldSecond(
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
                    .onFocusChanged { focusState: FocusState ->
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
fun CategoryChipSecond(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(30.dp)
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
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 12.sp
        )
    }
}

@Composable
fun TimeChipSecond(text: String) {
    Surface(
        modifier = Modifier.padding(2.dp),
        color = Color(0xFFE8F5E8),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun CounterRowSecond(
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
                            .offset(x = 6.dp, y = 0.5.dp)
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
fun RegistrationScreenPreview() {
    MaterialTheme {
        RegistrationScreen()
    }
}