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
                        .height(68.dp)
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

// ==================== 참여자 화면과 다이얼로그 연결 함수 ====================
// ==================== 참여자 화면과 다이얼로그, 채팅 연결 함수 ====================
@Composable
fun GongguWithDialogDemo(
    onBackClick: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    var showChat by remember { mutableStateOf(false) }

    if (showChat) {
        ChatScreen(
            onBackClick = {
                showChat = false
                showDialog = false  // 채팅에서 뒤로가기 시 참여자 화면으로
            },
            onMenuClick = {
                println("Menu clicked")
            },
            onSendMessage = { message ->
                println("Message sent: $message")
            }
        )
    } else if (showDialog) {
        DialogScreen(
            onConfirmClick = {
                showChat = true  // 🔥 네 버튼 클릭 시 채팅 화면으로 전환
            },
            onCancelClick = {
                showDialog = false
            }
        )
    } else {
        GroupPurchaseScreen(
            participants = sampleParticipants(),
            onBackClick = onBackClick,
            onChatClick = { participantId ->
                println("Chat with participant: $participantId")
            },
            onCompleteAllClick = {
                showDialog = true  // 🔥 버튼 클릭 시 다이얼로그 화면으로 전환
            }
        )
    }
}

// ==================== 데모용 화면 ====================
@Composable
fun ClosedGongguScreenDemo() {
    // 🔥 상태 변수 추가
    var showParticipants by remember { mutableStateOf(false) }

    if (showParticipants) {
        // 🔥 참여자 화면 표시 (다이얼로그 연결 포함)
        GongguWithDialogDemo(
            onBackClick = {
                showParticipants = false  // 뒤로가기 시 마감 화면으로 돌아감
            }
        )
    } else {
        // 🔥 마감 화면 표시
        ClosedGongguScreen(
            onCreateChatRoom = {
                showParticipants = true  // 버튼 클릭 시 참여자 화면으로 전환
            }
        )
    }
}

@Preview(showBackground = true, name = "Closed Gonggu Screen Preview")
@Composable
fun ClosedGongguScreenPreview() {
    MaterialTheme {
        ClosedGongguScreenDemo()
    }
}