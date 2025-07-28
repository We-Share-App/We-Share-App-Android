//package site.weshare.android.presentation.components
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import site.weshare.android.R
//import site.weshare.android.ui.theme.KachiAndroidTheme
//
//@Composable
//fun MenuItem(
//    title: String,
//    description: String? = null, // 설명이 없는 경우도 처리
//    onClick: () -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onClick)
//            .padding(vertical = 16.dp, horizontal = 16.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = title,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Normal,
//                color = Color.Black
//            )
//            description?.let {
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = it,
//                    fontSize = 12.sp,
//                    color = Color.Gray
//                )
//            }
//        }
//        Icon(
//            painter = painterResource(id = R.drawable.ic_arrow_right), // 실제 화살표 아이콘 파일명
//            contentDescription = null, // 접근성 고려: 텍스트로 충분하면 null, 아니면 적절한 설명
//            modifier = Modifier.size(24.dp),
//            tint = Color(0xFF757575) // 회색 계열
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MenuItemPreview() {
//    KachiAndroidTheme {
//        Column {
//            MenuItem(
//                title = "공동구매 초대 내역",
//                description = "내가 진행했던 공동구매를 확인할 수 있어요",
//                onClick = {}
//            )
//            MenuItem(
//                title = "물품교환 참여 내역",
//                onClick = {}
//            )
//        }
//    }
//}
package site.weshare.android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape // RoundedCornerShape import
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface // Surface import
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R
import site.weshare.android.ui.theme.KachiAndroidTheme

@Composable
fun MenuItem(
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 4.dp), // Surface 자체의 외부 패딩 (그룹화 고려)
        shape = RoundedCornerShape(12.dp), // 둥근 모서리 적용 (스크린샷에 맞춰 조절)
        color = Color.White, // 박스 배경색 (하얀색)
        shadowElevation = 2.dp // 살짝 그림자 추가 (선택 사항, 스크린샷에는 그림자가 없어 보일 수도 있음)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 20.dp), // Row 내부의 내용 패딩
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                description?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "자세히 보기",
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF424242)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuItemPreview() {
    KachiAndroidTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // 전체 화면에 대한 여백
            verticalArrangement = Arrangement.spacedBy(8.dp) // MenuItem 사이 간격
        ) {
            // 상단 그룹 (스크린샷처럼 구분된 그룹을 만들기 위해 Column으로 묶음)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp), // 전체 그룹의 둥근 모서리 (선택 사항)
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column {
                    MenuItem(
                        title = "공동구매 총대 내역",
                        description = "내가 진행했던 공동구매를 확인할 수 있어요",
                        onClick = {}
                    )
                    // 구분선 추가 (옵션)
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE0E0E0))) // 연한 회색 구분선
                    MenuItem(
                        title = "공동구매 참여 내역",
                        description = "내가 참여했던 공동구매를 확인할 수 있어요",
                        onClick = {}
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp)) // 상단 그룹과 하단 항목 사이의 간격

            // 하단 개별 항목
            MenuItem(
                title = "물품교환 참여 내역",
                onClick = {}
            )
        }
    }
}