package site.weshare.android.presentation.mypage

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import site.weshare.android.R // R.drawable 리소스를 사용하기 위함
import site.weshare.android.ui.theme.LightGrayBackground
import site.weshare.android.ui.theme.DividerGray
import site.weshare.android.ui.theme.IconGray
import site.weshare.android.ui.theme.KachiAndroidTheme


@OptIn(ExperimentalMaterial3Api::class) @Composable
fun EditMyInfoScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = {
                            // TODO: 뒤로가기 동작 구현 (navController.popBackStack() 등)
                            Toast.makeText(context, "뒤로가기 클릭", Toast.LENGTH_SHORT).show()
                        }, modifier = Modifier.align(Alignment.CenterStart)) {
                            Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "뒤로가기") // 뒤로가기 아이콘 (ic_arrow_back 필요)
                        }
                        Text(
                            text = "내 정보 수정",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                navigationIcon = {
                    // 뒤로가기 버튼이 title Box 안으로 이동했으므로 비워둡니다.
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White // 전체 배경색
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 프로필 이미지 (피그마에서 가져온 단일 이미지 파일)
            Image(
                painter = painterResource(id = R.drawable.ic_profile_with_camera), // 이 파일명을 실제 파일명으로 변경하세요.
                contentDescription = "프로필 사진 변경",
                modifier = Modifier
                    .size(95.dp)
                    .clickable { Toast.makeText(context, "프로필 사진 변경", Toast.LENGTH_SHORT).show() }
            )


            Spacer(modifier = Modifier.height(32.dp))

            // 정보 항목 섹션
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DividerGray, RoundedCornerShape(12.dp)), // 테두리 추가
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column {
                    InfoItem(label = "닉네임", value = "홍길동", onClick = { Toast.makeText(context, "닉네임 수정", Toast.LENGTH_SHORT).show() })
                    Divider(color = DividerGray, thickness = 1.dp)
                    InfoItem(label = "이름", value = "홍길동", onClick = { Toast.makeText(context, "이름 수정", Toast.LENGTH_SHORT).show() })
                    Divider(color = DividerGray, thickness = 1.dp)
                    InfoItem(label = "대표 이메일", value = "honghong @gmail.com", showArrow = true, onClick = { Toast.makeText(context, "대표 이메일 수정", Toast.LENGTH_SHORT).show() })
                    Divider(color = DividerGray, thickness = 1.dp)
                    InfoItem(label = "휴대폰 번호 변경", onClick = { Toast.makeText(context, "휴대폰 번호 변경", Toast.LENGTH_SHORT).show() })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 로그인 기기 관리
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DividerGray, RoundedCornerShape(12.dp)), // 테두리 추가
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                InfoItem(
                    label = "로그인 기기 관리",
                    description = "내 아이디로 로그인 된 기기를 관리할 수 있어요",
                    onClick = { Toast.makeText(context, "로그인 기기 관리", Toast.LENGTH_SHORT).show() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 연동된 소셜 계정
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, DividerGray, RoundedCornerShape(12.dp)) // 테두리 추가
                    .clickable { Toast.makeText(context, "연동된 소셜 계정 클릭됨", Toast.LENGTH_SHORT).show() }, // 클릭 가능하게 변경
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "연동된 소셜 계정",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // 하단 버튼을 바닥에 붙이기 위한 Spacer

            // 하단 로그아웃/회원탈퇴 버튼
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "로그아웃",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable { Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show() }
                        .padding(8.dp) // 클릭 영역 확장
                )
                Text(
                    text = "|",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(
                    text = "회원탈퇴",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clickable { Toast.makeText(context, "회원탈퇴", Toast.LENGTH_SHORT).show() }
                        .padding(8.dp) // 클릭 영역 확장
                )
            }
        }
    }
}

/**
 * 정보 항목을 표시하는 재사용 가능한 Composable
 * @param label 항목의 제목
 * @param value 항목의 값 (예: 닉네임, 이메일)
 * @param description 항목에 대한 추가 설명
 * @param showArrow 오른쪽 화살표 아이콘 표시 여부 (기본값 true)
 * @param onClick 항목 클릭 시 동작
 */
 @Composable
fun InfoItem(
    label: String,
    value: String? = null,
    description: String? = null,
    showArrow: Boolean = true,
    onClick: (() -> Unit)? = null // onClick이 없는 경우를 위해 nullable로 변경
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                // onClick이 제공된 경우에만 clickable 모디파이어 적용
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
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

        value?.let {
            Text(
                text = it,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(0.dp)) // 값과 화살표 사이 간격
        }

        if (showArrow) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right), // 오른쪽 화살표 아이콘
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = IconGray
            )
        }
    }
}

 @Preview(showBackground = true)
 @Composable
fun EditMyInfoScreenPreview() {
    KachiAndroidTheme { // YourProjectNameTheme -> Theme
        EditMyInfoScreen(rememberNavController())
    }
}