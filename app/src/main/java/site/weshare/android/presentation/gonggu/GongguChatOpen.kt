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

// DialogScreen에서는 Participant 데이터 클래스를 사용하지 않으므로 제거

@Composable
fun DialogScreen(
    onConfirmClick: () -> Unit = {}, // 🔥 "네" 버튼 클릭 시 콜백
    onCancelClick: () -> Unit = {}   // 🔥 "아니오" 버튼 클릭 시 콜백
) {
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
            onCallClick = onConfirmClick, // 🔥 "네" 버튼 → 네비게이션 처리
            onCancelClick = onCancelClick // 🔥 "아니오" 버튼 → 네비게이션 처리
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
                    onClick = onCallClick, // 🔥 이 버튼을 누르면 네비게이션 처리
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
                    onClick = onCancelClick, // 🔥 이 버튼을 누르면 뒤로가기 처리
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

// ==================== 사용 예시 ====================
/*
NavGraph에서 이렇게 사용하세요:

composable("dialog") {
    DialogScreen(
        onConfirmClick = {
            // 🔥 "네" 버튼 클릭 시 → ChatScreen으로 이동
            navController.navigate("chat")
        },
        onCancelClick = {
            // 🔥 "아니오" 버튼 클릭 시 → 이전 화면으로
            navController.popBackStack()
        }
    )
}

composable("chat") {
    ChatScreen(
        onBackClick = {
            navController.popBackStack()
        },
        onMenuClick = {
            // 메뉴 클릭 처리
            println("Menu clicked")
        },
        onSendMessage = { message ->
            // 메시지 전송 처리
            println("Message sent: $message")
        }
    )
}
*/

// ==================== 다이얼로그와 채팅 연결 함수 ====================
@Composable
fun DialogWithChatDemo(
    onBackClick: () -> Unit = {}
) {
    var showChat by remember { mutableStateOf(false) }

    DialogScreen(
        onConfirmClick = {
            showChat = true  // 🔥 네 버튼 클릭 시 채팅 화면으로 전환
        },
        onCancelClick = onBackClick
    )

    if (showChat) {
        ChatScreen(
            onBackClick = {
                showChat = false  // 🔥 채팅에서 뒤로가기 시 다이얼로그로 돌아감
            },
            onMenuClick = {
                println("Menu clicked")
            },
            onSendMessage = { message ->
                println("Message sent: $message")
            }
        )
    }
}

// 프리뷰
@Preview(showBackground = true)
@Composable
fun DialogScreenPreview() {
    DialogWithChatDemo()
}