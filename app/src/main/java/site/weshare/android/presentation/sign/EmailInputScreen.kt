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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
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
        Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("STEP 2", style = MaterialTheme.typography.titleMedium)
        Text("이메일을 입력해주세요.")

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
            modifier = Modifier.fillMaxWidth(),
            enabled = email.isNotBlank() && !isLoading
        ) {
            Text(if (isLoading) "전송 중..." else "확인")
        }
    }
}