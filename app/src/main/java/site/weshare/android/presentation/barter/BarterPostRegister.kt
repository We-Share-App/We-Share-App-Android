//package site.weshare.android.presentation.barter
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import site.weshare.android.R
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BarterPostRegister() {
//    var description by remember { mutableStateOf("") }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//                .padding(16.dp)
//                .verticalScroll(rememberScrollState())
//        ) {
//            // Header
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 4.dp),
//                horizontalArrangement = Arrangement.End,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    Icons.Default.Close,
//                    contentDescription = "닫기",
//                    tint = Color.Black,
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clickable { /* 닫기 로직 */ }
//                )
//            }
//
//            // 상품정보 섹션
//            Text(
//                text = "상품정보",
//                fontSize = 17.5.sp,
//                fontWeight = FontWeight.ExtraBold,
//                color = Color.Black,
//                modifier = Modifier.padding(bottom = 10.dp)
//            )
//
//            // 사진 등록
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 14.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(60.dp)
//                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
//                        .clickable { },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.camera),
//                            contentDescription = "사진 추가",
//                            tint = Color.Gray,
//                            modifier = Modifier.size(25.dp)
//                        )
//                        Text(
//                            text = "사진 추가",
//                            fontSize = 12.sp,
//                            letterSpacing = (-0.8).sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color.Gray
//                        )
//                    }
//                }
//            }
//
//            // 상품명
//            InputField(
//                label = "상품명",
//                value = "",
//                onValueChange = { },
//                placeholder = ""
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//            // 교환 희망 카테고리
//            Title("교환 희망 카테고리")
//            var selectedCategories by remember { mutableStateOf(listOf<String>()) }
//            val categories = listOf(
//                "의류", "신발", "디지털기기", "뷰티/미용", "가구",
//                "생활가전", "게임", "도서/티켓/음반", "피규어/인형", "스포츠"
//            )
//            CategorySelectionGroup(
//                options = categories,
//                selectedOptions = selectedCategories,
//                onOptionSelected = { category ->
//                    selectedCategories = if (selectedCategories.contains(category)) {
//                        selectedCategories - category
//                    } else {
//                        selectedCategories + category
//                    }
//                },
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            // 상품 상태
//            Title("상품 상태")
//            // 상품 상태
//            var selectedProductState by remember { mutableStateOf<String?>(null) }
//            val productStates = listOf("새상품", "사용감 적음" , "사용감 많음")
//            CategorySelectionGroup(
//                options = productStates,
//                selectedOptions = listOfNotNull(selectedProductState),
//                onOptionSelected = { state ->
//                    selectedProductState = if (selectedProductState == state) null else state
//                }
//            )
//
//            // 설명 섹션
//            Title("설명")
//
//            BasicTextField(
//                value = description,
//                onValueChange = { description = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp)
//                    .background(Color(0xFFF4F4F4), RoundedCornerShape(8.dp))
//                    .padding(12.dp),
//                textStyle = TextStyle(
//                    fontSize = 14.sp,
//                    color = Color.Black
//                ),
//                decorationBox = { innerTextField ->
//                    if (description.isEmpty()) {
//                        Row(
//                            verticalAlignment = Alignment.Top,      // 아이콘과 첫 줄 맞춤
//                        ) {
//                            Icon(
//                                painter = painterResource(id = R.drawable.circle_small),
//                                contentDescription = "설명 아이콘",
//                                tint = Color.Gray,
//                                modifier = Modifier
//                                    .size(20.dp)
//                                    .offset(x = 2.dp, y = (2).dp)
//                            )
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Text(
//                                text = "브랜드, 상품명, 구매 개수 등 상품 설명을 최대한\n자세히 적어주세요.",
//                                color = Color.Gray,
//                                fontSize = 14.sp
//                            )
//                        }
//                        Text(
//                            text = "0/1000",
//                            fontSize = 10.sp,
//                            color = Color.Gray,
//                            textAlign = TextAlign.End,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(top = 55.dp, bottom = 0.dp)
//                        )
//                    }
//                    innerTextField()
//                }
//            )
//
//            // 희망 공개 시간
//            Title("희망 공개 시간")
//            DesiredRevealTime()
//
//            // 등록 버튼
//            Button(
//                onClick = { },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2FB475)
//                ),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Text(
//                    text = "등록하기",
//                    color = Color.White,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//        }
//
//    }
//}
//
//
//// 선택 가능한 태그 컴포넌트
//@Composable
//fun SelectableTag(
//    text: String,
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    // 선택 상태에 따라 버튼의 색상을 결정합니다.
//    val backgroundColor = if (isSelected) Color(0xFF2FB475) else Color.Transparent
//    val contentColor = if (isSelected) Color.White else Color.DarkGray
//    val borderColor = if (isSelected) Color.White else Color.LightGray
//
//    Button(
//        onClick = onClick,
//        modifier = modifier,
//        // 버튼의 모양을 둥근 모서리로 설정합니다.
//        shape = RoundedCornerShape(20.dp),
//        // 버튼의 색상을 설정합니다.
//        colors = ButtonDefaults.buttonColors(
//            containerColor = backgroundColor,
//            contentColor = contentColor
//        ),
//        // 버튼의 테두리를 설정합니다.
//        border = BorderStroke(1.dp, borderColor),
//        // 버튼 내부 패딩을 설정합니다.
//        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
//    ) {
//        Text(text = text)
//    }
//}
//
//
//
//// 카테고리 선택 그룹 컴포넌트
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun CategorySelectionGroup(
//    options: List<String>,
//    selectedOptions: List<String>,
//    onOptionSelected: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
//        // FlowRow를 사용하여 태그들이 자동으로 다음 줄로 넘어가도록 합니다.
//        FlowRow(
//            modifier = Modifier.fillMaxSize(),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            options.forEach { option ->
//                val isSelected = selectedOptions.contains(option)
//                SelectableTag(
//                    text = option,
//                    isSelected = isSelected,
//                    onClick = { onOptionSelected(option) }
//                )
//            }
//        }
//    }
//}
//
//// 희망 공개 시간 컴포넌트
//@OptIn(ExperimentalLayoutApi::class)
//@Composable
//fun DesiredRevealTime(modifier: Modifier = Modifier) {
//    var revealTime by remember { mutableStateOf(0) } // 공개 시간 상태 (시간 단위)
//
//    Column(modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            // 마이너스 버튼
//            Button(
//                onClick = { if (revealTime > 0) revealTime-- },
//                shape = RoundedCornerShape(8.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//            ) {
//                Text(text = "-", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
//            }
//
//            // 시간 표시 및 입력 필드
//            OutlinedTextField(
//                value = revealTime.toString(),
//                onValueChange = { newValue ->
//                    // 숫자만 입력 가능하도록 필터링하고, Int로 변환
//                    revealTime = newValue.filter { it.isDigit() }.toIntOrNull() ?: 0
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 8.dp)
//                    .height(56.dp), // Height to match buttons
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
//                shape = RoundedCornerShape(8.dp),
//                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = MaterialTheme.colorScheme.primary,
//                    unfocusedBorderColor = Color.LightGray,
//                )
//            )
//
//            // 플러스 버튼
//            Button(
//                onClick = { revealTime++ },
//                shape = RoundedCornerShape(8.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//            ) {
//                Text(text = "+", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
//            }
//        }
//
//        // 추가 시간 버튼들
//        FlowRow(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            val timeIncrements = listOf(1, 6, 12, 24)
//            timeIncrements.forEach { increment ->
//                Button(
//                    onClick = { revealTime += increment },
//                    modifier = Modifier.weight(1f), // 각 버튼이 균등하게 공간을 차지하도록
//                    shape = RoundedCornerShape(8.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF2FB475),
//                        contentColor = Color.White
//                    ),
//                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
//                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
//                ) {
//                    Text(text = "+${increment}시간")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun InputField(
//    label: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//    placeholder: String,
//    modifier: Modifier = Modifier,
//    leadingIcon: (@Composable () -> Unit)? = null
//) {
//    var isFocused by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = modifier.padding(bottom = 8.dp)
//    ) {
//        Column {
//            BasicTextField(
//                value = value,
//                onValueChange = onValueChange,
//                textStyle = TextStyle(
//                    fontSize = 16.sp,
//                    color = Color.Black
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//                    .onFocusChanged { focusState ->
//                        isFocused = focusState.isFocused
//                    },
//                decorationBox = { innerTextField ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        leadingIcon?.let { icon ->
//                            icon()
//                            Spacer(modifier = Modifier.width(8.dp))
//                        }
//
//                        Box(modifier = Modifier.weight(1f)) {
//                            if (value.isEmpty() && !isFocused) {
//                                Text(
//                                    text = label,
//                                    color = Color.Gray,
//                                    fontWeight = FontWeight.Bold,
//                                    fontSize = 14.5.sp
//                                )
//                            }
//                            innerTextField()
//                        }
//                    }
//                }
//            )
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(1.dp)
//                    .background(Color(0xFFE0E0E0))
//            )
//        }
//    }
//}
//
//
//@Composable
//fun Title(text: String) {
//    Text(
//        text = text,
//        fontSize = 16.sp,
//        fontWeight = FontWeight.ExtraBold,
//        color = Color.Black,
//        modifier = Modifier.padding(bottom = 8.dp)
//    )
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun RegisterDetailScreenPreview() {
//    MaterialTheme {
//        BarterPostRegister()
//    }
//}
//
//


package site.weshare.android.presentation.barter

import android.app.Application
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel // ViewModel import
import androidx.lifecycle.viewModelScope // viewModelScope import
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.weshare.android.R // R.drawable.camera, R.drawable.circle_small 사용을 위해 필요
import androidx.lifecycle.viewmodel.compose.viewModel // viewModel() 함수 사용을 위해 필요
import coil.compose.rememberAsyncImagePainter // Coil import
import android.net.Uri // Uri import
import androidx.activity.compose.rememberLauncherForActivityResult // ActivityResultLauncher import
import androidx.activity.result.contract.ActivityResultContracts // ActivityResultContracts import
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.FileOutputStream

// API 요청 바디를 위한 데이터 클래스
data class PostRequestBody(
    val itemName: String,
    val itemCategoryIdList: List<Int>, // 교환 희망 카테고리 ID 목록
    val itemCondition: String, // "NEW", "LIKE_USE", "USED"
    val itemDescription: String,
    val activeDuration: Int, // 시간 단위
    val locationId: Int = 0 // 현재 UI에 없으므로 기본값 0
)

// 상품 상태를 API 스펙에 맞게 매핑하기 위한 Enum
enum class ItemCondition(val apiValue: String) {
    NEW("NEW"),
    LIKE_USE("LIKE_USE"), // 사용감 적음
    USED("USED") // 사용감 많음
}

// 교환 희망 카테고리를 API 스펙에 맞게 매핑하기 위한 맵
val tradeCategoryMap = mapOf(
    "의류" to 101,
    "신발" to 102,
    "디지털기기" to 103,
    "뷰티/미용" to 104,
    "가구" to 105,
    "생활가전" to 106,
    "게임" to 107,
    "도서/티켓/음반" to 108,
    "피규어/인형" to 109,
    "스포츠" to 110
)

// Retrofit API 인터페이스 정의
interface BarterPostApi {
    @Multipart
    @POST("https://we-share.site/exchanges/posts") // 실제 API 엔드포인트로 변경하세요.
    suspend fun registerBarterPost(
        @Header("access") accessToken: String,
        @Part("post") post: RequestBody, // JSON 데이터를 RequestBody로 전송
        @Part images: List<MultipartBody.Part> // 이미지 파일 목록
    ): retrofit2.Response<Unit> // 응답 바디가 없을 경우 Unit, 또는 실제 응답 데이터 클래스
}

// 실제 API 호출 서비스 구현
class BarterPostService(private val context: Context) { // Context를 생성자로 받습니다.
    private val retrofit: Retrofit
    private val barterPostApi: BarterPostApi

    init {
        // OkHttpClient에 로깅 인터셉터 추가 (디버깅용)
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // 요청/응답 본문까지 로깅
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://we-share.site/") // 실제 백엔드 API 기본 URL로 변경하세요.
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson())) // Gson 컨버터 사용
            .build()

        barterPostApi = retrofit.create(BarterPostApi::class.java)
    }

    suspend fun registerBarterPost(
        accessToken: String,
        postRequestBody: PostRequestBody,
        imageUris: List<Uri>
    ): Boolean {
        return try {
            // 1. PostRequestBody를 JSON 문자열로 변환하여 RequestBody 생성
            val postJson = Gson().toJson(postRequestBody)
            val postPart = postJson.toRequestBody("application/json".toMediaTypeOrNull())

            // 2. 이미지 Uri들을 MultipartBody.Part 리스트로 변환
            val imageParts = imageUris.mapNotNull { uri ->
                // Uri에서 실제 파일 경로를 얻거나, InputStream을 사용하여 RequestBody 생성
                // 여기서는 간단하게 Uri를 파일로 복사한 후 RequestBody로 만듭니다.
                // 실제 프로덕션 코드에서는 더 효율적인 방법을 고려해야 합니다 (예: ContentResolver.openInputStream).
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("images", file.name, requestBody)
                }
            }

            if (imageParts.isEmpty() && imageUris.isNotEmpty()) {
                println("Error: Failed to create image parts from URIs.")
                return false
            }

            val response = barterPostApi.registerBarterPost(
                accessToken = accessToken,
                post = postPart,
                images = imageParts
            )

            response.isSuccessful
        } catch (e: Exception) {
            println("API 호출 중 오류 발생: ${e.localizedMessage}")
            e.printStackTrace()
            false
        }
    }
}

// ViewModel 정의
class BarterPostRegisterViewModel(application: Application) : AndroidViewModel(application) { // AndroidViewModel 상속
    private val barterPostService = BarterPostService(application.applicationContext) // Application Context 전달

    // UI 상태 변수들
    var itemName by mutableStateOf("")
        private set

    var selectedTradeCategories by mutableStateOf(listOf<String>())
        private set

    var selectedProductState by mutableStateOf<String?>(null)
        private set

    var description by mutableStateOf("")
        private set

    var revealTime by mutableStateOf(0)
        private set

    var selectedImages by mutableStateOf(listOf<Uri>())
        private set

    var showSnackbar by mutableStateOf(false)
        private set

    var snackbarMessage by mutableStateOf("")
        private set

    var isRegistering by mutableStateOf(false)
        private set

    // 상태 업데이트 함수들
    fun onNameChange(newName: String) {
        itemName = newName
    }

    fun onTradeCategorySelected(category: String) {
        selectedTradeCategories = if (selectedTradeCategories.contains(category)) {
            selectedTradeCategories - category
        } else {
            selectedTradeCategories + category
        }
    }

    fun onProductStateSelected(state: String) {
        selectedProductState = if (selectedProductState == state) null else state
    }

    fun onDescriptionChange(newDescription: String) {
        description = newDescription
    }

    fun onRevealTimeChanged(newTime: Int) {
        revealTime = newTime
    }

    fun addImageUris(uris: List<Uri>) {
        selectedImages = (selectedImages + uris).distinct()
        snackbarMessage = "이미지 ${uris.size}개 추가됨. 총 ${selectedImages.size}개"
        showSnackbar = true
    }

    fun hideSnackbar() {
        showSnackbar = false
    }

    // 게시물 등록 로직
    fun registerPost() {
        if (isRegistering) return

        // 필수 필드 유효성 검사
        if (itemName.isBlank()) {
            snackbarMessage = "상품명을 입력해주세요."
            showSnackbar = true
            return
        }
        if (selectedTradeCategories.isEmpty()) {
            snackbarMessage = "교환 희망 카테고리를 최소 하나 선택해주세요."
            showSnackbar = true
            return
        }
        if (selectedProductState == null) {
            snackbarMessage = "상품 상태를 선택해주세요."
            showSnackbar = true
            return
        }
        if (description.isBlank()) {
            snackbarMessage = "상품 설명을 입력해주세요."
            showSnackbar = true
            return
        }
        if (revealTime <= 0) {
            snackbarMessage = "희망 공개 시간을 0보다 크게 설정해주세요."
            showSnackbar = true
            return
        }
        if (selectedImages.isEmpty()) {
            snackbarMessage = "사진을 최소 한 장 추가해주세요."
            showSnackbar = true
            return
        }

        isRegistering = true
        viewModelScope.launch {
            try {
                // 데이터 매핑
                val itemCategoryIds = selectedTradeCategories.map { categoryName ->
                    tradeCategoryMap[categoryName] ?: 0 // 매핑 실패 시 0 또는 에러 처리
                }

                val itemConditionApiValue = when (selectedProductState) {
                    "새상품" -> ItemCondition.NEW.apiValue
                    "사용감 적음" -> ItemCondition.LIKE_USE.apiValue
                    "사용감 많음" -> ItemCondition.USED.apiValue
                    else -> ""
                }

                val postBody = PostRequestBody(
                    itemName = itemName,
                    itemCategoryIdList = itemCategoryIds,
                    itemCondition = itemConditionApiValue,
                    itemDescription = description,
                    activeDuration = revealTime,
                    locationId = 1
                )

                // API 호출
                val success = barterPostService.registerBarterPost(
                    accessToken = "YOUR_ACCESS_TOKEN_HERE", // 실제 AccessToken 값으로 대체 필요
                    postRequestBody = postBody,
                    imageUris = selectedImages // Uri 리스트 전달
                )

                if (success) {
                    snackbarMessage = "상품이 성공적으로 등록되었습니다!"
                    // 성공 후 폼 초기화 또는 화면 이동 로직 추가
                    // resetForm()
                } else {
                    snackbarMessage = "상품 등록에 실패했습니다. 다시 시도해주세요."
                }
            } catch (e: Exception) {
                snackbarMessage = "오류 발생: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                showSnackbar = true
                isRegistering = false
            }
        }
    }

    fun resetForm() {
        itemName = ""
        selectedTradeCategories = emptyList()
        selectedProductState = null
        description = ""
        revealTime = 0
        selectedImages = emptyList()
        showSnackbar = false
        snackbarMessage = ""
        isRegistering = false
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BarterPostRegister(
    viewModel: BarterPostRegisterViewModel = viewModel(factory = BarterPostRegisterViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            viewModel.addImageUris(uris)
        }
    }

    LaunchedEffect(viewModel.showSnackbar) {
        if (viewModel.showSnackbar) {
            snackbarHostState.showSnackbar(
                message = viewModel.snackbarMessage,
                duration = SnackbarDuration.Short
            )
            viewModel.hideSnackbar()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { /* 닫기 로직 */ }
                )
            }

            // 상품정보 섹션
            Text(
                text = "상품정보",
                fontSize = 17.5.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // 사진 등록
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .clickable {
                            galleryLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "사진 추가",
                            tint = Color.Gray,
                            modifier = Modifier.size(25.dp)
                        )
                        Text(
                            text = "사진 추가",
                            fontSize = 12.sp,
                            letterSpacing = (-0.8).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                // 선택된 이미지들을 보여주는 부분
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.selectedImages.forEach { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    }
                }
            }

            // 상품명
            InputField(
                label = "상품명",
                value = viewModel.itemName,
                onValueChange = viewModel::onNameChange,
                placeholder = "상품명을 입력해주세요."
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 교환 희망 카테고리 (다중 선택)
            Title("교환 희망 카테고리")
            val tradeCategories = listOf(
                "의류", "신발", "디지털기기", "뷰티/미용", "가구",
                "생활가전", "게임", "도서/티켓/음반", "피규어/인형", "스포츠"
            )
            CategorySelectionGroup(
                options = tradeCategories,
                selectedOptions = viewModel.selectedTradeCategories,
                onOptionSelected = viewModel::onTradeCategorySelected,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 상품 상태 (단일 선택)
            Title("상품 상태")
            val productStates = listOf("새상품", "사용감 적음" , "사용감 많음")
            CategorySelectionGroup(
                options = productStates,
                selectedOptions = listOfNotNull(viewModel.selectedProductState),
                onOptionSelected = viewModel::onProductStateSelected,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 설명 섹션
            Title("설명")

            BasicTextField(
                value = viewModel.description,
                onValueChange = viewModel::onDescriptionChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFFF4F4F4), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                decorationBox = { innerTextField ->
                    if (viewModel.description.isEmpty()) {
                        Row(
                            verticalAlignment = Alignment.Top,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.circle_small),
                                contentDescription = "설명 아이콘",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(20.dp)
                                    .offset(x = 2.dp, y = (2).dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "브랜드, 상품명, 구매 개수 등 상품 설명을 최대한\n자세히 적어주세요.",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    innerTextField()
                    Text(
                        text = "${viewModel.description.length}/1000",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 70.dp, bottom = 0.dp)
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))


            // 희망 공개 시간
            Title("희망 공개 시간")
            DesiredRevealTime(
                initialRevealTime = viewModel.revealTime,
                onRevealTimeChanged = viewModel::onRevealTimeChanged
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 등록 버튼
            Button(
                onClick = viewModel::registerPost,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2FB475)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !viewModel.isRegistering
            ) {
                if (viewModel.isRegistering) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "등록하기",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
    }
}


// 선택 가능한 태그 컴포넌트
@Composable
fun SelectableTag(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFF2FB475) else Color.Transparent
    val contentColor = if (isSelected) Color.White else Color.DarkGray
    val borderColor = if (isSelected) Color(0xFF2FB475) else Color.LightGray

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = BorderStroke(1.dp, borderColor),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(text = text)
    }
}


// 카테고리 선택 그룹 컴포넌트
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelectionGroup(
    options: List<String>,
    selectedOptions: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                val isSelected = selectedOptions.contains(option)
                SelectableTag(
                    text = option,
                    isSelected = isSelected,
                    onClick = { onOptionSelected(option) }
                )
            }
        }
    }
}

// 희망 공개 시간 컴포넌트
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DesiredRevealTime(
    modifier: Modifier = Modifier,
    initialRevealTime: Int,
    onRevealTimeChanged: (Int) -> Unit
) {
    var revealTimeText by remember(initialRevealTime) { mutableStateOf(initialRevealTime.toString()) }

    LaunchedEffect(initialRevealTime) {
        revealTimeText = initialRevealTime.toString()
    }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val newTime = (revealTimeText.toIntOrNull() ?: 0) - 1
                    onRevealTimeChanged(if (newTime > 0) newTime else 0)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "-", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
            }

            OutlinedTextField(
                value = revealTimeText,
                onValueChange = { newValue ->
                    revealTimeText = newValue.filter { it.isDigit() }
                    onRevealTimeChanged(revealTimeText.toIntOrNull() ?: 0)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .height(56.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray,
                )
            )

            Button(
                onClick = {
                    val newTime = (revealTimeText.toIntOrNull() ?: 0) + 1
                    onRevealTimeChanged(newTime)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = "+", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
            }
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val timeIncrements = listOf(1, 6, 12, 24)
            timeIncrements.forEach { increment ->
                Button(
                    onClick = {
                        val newTime = (revealTimeText.toIntOrNull() ?: 0) + increment
                        onRevealTimeChanged(newTime)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2FB475),
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFF2FB475).copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(text = "+${increment}시간")
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(bottom = 8.dp)
    ) {
        Column {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        leadingIcon?.let { icon ->
                            icon()
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            if (value.isEmpty() && !isFocused) {
                                Text(
                                    text = placeholder,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.5.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color(0xFFE0E0E0))
            )
        }
    }
}


@Composable
fun Title(text: String) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.ExtraBold,
        color = Color.Black,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )

}

@Preview(showBackground = true)
@Composable
fun RegisterDetailScreenPreview() {
    MaterialTheme {
        BarterPostRegister()
    }
}

// ViewModel에 Context를 전달하기 위한 Factory
class BarterPostRegisterViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarterPostRegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarterPostRegisterViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
