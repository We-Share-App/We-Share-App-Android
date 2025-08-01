package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterInfo() {
    var guestCount by remember { mutableStateOf(0) }
    var myGuestCount by remember { mutableStateOf(0) }
    var showInfoModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
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
                .size(width = 290.dp, height = 180.dp)
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
                    text = "예) 총 구매 개수 100, 내가 가져가고 싶은 개수 10 \n \t\t\t→ 10개를 선택해주세요",
                    fontSize = 11.sp,
                    letterSpacing = (-0.8).sp,
                    lineHeight = 13.sp,
                    color = Color.DarkGray
                )
            }
        }
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