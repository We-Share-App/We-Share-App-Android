package site.weshare.android.presentation.Barter2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterOverlay(
    onParticipateClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 어두운 배경
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onDismiss() }
        )

        // 하나의 박스 안에 두 개의 버튼 영역
        Card(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 35.dp, bottom = 160.dp)
                .width(160.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2FB475))
        ) {
            Column {
                // 물품교환 참여하기 버튼
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp)
                        .clickable {
                            // 전환 먼저, 닫힘 나중 (일부 구성에서 닫힘이 먼저면 nav가 먹지 않는 경우 방지)
                            onParticipateClick()
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "물품교환 참여하기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                // 구분선
                Divider(
                    color = Color.White.copy(alpha = 0.3f),
                    thickness = 1.dp
                )

                // 물품교환 등록하기 버튼
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp)
                        .clickable {
                            // 전환 먼저, 닫힘 나중
                            onRegisterClick()
                            onDismiss()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "물품교환 등록하기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // X 버튼 (FAB 스타일)
        FloatingActionButton(
            onClick = onDismiss,
            containerColor = Color(0xFF2FB475),
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 35.dp, bottom = 100.dp)
                .size(width = 45.dp, height = 45.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "닫기",
                modifier = Modifier.size(18.dp),
                tint = Color.White
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterOverlayPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            RegisterOverlay(
                onParticipateClick = {},
                onRegisterClick = {},
                onDismiss = {}
            )
        }
    }
}