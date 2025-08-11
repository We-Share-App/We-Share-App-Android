package site.weshare.android.presentation.sign

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import site.weshare.android.util.getAccessToken
import java.net.URLEncoder
import site.weshare.android.data.remote.api.ApiClient

@Composable
fun NicknameInputScreen(
    onNicknameSet: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var nickname by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var isAvailable by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffF0F0F0))
            .padding(start = 28.dp, end = 28.dp, top = 200.dp),
        ) {
        Text("STEP 3", style = MaterialTheme.typography.titleMedium, fontSize = 22.sp, fontWeight = FontWeight.Bold )
        Spacer(modifier = Modifier.height(8.dp))
        Text("닉네임을 입력해주세요.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text("상품 등록 및 구매, 교환을 위해 필요합니다.", fontSize = 13.sp, fontWeight = FontWeight.Light, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = {
                nickname = it
                isChecked = false
                isAvailable = false
                errorMessage = null
                successMessage = null
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null,
            placeholder = { Text("사용 가능한 닉네임을 입력하세요.") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ 중복확인 버튼
        Button(
            onClick = {
                scope.launch {
                    val token = getAccessToken(context) ?: ""
                    val encodedNickname = URLEncoder.encode(nickname.trim(), "UTF-8")

                    Log.d("NICKNAME_CHECK", "👀 닉네임 원본: $nickname")
                    Log.d("NICKNAME_CHECK", "👀 인코딩된 닉네임: $encodedNickname")
                    Log.d("TOKEN_LOG", "📌 Access Token: $token")

                    try {
                        val response = ApiClient.userApi.checkNicknameAvailability(
                            nickname = encodedNickname,
                            accessToken = token
                        )
                        Log.d("API_CALL", "📥 응답 코드: ${response.code()}")

                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.d("API_CALL", "✅ 서버 응답 available: ${result?.available}")

                            isChecked = true
                            isAvailable = result?.available == true
                            errorMessage = if (isAvailable) null else "이미 사용 중인 닉네임입니다."
                            successMessage = if (isAvailable) "사용 가능한 닉네임입니다." else null
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("API_CALL", "❌ 서버 오류 응답: $errorBody")
                            isChecked = false
                            errorMessage = "서버 오류 발생: ${response.code()}"
                        }
                    } catch (e: Exception) {
                        isChecked = false
                        errorMessage = "네트워크 오류"
                        Log.e("NicknameCheck", "❌ 중복확인 실패", e)
                    }
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = nickname.isNotBlank()
        ) {
            Text("중복확인")
        }

        Spacer(modifier = Modifier.height(12.dp))

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        successMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ 닉네임 등록 버튼
        Button(
            onClick = {
                scope.launch {
                    isSubmitting = true
                    val token = getAccessToken(context) ?: ""
                    val encodedNickname = URLEncoder.encode(nickname.trim(), "UTF-8")

                    Log.d("TOKEN_LOG", "📌 Access Token (등록): $token")
                    Log.d("API_CALL", "📤 닉네임 등록 요청 - nickname: $encodedNickname")

                    try {
                        val response = ApiClient.userApi.updateNickname(
                            nickname = encodedNickname,
                            accessToken = token
                        )
                        Log.d("API_CALL", "📥 응답 코드: ${response.code()}")

                        if (response.isSuccessful) {
                            Log.d("NicknameSubmit", "🎉 닉네임 저장 성공")
                            onNicknameSet()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("NicknameSubmit", "❌ 닉네임 저장 실패: $errorBody")
                            errorMessage = "닉네임 저장 실패: ${response.code()}"
                        }
                    } catch (e: Exception) {
                        errorMessage = "서버 연결 실패"
                        Log.e("NicknameSubmit", "❌ 닉네임 저장 실패", e)
                    } finally {
                        isSubmitting = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2FB475),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF787878),
                disabledContentColor = Color.LightGray
            ),
            shape = RoundedCornerShape(5.dp),
            enabled = isChecked && isAvailable && !isSubmitting
        ) {
            Text(
                text = if (isSubmitting) "확인 중…" else "확    인",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NicknameInputScreenPreview() {
    NicknameInputScreen(
        onNicknameSet = {}
    )
}