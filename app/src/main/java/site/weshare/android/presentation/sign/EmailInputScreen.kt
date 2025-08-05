package site.weshare.android.presentation.sign

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.weshare.android.data.remote.api.model.EmailRequest
import site.weshare.android.util.getAccessToken


@Composable
fun EmailInputScreen(
    onNext: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        Modifier.fillMaxSize().padding(start = 28.dp, end = 28.dp, top = 200.dp)
    ) {
        Text("STEP 4", style = MaterialTheme.typography.titleMedium, fontSize = 22.sp, fontWeight = FontWeight.Bold )
        Spacer(modifier = Modifier.height(8.dp))
        Text("이메일을 입력해주세요.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null

                    val response = try {
                        ApiClient.userApi.requestEmailCode(
                            EmailRequest(email.trim())  // ✅ JSON으로 보낼 수 있게 래핑
                        )
                    } catch (e: Exception) {
                        errorMessage = "서버에 연결할 수 없습니다."
                        null
                    }

                    isLoading = false

                    if (response?.isSuccessful == true) {
                        onNext(email)
                    } else {
                        errorMessage = "이메일 전송 실패: ${response?.code()}"
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
            enabled = email.isNotBlank() && !isLoading
        ) {
            Text(
                text = if (isLoading) "확인 중…" else "확    인",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailInputScreenPreview() {
    EmailInputScreen(
        onNext = {},
        onBack = {}
    )
}