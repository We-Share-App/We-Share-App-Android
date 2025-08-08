package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String? = null,
    val userId: String,
    val userName: String? = null
)

data class UserInfo(
    val userId: String,
    val userName: String,
    val userStatus: String? = null
)

data class ChatRoom(
    val id: String,
    val name: String,
    val participantCount: Int
)

// 사용자별 색상을 생성하는 함수
fun getUserColor(userId: String): Color {
    val colors = listOf(
        Color(0xFF44BC83), // 연한 빨강
        Color(0xFF59C391), // 연한 초록
        Color(0xFF59C391), // 연한 파랑
        Color(0xFF82D2AC)
    )

    val index = userId.hashCode().let {
        if (it < 0) -it else it
    } % colors.size

    return colors[index]
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatRoom: ChatRoom? = null,
    messages: List<ChatMessage> = emptyList(),
    userInfoMap: Map<String, UserInfo> = emptyMap(), // 사용자 정보 맵 추가
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onSendMessage: (String) -> Unit = {}
) {
    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = chatRoom?.name ?: "제주삼다수 2L 공동구매",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로 가기"
                    )
                }
            },
            actions = {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "메뉴"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // 날짜 표시
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "2025.07.09",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .background(
                        Color.White,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }

        // 공지 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(
                    Color(0xFF9E9E9E),
                    RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                Text(
                    text = "채팅 전, 이용 수칙을 꼭 읽어주세요!",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "서로서로 바르고 고운말을 사용하여 소통해주세요.",
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
        }

        // 메시지 리스트
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { message ->
                MessageItem(
                    message = message,
                    userInfo = userInfoMap[message.userId]
                )

                // 타임스탬프가 있는 경우 날짜 구분선 표시
                message.timestamp?.let { timestamp ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = timestamp,
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .background(
                                    Color.White,
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // 메시지 입력 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("메시지를 입력하세요") },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (messageText.isNotBlank()) {
                        onSendMessage(messageText)
                        messageText = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFF4CAF50),
                        RoundedCornerShape(24.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "전송",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun MessageItem(
    message: ChatMessage,
    userInfo: UserInfo? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            // 다른 사용자 메시지 (왼쪽 정렬) - 프로필 아이콘과 함께
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // 프로필 아이콘
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD9D9D9)),
                    contentAlignment = Alignment.Center
                ) {
                    val displayName = userInfo?.userName ?: message.userName ?: "?"
                    Text(
                        text = displayName.take(1),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.widthIn(max = 240.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                getUserColor(message.userId),
                                RoundedCornerShape(
                                    topStart = 4.dp,
                                    topEnd = 18.dp,
                                    bottomStart = 18.dp,
                                    bottomEnd = 18.dp
                                )
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = message.text,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            // 내 메시지 (오른쪽 정렬) - 프로필 정보 표시하지 않음
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .background(
                        Color(0xFF2FB475),
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 4.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val sampleMessages = listOf(
        ChatMessage(
            id = "1",
            text = "안녕하세요.\n제주삼다수 공동구매 총대입니다~",
            isFromUser = true,
            userId = "currentUser"
        ),
        ChatMessage(
            id = "2",
            text = "안녕하세요",
            isFromUser = false,
            userId = "user1",
            userName = "추"
        ),
        ChatMessage(
            id = "3",
            text = "안녕하세요!!",
            isFromUser = false,
            userId = "user2",
            userName = "길"
        ),
        ChatMessage(
            id = "4",
            text = "안녕하세요~",
            isFromUser = false,
            userId = "user3",
            userName = "홍"
        ),
        ChatMessage(
            id = "5",
            text = "안녕하세요~\n잘부탁드립니다",
            isFromUser = false,
            userId = "user4",
            userName = "서"
        )
    )

    val sampleUserInfoMap = mapOf(
        "user1" to UserInfo("user1", "추", "안녕하세요"),
        "user2" to UserInfo("user2", "길", "안녕하세요!!"),
        "user3" to UserInfo("user3", "홍", "안녕하세요😊"),
        "user4" to UserInfo("user4", "서", "안녕하세요~\n잘부탁드립니다")
    )

    val sampleChatRoom = ChatRoom(
        id = "room1",
        name = "제주삼다수 2L 공동구매",
        participantCount = 5
    )

    ChatScreen(
        chatRoom = sampleChatRoom,
        messages = sampleMessages,
        userInfoMap = sampleUserInfoMap
    )
}