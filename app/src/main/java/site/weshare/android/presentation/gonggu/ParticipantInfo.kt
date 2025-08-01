package com.example.grouppurchase

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 데이터 클래스
data class Participant(
    val id: String,
    val name: String,
    val purchaseCount: Int, // 구매물품 수
    val participationDate: String, // 참여일
    val userType: UserType, // 사용자 타입
    val profileImageUrl: String? = null
)

enum class UserType {
    SAFE, // 안심 사용자
    REPORTED, // 신고당한 사용자
    NORMAL // 일반 사용자
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupPurchaseScreen(
    participants: List<Participant> = sampleParticipants(),
    onBackClick: () -> Unit = {},
    onChatClick: (String) -> Unit = {},
    onCompleteAllClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 상단 앱바
        TopAppBar(
            title = {
                Text(
                    text = "참여자",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        // 참여자 목록
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(participants) { participant ->
                ParticipantItem(
                    participant = participant,
                    onChatClick = { onChatClick(participant.id) }
                )
            }
        }

        // 공동구매 완료 버튼
        Button(
            onClick = onCompleteAllClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "공동구매 완료 버튼",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ParticipantItem(
    participant: Participant,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (participant.profileImageUrl != null) {
                    // 실제 이미지 로딩 시 Coil 등의 라이브러리 사용
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 참여자 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = participant.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "구매물품 수: ${participant.purchaseCount}개",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Text(
                    text = "구매참여일: ${participant.participationDate}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                // 사용자 타입 표시
                when (participant.userType) {
                    UserType.SAFE -> {
                        Text(
                            text = "안심 사용자입니다",
                            fontSize = 11.sp,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    UserType.REPORTED -> {
                        Text(
                            text = "신고당한 사용자입니다",
                            fontSize = 11.sp,
                            color = Color(0xFFFF5722)
                        )
                    }
                    UserType.NORMAL -> {
                        // 일반 사용자는 별도 표시 없음
                    }
                }
            }

            // 채팅하기 버튼
            Button(
                onClick = onChatClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .width(70.dp)
                    .height(32.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "채팅하기",
                    color = Color.White,
                    fontSize = 11.sp
                )
            }
        }
    }
}



// 샘플 데이터
fun sampleParticipants() = listOf(
    Participant(
        id = "1",
        name = "숙니",
        purchaseCount = 3,
        participationDate = "7/7",
        userType = UserType.SAFE
    ),
    Participant(
        id = "2",
        name = "김용",
        purchaseCount = 2,
        participationDate = "7/5",
        userType = UserType.SAFE
    ),
    Participant(
        id = "3",
        name = "웅모든",
        purchaseCount = 1,
        participationDate = "7/8",
        userType = UserType.REPORTED
    ),
    Participant(
        id = "4",
        name = "사용자1",
        purchaseCount = 4,
        participationDate = "7/6",
        userType = UserType.SAFE
    ),
    Participant(
        id = "5",
        name = "사용자2",
        purchaseCount = 2,
        participationDate = "7/7",
        userType = UserType.SAFE
    )
)

@Preview(showBackground = true)
@Composable
fun GroupPurchaseScreenPreview() {
    GroupPurchaseScreen()
}