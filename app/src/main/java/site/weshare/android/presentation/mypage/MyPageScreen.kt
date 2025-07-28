package site.weshare.android.presentation.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import site.weshare.android.R
import site.weshare.android.presentation.components.MenuItem
import site.weshare.android.presentation.components.ProfileHeader
import site.weshare.android.presentation.components.SectionHeader
import site.weshare.android.ui.theme.KachiAndroidTheme

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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)), // 밝은 회색 배경
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // 그림자 없앰
        ) {
            Column {
                MenuItem(
                    title = "공동구매 총대 내역",
                    description = "내가 진행했던 공동구매를 확인할 수 있어요",
                    onClick = { navController.navigate("communityPurchaseInviteHistory") }
                )
                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
                MenuItem(
                    title = "공동구매 참여 내역",
                    description = "내가 참여했던 공동구매를 확인할 수 있어요",
                    onClick = { navController.navigate("communityPurchaseParticipationHistory") }
                )
                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                MenuItem(
                    title = "공동구매 게시글 확인하기",
                    onClick = { navController.navigate("communityPurchasePostConfirm") }
                )
                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
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
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column {
                MenuItem(
                    title = "내 동네 설정하기",
                    onClick = { navController.navigate("setMyNeighborhood") }
                )
                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
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