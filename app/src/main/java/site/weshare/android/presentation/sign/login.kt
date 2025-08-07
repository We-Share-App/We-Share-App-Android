package site.weshare.android.presentation.sign.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.R


@Composable
fun LoginScreen(
    navToWebLogin: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffF0F0F0))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Start)
                .wrapContentHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "가치있는 소비, 같이하는 소비",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.End)
                .wrapContentHeight()
                .padding(bottom = 18.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "현명한 소비생활을 즐기고 싶다면?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.End
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(4.dp))
                .padding(top = 6.dp, bottom = 12.dp, start = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                decorationBox = { innerTextField ->
                    Column {
                        Text("이메일", color = Color(0xffA9A9A9), fontSize = 9.5.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("sogong@gmail.com", color = Color(0xffA9A9A9), fontSize = 13.sp)
                    }
                    innerTextField()
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 기존 코드를 이것으로 교체
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(4.dp))
                .padding(top = 6.dp, bottom = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = email,
                        onValueChange = { email = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        decorationBox = { innerTextField ->
                            Column {
                                Text("이메일", color = Color(0xffA9A9A9), fontSize = 9.5.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("sogong@gmail.com", color = Color(0xffA9A9A9), fontSize = 13.sp)
                            }
                            innerTextField()
                        }
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.eye_off_outline), // 또는 원하는 아이콘
                    contentDescription = "비밀번호 보기",
                    tint = Color(0xffA9A9A9),
                    modifier = Modifier
                        .offset(y = (4).dp)
                        .size(20.dp)
                        .clickable { /* 아이콘 클릭 액션 */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .wrapContentHeight()
                .padding(bottom = 18.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "비밀번호 찾기",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xff0064FF),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF2FB475), RoundedCornerShape(4.dp))
                .clickable { /* TODO: 로그인 기능 */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "로그인",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            // 상단 회색 직선
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Color.Black)
            )

            // 텍스트 부분
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "회원이 아니시라면?",
                    fontSize = 16.sp,
                    color = Color(0xff666666),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF2FB475), RoundedCornerShape(4.dp))
                .clickable { /* TODO: 회원가입 기능 */ },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "회원가입",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_google),
                contentDescription = "Google 로그인",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { /* TODO */ }
            )

            Image(
                painter = painterResource(id = R.drawable.icon_naver),
                contentDescription = "Naver 로그인",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        navToWebLogin()
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.icon_kakao),
                contentDescription = "Kakao 로그인",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { /* TODO */ }
            )

            Image(
                painter = painterResource(id = R.drawable.icon_facebook),
                contentDescription = "Facebook 로그인",
                modifier = Modifier
                    .size(48.dp)
                    .clickable { /* TODO */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        navToWebLogin = {},
        onLoginSuccess = {}
    )
}