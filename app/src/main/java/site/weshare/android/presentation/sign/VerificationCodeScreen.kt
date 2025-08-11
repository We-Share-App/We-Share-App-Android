package site.weshare.android.presentation.sign

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import site.weshare.android.data.remote.api.model.EmailVerifyRequest
import site.weshare.android.data.remote.api.ApiClient

@Composable
fun VerificationCodeScreen(
    email: String,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 28.dp, end = 28.dp, top = 200.dp),
        ) {
        Text("STEP 2", style = MaterialTheme.typography.titleMedium, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("전송된 인증번호를 입력해주세요.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))


        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("인증번호") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null

                    val trimmedEmail = email.trim()
                    val trimmedCode = code.trim()

                    val verifyRequest = EmailVerifyRequest(
                        email = trimmedEmail,
                        key = trimmedCode // ✅ 서버가 요구하는 필드명에 맞춤
                    )

                    Log.d("VerifyEmail", "✅ 요청 준비됨")
                    Log.d("VerifyEmail", "보낼 email: $trimmedEmail")
                    Log.d("VerifyEmail", "보낼 code: $trimmedCode")

                    val response = try {
                        val res = ApiClient.userApi.verifyEmailCode(verifyRequest)
                        Log.d("VerifyEmail", "✅ 응답 수신 완료: ${res.code()}")
                        val errorBody = res.errorBody()?.string()
                        if (!res.isSuccessful && errorBody != null) {
                            Log.e("VerifyEmail", "❌ 에러 본문: $errorBody")
                        }
                        res
                    } catch (e: Exception) {
                        Log.e("VerifyEmail", "❌ 네트워크 오류 발생", e)
                        errorMessage = "서버에 연결할 수 없습니다."
                        null
                    }

                    isLoading = false

                    if (response?.isSuccessful == true) {
                        Log.d("VerifyEmail", "🎉 인증 성공!")
                        onNext()
                    } else {
                        errorMessage = "인증 실패: ${response?.code()}"
                        Log.e("VerifyEmail", "❌ 인증 실패: ${response?.code()}")
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
            enabled = code.isNotBlank() && !isLoading
        ) {
            Text(
                text = if (isLoading) "확인 중…" else "확    인",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


//package site.weshare.android.presentation.sign
//
//import android.util.Log
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.core.view.DragAndDropPermissionsCompat.request
//import kotlinx.coroutines.launch
//import site.weshare.android.data.remote.api.ApiClient
//import site.weshare.android.data.remote.api.model.EmailVerifyRequest
//import site.weshare.android.util.getAccessToken
//
//@Composable
//fun VerificationCodeScreen(
//    email: String,
//    onNext: () -> Unit,
//    onBack: () -> Unit
//) {
//    val context = LocalContext.current
//    var code by remember { mutableStateOf("") }
//    var isLoading by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    val scope = rememberCoroutineScope()
//
//    Column(
//        Modifier.fillMaxSize().padding(24.dp),
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text("STEP 3", style = MaterialTheme.typography.titleMedium)
//        Text("전송된 인증번호를 입력하세요.")
//
//        OutlinedTextField(
//            value = code,
//            onValueChange = { code = it },
//            label = { Text("인증번호") },
//            modifier = Modifier.fillMaxWidth(),
//            isError = errorMessage != null
//        )
//
//        errorMessage?.let {
//            Spacer(Modifier.height(8.dp))
//            Text(it, color = MaterialTheme.colorScheme.error)
//        }
//
//        Spacer(Modifier.height(16.dp))
//        Button(
//            onClick = {
//                scope.launch {
//                    isLoading = true
//                    errorMessage = null
//
//                    val verifyRequest = EmailVerifyRequest(
//                        email = email.trim(),
//                        code = code.trim()
//                    )
//
//
//                    Log.d("VerifyEmail", "요청 전송: $verifyRequest")
//
//                    val response = try {
//                        ApiClient.userApi.verifyEmailCode(
//                            verifyRequest
//                        )
//                    } catch (e: Exception) {
//                        errorMessage = "서버에 연결할 수 없습니다."
//                        null
//                    }
//
//                    isLoading = false
//
//                    if (response?.isSuccessful == true) {
//                        onNext()
//                    } else {
//                        errorMessage = "인증 실패: ${response?.code()}"
//                        print(response)
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth(),
//            enabled = code.isNotBlank() && !isLoading
//        ) {
//            Text(if (isLoading) "확인 중..." else "확인")
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
private fun VerificationCodeScreenPreview() {
    VerificationCodeScreen(
        email = "test@example.com",
        onNext = {},
        onBack = {}
    )
}