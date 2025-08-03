package site.weshare.android.presentation.barter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarterCandidateRegister() {
    var description by remember { mutableStateOf("") }

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

            Spacer(modifier = Modifier.height(8.dp))

            //물품 카테고리
            Title("물품 카테고리")
            var selectedItemCategory by remember { mutableStateOf<String?>(null) } // 단일 선택을 위해 String? 타입 사용
            val itemCategories = listOf(
                "가전", "의류", "도서", "식품", "생활용품", "스포츠/레저",
                "유아용품", "반려동물용품", "기타"
            )
            CategorySelectionGroup(
                options = itemCategories,
                selectedOptions = listOfNotNull(selectedItemCategory), // 단일 선택된 아이템만 리스트로 전달
                onOptionSelected = { category ->
                    // 이미 선택된 상태라면 null로 만들어 선택 해제, 아니면 새 상태로 변경
                    selectedItemCategory = if (selectedItemCategory == category) null else category
                }
            )



            // 상품 상태
            Title("상품 상태")
            // 상품 상태
            var selectedProductState by remember { mutableStateOf<String?>(null) }
            val productStates = listOf("새상품", "사용감 적음" , "사용감 많음")
            CategorySelectionGroup(
                options = productStates,
                selectedOptions = listOfNotNull(selectedProductState),
                onOptionSelected = { state ->
                    selectedProductState = if (selectedProductState == state) null else state
                }
            )

            // 희망 공개 시간
            Title("희망 공개 시간")
            DesiredRevealTime()

            // 설명 섹션
            Title("설명")

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

            //희망 공개 시간
            Title("희망 공개 시간")


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


// 선택 가능한 태그 컴포넌트
@Composable
fun SelectableTag(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 선택 상태에 따라 버튼의 색상을 결정합니다.
    val backgroundColor = if (isSelected) Color(0xFF2FB475) else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color.DarkGray
    val borderColor = if (isSelected) Color.White else Color.LightGray

    Button(
        onClick = onClick,
        modifier = modifier,
        // 버튼의 모양을 둥근 모서리로 설정합니다.
        shape = RoundedCornerShape(20.dp),
        // 버튼의 색상을 설정합니다.
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        // 버튼의 테두리를 설정합니다.
        border = BorderStroke(1.dp, borderColor),
        // 버튼 내부 패딩을 설정합니다.
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = text)
    }
}



// 카테고리 선택 그룹 컴포넌트
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelectionGroup(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        // FlowRow를 사용하여 태그들이 자동으로 다음 줄로 넘어가도록 합니다.
        FlowRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = selectedOptions.contains(option)
                SelectableTag(
                    text = option,
                    isSelected = isSelected,
                    onClick = { onOptionSelected(option) }
                )
            }
        }
    }
}

// 희망 공개 시간 컴포넌트
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesiredRevealTime(modifier: Modifier = Modifier) {
    var revealTime by remember { mutableStateOf(0) } // 공개 시간 상태 (시간 단위)

    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 마이너스 버튼
            Button(
                onClick = { if (revealTime > 0) revealTime-- },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "-", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
            }

            // 시간 표시 및 입력 필드
            OutlinedTextField(
                value = revealTime.toString(),
                onValueChange = { newValue ->
                    // 숫자만 입력 가능하도록 필터링하고, Int로 변환
                    revealTime = newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(56.dp), // Height to match buttons
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                shape = RoundedCornerShape(8.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray,
                )
            )

            // 플러스 버튼
            Button(
                onClick = { revealTime++ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "+", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
            }
        }

        // 추가 시간 버튼들
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val timeIncrements = listOf(1, 6, 12, 24)
            timeIncrements.forEach { increment ->
                Button(
                    onClick = { revealTime += increment },
                    modifier = Modifier.weight(1f), // 각 버튼이 균등하게 공간을 차지하도록
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2FB475),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = "+${increment}시간")
                }
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
fun Title(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )

}

@Preview(showBackground = true)
@Composable
fun RegisterDetailScreenPreview() {
    MaterialTheme {
        BarterCandidateRegister()
    }
}