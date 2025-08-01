package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import site.weshare.android.R

// ==================== 초과 수량 메시지 다이얼로그 ====================
@Composable
fun OverQuantityDialog(
    remainingQuantity: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 경고 아이콘 (vector 파일 사용)
                Icon(
                    painter = painterResource(id = R.drawable.vector),
                    contentDescription = "경고",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Unspecified // 원본 색상 유지
                )

                // 메시지 텍스트
                Text(
                    text = "재고 부족으로 ${remainingQuantity}개까지\n구매 가능한 상품입니다.\n개수를 다시 선택해주세요.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    lineHeight = 22.sp
                )

                // 확인 버튼
                Button(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text(
                        text = "확  인",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}

// ==================== 데모용 화면 ====================
@Composable
fun OverQuantityDialogDemo() {
    var showDialog by remember { mutableStateOf(true) } // 처음부터 다이얼로그 표시
    val remainingQuantity = 8

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .then(if (showDialog) Modifier.blur(3.dp) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        // 배경 화면 (블러 처리됨)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "남은 개수: ${remainingQuantity}개",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                onClick = { showDialog = true }
            ) {
                Text("초과 수량 메시지 보기")
            }
        }
    }

    // 초과 수량 메시지 다이얼로그
    if (showDialog) {
        OverQuantityDialog(
            remainingQuantity = remainingQuantity,
            onDismiss = { showDialog = false },
            onConfirm = {
                // 확인 버튼 클릭 시 처리할 로직
                showDialog = false
            }
        )
    }
}

@Preview(showBackground = true, name = "Over Quantity Dialog Preview")
@Composable
fun OverQuantityDialogPreview() {
    MaterialTheme {
        OverQuantityDialogDemo()
    }
}