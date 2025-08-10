package site.weshare.android.presentation.Barter2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import site.weshare.android.R

data class TradeItem(
    val id: Int,
    val name: String,
    val category: String,
    val condition: String,
    val imageRes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeSelectionScreen(
    onBackClick: () -> Unit = {},
    onSelectionComplete: () -> Unit = {}
) {
    val selectedItems = remember { mutableStateListOf<Int>() }
    val dummyItems = getDummyItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* 메뉴 */ }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "메뉴",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Column {
                Divider(color = Color.LightGray, thickness = 0.5.dp)

                Button(
                    onClick = {
                        if (selectedItems.isNotEmpty()) {
                            onSelectionComplete()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedItems.isNotEmpty()) Color(0xFF2FB475) else Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = selectedItems.isNotEmpty()
                ) {
                    Text(
                        "선택을 완료했어요.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 제목과 설명 영역
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "상대방에게 요청할 교환 희망\n상품을 선택해주세요",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 30.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        "상대방의 희망 카테고리는 ",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "스포츠, 의류",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        "에요 😊",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 아이템 그리드
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(dummyItems) { item ->
                    TradeItemCard(
                        item = item,
                        isSelected = selectedItems.contains(item.id),
                        onSelectionChanged = { isSelected ->
                            if (isSelected) {
                                selectedItems.add(item.id)
                            } else {
                                selectedItems.remove(item.id)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TradeItemCard(
    item: TradeItem,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelectionChanged(!isSelected) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 이미지
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                item.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                "[${item.category}]",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Text(
                item.condition,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}

private fun getDummyItems() = listOf(
    TradeItem(1, "엘지트윈스 검니폼", "의류 카테고리", "스포츠, 의류", R.drawable.dpfwl),
    TradeItem(2, "정품) LG트윈스 유니폼", "의류 카테고리", "스포츠, 의류", R.drawable.dpfwl2),
    TradeItem(3, "Jackson (잭슨) 일렉기", "악기 카테고리", "디지털기기", R.drawable.rlxk),
    TradeItem(4, "F87 pro 다크그레이 키", "디지털 카테고리", "디지털기기", R.drawable.zlqhem),
    TradeItem(5, "귀멸의 칼날 만화책", "의류 카테고리", "도서/취미/완구", R.drawable.rnlzkf)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TradeSelectionScreenPreview() {
    MaterialTheme {
        TradeSelectionScreen()
    }
}