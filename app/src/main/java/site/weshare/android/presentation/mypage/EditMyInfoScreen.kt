package site.weshare.android.presentation.mypage

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import site.weshare.android.ui.theme.KachiAndroidTheme

@Composable
fun EditMyInfoScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "내 정보 수정",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("뒤로가기")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "화면 전환이 성공했습니다!",
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditMyInfoScreenPreview() {
    KachiAndroidTheme {
        EditMyInfoScreen(rememberNavController())
    }
}