package com.example.registeroverlay

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

        // 공동구매 등록하기 버튼
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 35.dp, bottom = 160.dp) // FAB 위에 위치
        ) {
            Button(
                onClick = onRegisterClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2FB475),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .width(160.dp)
                    .height(47.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    "공동구매 등록하기",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
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
                onRegisterClick = {},
                onDismiss = {}
            )
        }
    }
}