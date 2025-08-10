package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==================== 마감된 공동구매 화면 ====================
@Composable
fun ClosedGongguScreen(
    onCreateChatRoom: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 메인 콘텐츠 영역 (어두운 필터 적용)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                // 마감 메시지
                Text(
                    text = "공동구매가\n마감되었어요",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    lineHeight = 45.sp,
                    style = TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color.Black.copy(alpha = 0.7f),
                            offset = androidx.compose.ui.geometry.Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    )
                )
            }

            // 하단 버튼 영역
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Button(
                    onClick = onCreateChatRoom, // 🔥 이 콜백으로 네비게이션 처리
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2FB475)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "공동구매 채팅방 만들기",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ==================== 사용 예시 ====================
/*
기존 화면에서 이렇게 사용하세요:

ClosedGongguScreen(
    onCreateChatRoom = {
        // 🔥 참여자 화면으로 네비게이션
        navController.navigate("participants")
    }
)

그리고 NavGraph에 이 라우트를 추가하세요:

composable("participants") {
    GroupPurchaseScreen(
        participants = sampleParticipants(),
        onBackClick = {
            navController.popBackStack()
        },
        onChatClick = { participantId ->
            println("Chat with participant: $participantId")
        },
        onCompleteAllClick = {
            println("Group purchase completed")
        }
    )
}
*/

// ==================== 데모용 화면 ====================
@Composable
fun ClosedGongguScreenDemo() {
    // 바로 마감 화면이 보이도록 수정
    ClosedGongguScreen(
        onCreateChatRoom = {
            // 🔥 실제 사용 시에는 navController.navigate("participants")로 교체
            println("채팅방 만들기 버튼 클릭됨 - participants 화면으로 이동")
        }
    )
}

@Preview(showBackground = true, name = "Closed Gonggu Screen Preview")
@Composable
fun ClosedGongguScreenPreview() {
    MaterialTheme {
        ClosedGongguScreenDemo()
    }
}