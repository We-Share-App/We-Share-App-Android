package site.weshare.android.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R
import site.weshare.android.ui.theme.KachiAndroidTheme

@Composable
fun ProfileHeader(
    userName: String,
    profileImagePainter: Painter,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProfileClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = profileImagePainter,
            contentDescription = "프로필 사진",
            modifier = Modifier
                .size(69.dp)
                .clip(CircleShape) // 원형으로 클립
                .padding(12.dp) // 아이콘 안쪽 여백
        )
        Text(
            text = userName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "프로필 상세",
            modifier = Modifier.size(30.dp),
            tint = Color(0xFF757575) // 회색 계열
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    KachiAndroidTheme {
        ProfileHeader(
            userName = "홍길동님",
            profileImagePainter = painterResource(id = R.drawable.ic_profile_placeholder),
            onProfileClick = {}
        )
    }
}