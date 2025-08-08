package site.weshare.android.presentation.sign

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

@Composable
fun PhonenumberScreen(
    email: String,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var code by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 28.dp, end = 28.dp, top = 200.dp),
    ) {
        Text("STEP 2", style = MaterialTheme.typography.titleMedium, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("휴대폰 번호를 입력해주세요.", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text("본인인증을 위해 필요합니다.", fontSize = 13.sp, fontWeight = FontWeight.Light, color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("010-xxxx-xxxx") },
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
                // TODO: 휴대폰 번호 검증 로직 추가
                if (code.isNotBlank()) {
                    onNext()
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

@Preview(showBackground = true)
@Composable
private fun VerificationCodeScreenPreview() {
    PhonenumberScreen(
        email = "test@example.com",
        onNext = {},
        onBack = {}
    )
}