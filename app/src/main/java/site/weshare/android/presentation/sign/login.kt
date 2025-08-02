package site.weshare.android.presentation.sign.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.weshare.android.R


@Composable
fun LoginScreen(
    navToWebLogin: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffF0F0F0))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_title),
            contentDescription = "타이틀",
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(160.dp)
                .padding(vertical = 20.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.login_email_box),
            contentDescription = "이메일 입력",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.login_password_box),
            contentDescription = "비밀번호 입력",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.login_find_password),
            contentDescription = "비밀번호 찾기",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .height(50.dp)
                .padding(bottom = 20.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.login_button),
            contentDescription = "로그인 버튼",
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable { /* TODO: 로그인 기능 */ }
        )

        Image(
            painter = painterResource(id = R.drawable.ifnot_user),
            contentDescription = "회원이 아니시라면",
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable { }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(id = R.drawable.signup_button),
            contentDescription = "회원가입 버튼",
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clickable { /* TODO: 회원가입 기능 */ }
        )

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