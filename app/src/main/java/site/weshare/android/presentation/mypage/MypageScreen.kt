package site.weshare.android.presentation.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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

@Composable
fun MenuItem(
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            description?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right), // 오른쪽 화살표 아이콘
            contentDescription = "이동",
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF757575)
        )
    }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 8.dp, start = 10.dp)
    )
}

@Composable
fun MyPageScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 전체 배경색
            .padding(16.dp)
    ) {


        // 1. 프로필 섹션
        ProfileHeader(
            userName = "홍길동님",
            profileImagePainter = painterResource(id = R.drawable.ic_profile_placeholder), // 실제 프로필 아이콘 파일명
            onProfileClick = {
                // 프로필 클릭 시 동작 (예: 프로필 편집 화면으로 이동)
                navController.navigate("profileEditScreen") // 예시 라우트
            }
        )

        Spacer(modifier = Modifier.height(24.dp))


        // 2. 거래 내역 섹션
        SectionHeader(title = "거래 내역")
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().border(0.7.dp, Color.Gray, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White), // 흰색 배경
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // 그림자 없앰
        ) {
            Column {
                MenuItem(
                    title = "공동구매 총대 내역",
                    description = "내가 진행했던 공동구매를 확인할 수 있어요",
                    onClick = { navController.navigate("communityPurchaseInviteHistory") }
                )
                Divider(color = Color.Gray, thickness = 0.7.dp)
                MenuItem(
                    title = "공동구매 참여 내역",
                    description = "내가 참여했던 공동구매를 확인할 수 있어요",
                    onClick = { navController.navigate("communityPurchaseParticipationHistory") }
                )}}
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier.fillMaxWidth().border(0.7.dp, Color.Gray, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White), // 흰색 배경
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // 그림자 없앰
        ){
            Column{
                MenuItem(
                    title = "물품교환 참여 내역",
                    onClick = { navController.navigate("itemExchangeParticipationHistory") }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. 내 게시글 섹션
        SectionHeader(title = "내 게시글")
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().border(0.7.dp, Color.Gray, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                MenuItem(
                    title = "공동구매 게시글 확인하기",
                    onClick = { navController.navigate("communityPurchasePostConfirm") }
                )
                Divider(color = Color.Gray, thickness = 0.7.dp, modifier = Modifier.padding(horizontal = 0.dp))
                MenuItem(
                    title = "물품교환 게시글 확인하기",
                    onClick = { navController.navigate("itemExchangePostConfirm") }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 4. 기타 섹션
        SectionHeader(title = "기타")
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth().border(0.7.dp, Color.Gray, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                MenuItem(
                    title = "내 동네 설정하기",
                    onClick = { navController.navigate("setMyNeighborhood") }
                )
                Divider(color = Color.Gray, thickness = 0.7.dp, modifier = Modifier.padding(horizontal = 0.dp))
                MenuItem(
                    title = "동네 인증하기",
                    onClick = { navController.navigate("neighborhoodCertification") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageScreenPreview() {
    KachiAndroidTheme {
        MyPageScreen(navController = rememberNavController())
    }
}