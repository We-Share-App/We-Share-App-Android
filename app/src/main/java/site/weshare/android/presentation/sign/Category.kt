package site.weshare.android.presentation.sign

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import site.weshare.android.R

data class Category(
    val id: String,
    val name: String,
    val iconResId: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionScreen() {
    val categories = listOf(
        Category("clothes", "의류", R.drawable.clothed), // 실제 drawable 이름으로 변경
        Category("shoes", "신발", R.drawable.shoes),
        Category("digital", "디지털기기", R.drawable.digital),
        Category("furniture", "가구", R.drawable.furniture),
        Category("appliances", "생활가전", R.drawable.appliances),
        Category("games", "게임", R.drawable.game),
        Category("toys", "장난감/인형", R.drawable.doll),
        Category("sports", "스포츠", R.drawable.sports),
        Category("books", "도서/티켓/음반", R.drawable.book),
        Category("beauty", "뷰티/미용", R.drawable.beauty)
    )

    var selectedCategories by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { /* 뒤로가기 */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back), // drawable의 뒤로가기 아이콘으로 변경
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            // 제목과 설명
            Text(
                text = "어떤 카테고리에 관심\n있으세요?",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 33.sp,
                color = Color.Black,
            )
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = buildAnnotatedString {
                    // 앞 부분 스타일
                    withStyle(style = SpanStyle(color = Color(0xFF545454))) {
                        append("관심 카테고리 물품을 추천드릴게요.")
                    }
                    // 뒤 부분 스타일
                    withStyle(style = SpanStyle(color = Color(0xFF787878))) {
                        append(" (최소 2개 선택)")
                    }
                },
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 30.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 카테고리 그리드
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(categories) { category ->
                    CategoryItem(
                        category = category,
                        isSelected = selectedCategories.contains(category.id),
                        onSelectionChanged = { isSelected ->
                            selectedCategories = if (isSelected) {
                                selectedCategories + category.id
                            } else {
                                selectedCategories - category.id
                            }
                        }
                    )
                }
            }

            // 확인 버튼
            Button(
                onClick = { /* 확인 처리 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2FB475)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "확    인",
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                onSelectionChanged(!isSelected)
            }
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(70.dp)
        ) {
            Image(
                painter = painterResource(id = category.iconResId),
                contentDescription = category.name,
                modifier = Modifier.size(68.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category.name,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) Color.Black else Color.DarkGray,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategorySelectionScreenPreview() {
    MaterialTheme {
        CategorySelectionScreen()
    }
}