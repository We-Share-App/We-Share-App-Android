package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
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

// 데이터 클래스
data class Participant(
    val name: String,
    val period: String,
    val location: String,
    val description: String,
    val isOnline: Boolean = false
)

@Composable
fun DialogScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)), // 어두운 오버레이
        contentAlignment = Alignment.Center
    ) {
        // 배경 하단 버튼 (오버레이 뒤에)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // 공동구매 채팅방 만들기 버튼
            Button(
                onClick = { },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xff606060),
                    disabledContainerColor = Color(0xff606060)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "공동구매 채팅방 만들기",
                    color = Color(0xffCCCCCC),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 중앙 다이얼로그
        CallDialog(
            onCallClick = { },
            onCancelClick = { }
        )
    }
}

@Composable
fun CallDialog(
    onCallClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 29.dp, bottom = 16.dp,),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 메시지 텍스트
            Text(
                text = "공동구매 채팅방을\n만드시겠습니까?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 네 버튼
                Button(
                    onClick = onCallClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2FB475)
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "네",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // 아니요 버튼
                Button(
                    onClick = onCancelClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF73CDA2)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "아니오",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// 프리뷰
@Preview(showBackground = true)
@Composable
fun DialogScreenPreview() {
    DialogScreen()
}