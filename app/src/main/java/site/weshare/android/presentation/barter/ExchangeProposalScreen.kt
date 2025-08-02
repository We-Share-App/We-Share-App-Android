import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

// 가상의 아이템 데이터 모델
data class ExchangeItem(
    val id: String,
    val name: String,
    val imageUrl: String,
)

// 가상의 데이터 로딩 함수 (사용자가 등록한 아이템을 가져오는 것을 시뮬레이션)
suspend fun fetchUserItems(): List<ExchangeItem> {
    kotlinx.coroutines.delay(500) // 네트워크 지연 시뮬레이션
    return listOf(
        ExchangeItem("item1", "엘지트윈스 검니폼", "https://twinslockerdium.co.kr/web/product/big/202503/fb024ef9790f9097c5b706e8c96da271.jpg"),
        ExchangeItem("item2", "정품 LG트윈스 야구", "https://placehold.co/150x150/00FF00/000000?text=Item+2"),
        ExchangeItem("item3", "Jackson (잭슨) 일렉", "https://placehold.co/150x150/0000FF/FFFFFF?text=Item+3"),
        ExchangeItem("item4", "F87 pro 다크그레이", "https://placehold.co/150x150/FFFF00/000000?text=Item+4"),
        ExchangeItem("item5", "귀멸의 칼날 만화책", "https://placehold.co/150x150/00FFFF/000000?text=Item+5"),
        ExchangeItem("item6", "입생로랑 몽파리 오드", "https://placehold.co/150x150/FF00FF/FFFFFF?text=Item+6"),
        ExchangeItem("item7", "새상품 스타벅스 SS", "https://placehold.co/150x150/800000/FFFFFF?text=Item+7"),
        ExchangeItem("item8", "나이키 운동화", "https://placehold.co/150x150/008000/FFFFFF?text=Item+8")
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeProposalScreen(modifier: Modifier = Modifier) {
    var userItems by remember { mutableStateOf<List<ExchangeItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedItemIds by remember { mutableStateOf(setOf<String>()) } // 선택된 아이템 ID 집합

    // 데이터 로딩
    LaunchedEffect(Unit) {
        isLoading = true
        userItems = fetchUserItems()
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("물품교환 제안", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { /* 뒤로 가기 액션 */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* 메뉴 액션 */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Button(
                    onClick = { /* 선택 완료 액션 */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp),
                    enabled = selectedItemIds.isNotEmpty() // 1개 이상 선택 시 활성화
                ) {
                    Text("선택을 완료했어요.", color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 안내 메시지
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "상대방에게 요청할 교환 희망 상품을 선택해주세요 ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "(최소 1개)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "상대방의 희망 카테고리는 스포츠, 의류에요",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // 아이템 그리드
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (userItems.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3열 그리드
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(userItems) { item ->
                        val isSelected = selectedItemIds.contains(item.id)
                        ExchangeItemCard(
                            item = item,
                            isSelected = isSelected,
                            onClick = {
                                selectedItemIds = if (isSelected) {
                                    selectedItemIds - item.id
                                } else {
                                    selectedItemIds + item.id
                                }
                            }
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("등록된 아이템이 없습니다.")
                }
            }
        }
    }
}

@Composable
fun ExchangeItemCard(
    item: ExchangeItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(IntrinsicSize.Min), // 높이를 내용에 맞게 조절
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null // 선택 시 테두리
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // 1:1 비율 유지
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray) // 이미지 로딩 전 배경
            ) {
                Image(
                    painter = rememberAsyncImagePainter(item.imageUrl),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                if (isSelected) {
                    // 선택 시 체크 표시 오버레이
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)), // 어두운 오버레이
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = "Selected",
                            tint = MaterialTheme.colorScheme.primary, // 체크 아이콘 색상
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeProposalScreenPreview() {
    MaterialTheme {
        ExchangeProposalScreen()
    }
}
