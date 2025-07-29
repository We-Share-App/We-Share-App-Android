package site.weshare.android.presentation.barter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp // 좋아요 아이콘 (하트 아이콘은 다른 라이브러리 필요)
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import coil.compose.rememberAsyncImagePainter


@Composable
fun BarterScreen(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopArea()
            CategoryList()
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class) // TopAppBar 관련 API에 필요할 수 있음
@Composable
fun TopArea(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface) // 배경색 설정 (카드의 배경색과 통일)
            .padding(bottom = 8.dp) // 아래쪽에 여백 추가
    ) {
        // 1. 최상단: "흑석동" 및 아이콘들
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 좌측: "흑석동" 텍스트와 드롭다운 아이콘
            Row(
                modifier = Modifier.clickable { /* 위치 선택 드롭다운 토글 */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon(Icons.Default.LocationOn, contentDescription = "Location") // 위치 아이콘 필요 시
                Text(
                    text = "흑석동",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp, // 화면 이미지에 맞게 조절
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Location")
                // TODO: 실제 드롭다운 메뉴 구현 (아래 FilterChip 예시 참고)
            }

            // 우측: 검색, 하트, 메뉴 아이콘
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp) // 아이콘 간 간격
            ) {
                IconButton(onClick = { /* 검색 클릭 */ }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { /* 관심 목록 클릭 */ }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorites")
                }
                IconButton(onClick = { /* 메뉴 클릭 */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            }
        }

        // 2. 필터/카테고리 칩 목록
        // LazyRow를 사용하여 스크롤 가능한 칩 목록 구현
        // 이미지에서는 칩들이 가로로 스크롤 되는 것처럼 보이므로 LazyRow가 적합
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // 칩 간 간격
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 필터/카테고리 데이터 (예시)
            val filters = listOf("가격", "갯수", "카테고리", "상품상태")
            val sortOption = "최신순" // 정렬 옵션은 별도로 처리

            // 일반 필터 칩들
            items(filters) { filterName ->
                FilterDropdownChip(label = filterName) {
                    // TODO: 필터 선택 시 동작 정의
                    // 예를 들어, 해당 필터에 대한 드롭다운 메뉴를 띄우는 로직
                }
            }

            // 최신순 정렬 칩 (우측 정렬을 위해 Spacer와 함께 사용하거나,
            // LazyRow 자체의 contentPadding과 Item의 Modifier.weight()를 이용해 조절 가능)
            // 간단하게는 뒤쪽에 배치
            item {
                Spacer(modifier = Modifier.weight(1f)) // 남은 공간을 채워서 다음 칩을 오른쪽으로 밀어냄
                FilterDropdownChip(label = sortOption) {
                    // TODO: 정렬 옵션 선택 시 동작 정의
                }
            }
        }
    }
}

// 필터 드롭다운 칩을 위한 재사용 가능한 컴포저블
@Composable
fun FilterDropdownChip(label: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("옵션 1", "옵션 2", "옵션 3") // 실제 옵션 데이터는 달라질 것임
    var selectedText by remember { mutableStateOf(label) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart) // DropdownMenu가 칩 위에 뜨도록
            .clip(RoundedCornerShape(8.dp)) // 둥근 모서리
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)) // 테두리
            .clickable { expanded = true } // 클릭 시 드롭다운 열림
            .padding(horizontal = 12.dp, vertical = 8.dp) // 내부 패딩
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = selectedText, style = MaterialTheme.typography.bodyMedium)
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
        }

        // 드롭다운 메뉴
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = true) // 포커스 가능하게 설정
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedText = option // 선택된 옵션으로 텍스트 변경
                        onOptionSelected(option) // 콜백 호출
                        expanded = false // 드롭다운 닫기
                    }
                )
            }
        }
    }
}



@Composable
fun CategoryList(){
    val barterItems = listOf(

        // test 용
        BarterItem(
            id = "1",
            imageUrl = "https://i.namu.wiki/i/tGwMeBIapNlKiywyhqmnUhjYED6bMNrrIPyFRo5HbbEKtbcumcaKg0x0mOs9vHpPK8AI6exF-RpIhyGN1at-hG_f3uig5_nR-xotdotuZ3JqXzR-k3I9vFpXYOJVMqu6grhz16nHK7w-k3MJbAZQAQ.webp",
            name = "롯데자이언츠 동백 유니폼 어센틱",
            condition = "사용감 없음",
            desiredCategory = "스포츠, 의류",
            uploadTime = "1주전",
            likesCount = 13
        ),
        BarterItem(
            id = "2",
            imageUrl = "https://via.placeholder.com/150/33FF57/FFFFFF?text=Vitamin+Bottles",
            name = "프리미에 디칼파잉 방 클라스타즈 상품 3개",
            condition = "사용감 없음",
            desiredCategory = "뷰티/미용, 도서/티켓/음반",
            uploadTime = "1주전",
            likesCount = 8
        ),
        BarterItem(
            id = "3",
            imageUrl = "https://via.placeholder.com/150/3357FF/FFFFFF?text=Gaming+Laptop",
            name = "레노버 게이밍 노트북 Legion 5 15arh6",
            condition = "사용감 없음",
            desiredCategory = "디지털기기, 게임",
            uploadTime = "2주전",
            likesCount = 11
        ),
        BarterItem(
            id = "4",
            imageUrl = "https://via.placeholder.com/150/FFFF33/000000?text=Watch",
            name = "태그호이어 링크 청판 CBC2112",
            condition = "사용감 없음",
            desiredCategory = "의류, 디지털기기",
            uploadTime = "2주전",
            likesCount = 1
        ),
        BarterItem(
            id = "5",
            imageUrl = "https://via.placeholder.com/150/FF33FF/FFFFFF?text=Polo+Shirt",
            name = "(신상) 플로 슬림핏 린넨 셔츠", // 이미지에 "(신상)" 태그가 있으므로 데이터에 추가
            condition = "새 상품",
            desiredCategory = "의류",
            uploadTime = "1주전",
            likesCount = 0,
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize() // LazyColumn이 남은 공간을 채우도록 함
    ) {
        items(barterItems) { item ->
            BarterItemCard(item = item)
        }
    }
}

@Composable
fun BarterItemCard(item: BarterItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 상품 이미지
            Image(
                painter = rememberAsyncImagePainter(item.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 상품명 및 더보기 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                    /* 더보기 메뉴 처리 */
                    }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 상품 상태
                Text(
                    text = "상품 상태: ${item.condition}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                // 희망 카테고리
                Text(
                    text = "희망 카테고리: ${item.desiredCategory}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 업로드 시간
                    Text(
                        text = item.uploadTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.DarkGray
                    )

                    // 좋아요/관심 숫자
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.FavoriteBorder, // 실제 하트 아이콘으로 교체 필요
                            contentDescription = "Likes",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Red // 예시 색상
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.likesCount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }


    }


}

data class BarterItem(
    val id: String,        // 게시물 id
    val imageUrl: String, // 이미지 URL (실제 이미지는 Coil/Glide 등으로 로드)
    val name: String,      //게시물 제목(아이템명)
    val condition: String, // 예: "사용감 없음", "새 상품"
    val desiredCategory: String, // 예: "스포츠, 의류"
    val uploadTime: String, // 예: "1주전"
    val likesCount: Int,
)


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

        BarterScreen()

}