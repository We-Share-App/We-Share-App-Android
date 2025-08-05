//package site.weshare.android.presentation.barter
//
//import android.app.Application
//import android.content.Context
//import android.net.Uri
//import android.util.Log // Log import
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
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
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.focus.onFocusChanged
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import site.weshare.android.R
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.lifecycle.ViewModelProvider
//import coil.compose.rememberAsyncImagePainter
//import com.google.gson.Gson
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.OkHttpClient
//import okhttp3.RequestBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import okhttp3.RequestBody.Companion.toRequestBody
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.Header
//import retrofit2.http.Multipart
//import retrofit2.http.POST
//import retrofit2.http.Part
//import java.io.File
//import java.io.FileOutputStream
//import site.weshare.android.util.getAccessToken
//
//
//
//
//// API 요청 바디를 위한 데이터 클래스
//data class PostRequestBody(
//    val itemName: String,
//    val itemCategoryIdList: List<Long>, // Long으로 변경
//    val itemCondition: String,
//    val itemDescription: String,
//    val activeDuration: Int,
//    val locationId: Long // Long으로 변경
//)
//
//// 상품 상태를 API 스펙에 맞게 매핑하기 위한 Enum
//enum class ItemCondition(val apiValue: String) {
//    NEW("NEW"),
//    LIKE_NEW("LIKE_NEW"),
//    USED("USED")
//}
//
//// 교환 희망 카테고리를 API 스펙에 맞게 매핑하기 위한 맵
//val tradeCategoryMap = mapOf(
//    "의류" to 1L,
//    "신발" to 2L,
//    "디지털기기" to 3L,
//    "뷰티/미용" to 4L,
//    "가구" to 5L,
//    "생활가전" to 6L,
//    "게임" to 7L,
//    "도서/티켓/음반" to 8L,
//    "피규어/인형" to 9L,
//    "스포츠" to 10L,
//)
//
//// API 응답 바디를 위한 데이터 클래스 추가
//data class BarterPostResponse(
//    val isSuccess: Boolean,
//    val exchangePostId: Long // Long으로 변경, 0으로 초기화될 수 있음
//)
//
//// Retrofit API 인터페이스 정의
//interface BarterPostApi {
//    @Multipart
//    @POST("https://we-share.site/exchanges/posts") // 실제 API 엔드포인트로 변경하세요.
//    suspend fun registerBarterPost(
//        @Header("access") accessToken: String,
//        @Part("post") post: RequestBody,
//        @Part images: List<MultipartBody.Part>
//    ): retrofit2.Response<BarterPostResponse> // 응답 타입 변경
//}
//
////  API 호출 서비스 구현
//class BarterPostService(private val context: Context) {
//    private val retrofit: Retrofit
//    private val barterPostApi: BarterPostApi
//
//    init {
//        val logging = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .build()
//
//        retrofit = Retrofit.Builder()
//            .baseUrl("https://we-share.site/") // 실제 백엔드 API 기본 URL로 변경하세요.
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create(Gson()))
//            .build()
//
//        barterPostApi = retrofit.create(BarterPostApi::class.java)
//    }
//
//    suspend fun registerBarterPost(
//        accessToken: String,
//        postRequestBody: PostRequestBody,
//        imageUris: List<Uri>
//    ): Boolean {
//        return try {
//            Log.d("BarterPostService", "API Request preparation started.")
//            Log.d("BarterPostService", "PostRequestBody: $postRequestBody")
//            val postJson = Gson().toJson(postRequestBody)
//            val postPart = postJson.toRequestBody("application/json".toMediaTypeOrNull())
//            Log.d("BarterPostService", "Post JSON part created.")
//
//            Log.d("BarterPostService", "Processing ${imageUris.size} image URIs.")
//            val imageParts = imageUris.mapNotNull { uri ->
//                try {
//                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
//                        val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
//                        FileOutputStream(file).use { outputStream ->
//                            inputStream.copyTo(outputStream)
//                        }
//                        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
//                        Log.d("BarterPostService", "Successfully created image part for URI: $uri, file: ${file.name}")
//                        MultipartBody.Part.createFormData("images", file.name, requestBody)
//                    } ?: run {
//                        Log.e("BarterPostService", "Failed to open InputStream for URI: $uri. Check if URI is valid or file exists.")
//                        null
//                    }
//                } catch (e: Exception) {
//                    Log.e("BarterPostService", "Exception creating image part for URI: $uri. Error: ${e.localizedMessage}", e)
//                    null
//                }
//            }
//
//            Log.d("BarterPostService", "Finished processing image URIs. Created ${imageParts.size} image parts.")
//
//            if (imageParts.isEmpty() && imageUris.isNotEmpty()) {
//                Log.e("BarterPostService", "Error: No image parts could be created despite having URIs. This likely indicates a file access/permission issue or invalid URIs.")
//                return false
//            } else if (imageParts.isEmpty() && imageUris.isEmpty()) {
//                Log.d("BarterPostService", "No images to upload, proceeding without image parts.")
//            }
//
//
//            Log.d("BarterPostService", "Attempting API call to ${retrofit.baseUrl()}barter/post with AccessToken: ${accessToken.take(10)}...")
//            val response = barterPostApi.registerBarterPost(
//                accessToken = accessToken,
//                post = postPart,
//                images = imageParts
//            )
//
//            Log.d("BarterPostService", "API Response received. HTTP Code: ${response.code()}")
//            if (response.isSuccessful) {
//                val responseBody = response.body()
//                if (responseBody != null) {
//                    Log.d("BarterPostService", "API Response Body (Parsed): $responseBody")
//                    if (responseBody.isSuccess) {
//                        Log.d("BarterPostService", "API call successful! isSuccess: true, exchangePostId: ${responseBody.exchangePostId}")
//                        true
//                    } else {
//                        Log.e("BarterPostService", "API call failed: isSuccess is false in response body. Response: $responseBody")
//                        false
//                    }
//                } else {
//                    Log.e("BarterPostService", "Successful HTTP response (2xx) but response body is null.")
//                    false
//                }
//            } else {
//                val errorBody = response.errorBody()?.string()
//                Log.e("BarterPostService", "API call failed. HTTP Code: ${response.code()}, Error Body: $errorBody")
//                false
//            }
//        } catch (e: Exception) {
//            Log.e("BarterPostService", "Critical Exception during API call process: ${e.localizedMessage}", e)
//            false
//        }
//    }
//}
//
//// ViewModel 정의
//class BarterPostRegisterViewModel(application: Application) : AndroidViewModel(application) {
//    private val barterPostService = BarterPostService(application.applicationContext)
//
//    // UI 상태 변수들
//    var itemName by mutableStateOf("")
//        private set
//
//    var selectedTradeCategories by mutableStateOf(listOf<String>())
//        private set
//
//    var selectedProductState by mutableStateOf<String?>(null)
//        private set
//
//    var description by mutableStateOf("")
//        private set
//
//    var revealTime by mutableStateOf(0)
//        private set
//
//    var selectedImages by mutableStateOf(listOf<Uri>())
//        private set
//
//    var showSnackbar by mutableStateOf(false)
//        private set
//
//    var snackbarMessage by mutableStateOf("")
//        private set
//
//    var isRegistering by mutableStateOf(false)
//        private set
//
//    // 상태 업데이트 함수들
//    fun onNameChange(newName: String) {
//        itemName = newName
//    }
//
//    fun onTradeCategorySelected(category: String) {
//        selectedTradeCategories = if (selectedTradeCategories.contains(category)) {
//            selectedTradeCategories - category
//        } else {
//            selectedTradeCategories + category
//        }
//    }
//
//    fun onProductStateSelected(state: String) {
//        selectedProductState = if (selectedProductState == state) null else state
//    }
//
//    fun onDescriptionChange(newDescription: String) {
//        description = newDescription
//    }
//
//    fun onRevealTimeChanged(newTime: Int) {
//        revealTime = newTime
//    }
//
//    fun addImageUris(uris: List<Uri>) {
//        selectedImages = (selectedImages + uris).distinct()
//        snackbarMessage = "이미지 ${uris.size}개 추가됨. 총 ${selectedImages.size}개"
//        showSnackbar = true
//    }
//
//    fun hideSnackbar() {
//        showSnackbar = false
//    }
//
//    // 게시물 등록 로직
//    fun registerPost() {
//        if (isRegistering) return
//
//        // 필수 필드 유효성 검사
//        Log.d("BarterPostRegisterVM", "Starting registerPost. Validating inputs...")
//        if (itemName.isBlank()) {
//            snackbarMessage = "상품명을 입력해주세요."
//            showSnackbar = true
//            Log.d("BarterPostRegisterVM", "Validation failed: Item name is blank.")
//            return
//        }
//        if (selectedTradeCategories.isEmpty()) {
//            snackbarMessage = "교환 희망 카테고리를 최소 하나 선택해주세요."
//            showSnackbar = true
//            Log.d("BarterPostRegisterVM", "Validation failed: No trade categories selected.")
//            return
//        }
//        if (selectedProductState == null) {
//            snackbarMessage = "상품 상태를 선택해주세요."
//            showSnackbar = true
//            Log.d("BarterPostRegisterVM", "Validation failed: Product state not selected.")
//            return
//        }
//        if (description.isBlank()) {
//            snackbarMessage = "상품 설명을 입력해주세요."
//            showSnackbar = true
//            Log.d("BarterPostRegisterVM", "Validation failed: Description is blank.")
//            return
//        }
//        if (revealTime <= 0) {
//            snackbarMessage = "희망 공개 시간을 0보다 크게 설정해주세요."
//            showSnackbar = true
//            Log.d("BarterPostRegisterVM", "Validation failed: Reveal time is zero or less.")
//            return
//        }
//        if (selectedImages.isEmpty()) {
//            snackbarMessage = "사진을 최소 한 장 추가해주세요."
//            showSnackbar = true
//            Log.d("BarterPostRegisterVM", "Validation failed: No images selected.")
//            return
//        }
//        Log.d("BarterPostRegisterVM", "Input validation passed.")
//
//
//        isRegistering = true
//        viewModelScope.launch {
//            try {
//                // 저장된 Access Token 가져오기
//                val accessToken = getAccessToken(getApplication())
//                Log.d("BarterPostRegisterVM", "Retrieved Access Token: ${accessToken?.take(10)}...")
//                if (accessToken == null) {
//                    snackbarMessage = "로그인 정보가 없습니다. 다시 로그인해주세요."
//                    showSnackbar = true
//                    isRegistering = false
//                    Log.e("BarterPostRegisterVM", "Access Token is null. Cannot proceed with API call.")
//                    return@launch
//                }
//
//                // 데이터 매핑
//                val itemCategoryIds = selectedTradeCategories.map { categoryName ->
//                    tradeCategoryMap[categoryName] ?: 0
//                }
//                Log.d("BarterPostRegisterVM", "Mapped itemCategoryIds: $itemCategoryIds")
//
//
//                val itemConditionApiValue = when (selectedProductState) {
//                    "새상품" -> ItemCondition.NEW.apiValue
//                    "사용감 적음" -> ItemCondition.LIKE_NEW.apiValue
//                    "사용감 많음" -> ItemCondition.USED.apiValue
//                    else -> ""
//                }
//                Log.d("BarterPostRegisterVM", "Mapped itemCondition: $itemConditionApiValue")
//
//
//                val postBody = PostRequestBody(
//                    itemName = itemName,
//                    itemCategoryIdList = itemCategoryIds,
//                    itemCondition = itemConditionApiValue,
//                    itemDescription = description,
//                    activeDuration = revealTime,
//                    locationId = 1
//                )
//                Log.d("BarterPostRegisterVM", "Constructed PostRequestBody: $postBody")
//
//
//                // API 호출
//                val success = barterPostService.registerBarterPost(
//                    accessToken = accessToken,
//                    postRequestBody = postBody,
//                    imageUris = selectedImages
//                )
//
//                if (success) {
//                    snackbarMessage = "상품이 성공적으로 등록되었습니다!"
//                    Log.d("BarterPostRegisterVM", "API call successful!")
//                    // 성공 후 폼 초기화 또는 화면 이동 로직 추가
//                    // resetForm()
//                } else {
//                    snackbarMessage = "상품 등록에 실패했습니다. 다시 시도해주세요."
//                    Log.e("BarterPostRegisterVM", "API call failed (response not successful or isSuccess is false).")
//                }
//            } catch (e: Exception) {
//                snackbarMessage = "오류 발생: ${e.localizedMessage}"
//                Log.e("BarterPostRegisterVM", "Exception during API call: ${e.localizedMessage}", e)
//            } finally {
//                showSnackbar = true
//                isRegistering = false
//                Log.d("BarterPostRegisterVM", "registerPost finished. isRegistering: $isRegistering, showSnackbar: $showSnackbar")
//            }
//        }
//    }
//
//    fun resetForm() {
//        itemName = ""
//        selectedTradeCategories = emptyList()
//        selectedProductState = null
//        description = ""
//        revealTime = 0
//        selectedImages = emptyList()
//        showSnackbar = false
//        snackbarMessage = ""
//        isRegistering = false
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
//@Composable
//fun BarterPostRegister(
//    viewModel: BarterPostRegisterViewModel = viewModel(factory = BarterPostRegisterViewModelFactory(LocalContext.current.applicationContext as Application))
//) {
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    val galleryLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetMultipleContents()
//    ) { uris: List<Uri> ->
//        if (uris.isNotEmpty()) {
//            viewModel.addImageUris(uris)
//        }
//    }
//
//    LaunchedEffect(viewModel.showSnackbar) {
//        if (viewModel.showSnackbar) {
//            snackbarHostState.showSnackbar(
//                message = viewModel.snackbarMessage,
//                duration = SnackbarDuration.Short
//            )
//            viewModel.hideSnackbar()
//        }
//    }
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
//                        .clickable {
//                            galleryLauncher.launch("image/*")
//                        },
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
//                Spacer(modifier = Modifier.width(8.dp))
//                // 선택된 이미지들을 보여주는 부분
//                FlowRow(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    viewModel.selectedImages.forEach { uri ->
//                        Image(
//                            painter = rememberAsyncImagePainter(uri),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .size(60.dp)
//                                .clip(RoundedCornerShape(8.dp)),
//                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
//                        )
//                    }
//                }
//            }
//
//            // 상품명
//            InputField(
//                label = "상품명",
//                value = viewModel.itemName,
//                onValueChange = viewModel::onNameChange,
//                placeholder = "상품명을 입력해주세요."
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // 교환 희망 카테고리 (다중 선택)
//            Title("교환 희망 카테고리")
//            val tradeCategories = listOf(
//                "의류", "신발", "디지털기기", "뷰티/미용", "가구",
//                "생활가전", "게임", "도서/티켓/음반", "피규어/인형", "스포츠"
//            )
//            CategorySelectionGroup(
//                options = tradeCategories,
//                selectedOptions = viewModel.selectedTradeCategories,
//                onOptionSelected = viewModel::onTradeCategorySelected,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            // 상품 상태 (단일 선택)
//            Title("상품 상태")
//            val productStates = listOf("새상품", "사용감 적음" , "사용감 많음")
//            CategorySelectionGroup(
//                options = productStates,
//                selectedOptions = listOfNotNull(viewModel.selectedProductState),
//                onOptionSelected = viewModel::onProductStateSelected,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            // 설명 섹션
//            Title("설명")
//
//            BasicTextField(
//                value = viewModel.description,
//                onValueChange = viewModel::onDescriptionChange,
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
//                    if (viewModel.description.isEmpty()) {
//                        Row(
//                            verticalAlignment = Alignment.Top,
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
//                    }
//                    innerTextField()
//                    Text(
//                        text = "${viewModel.description.length}/1000",
//                        fontSize = 10.sp,
//                        color = Color.Gray,
//                        textAlign = TextAlign.End,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 70.dp, bottom = 0.dp)
//                    )
//                }
//            )
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            // 희망 공개 시간
//            Title("희망 공개 시간")
//            DesiredRevealTime(
//                initialRevealTime = viewModel.revealTime,
//                onRevealTimeChanged = viewModel::onRevealTimeChanged
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // 등록 버튼
//            Button(
//                onClick = viewModel::registerPost,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF2FB475)
//                ),
//                shape = RoundedCornerShape(8.dp),
//                enabled = !viewModel.isRegistering
//            ) {
//                if (viewModel.isRegistering) {
//                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
//                } else {
//                    Text(
//                        text = "등록하기",
//                        color = Color.White,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//        }
//        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.BottomCenter))
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
//    val backgroundColor = if (isSelected) Color(0xFF2FB475) else Color.Transparent
//    val contentColor = if (isSelected) Color.White else Color.DarkGray
//    val borderColor = if (isSelected) Color(0xFF2FB475) else Color.LightGray
//
//    Button(
//        onClick = onClick,
//        modifier = modifier,
//        shape = RoundedCornerShape(20.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = backgroundColor,
//            contentColor = contentColor
//        ),
//        border = BorderStroke(1.dp, borderColor),
//        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
//    ) {
//        Text(text = text)
//    }
//}
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
//    Column(modifier = modifier.padding(vertical = 8.dp)) {
//        FlowRow(
//            modifier = Modifier.fillMaxWidth(),
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
//fun DesiredRevealTime(
//    modifier: Modifier = Modifier,
//    initialRevealTime: Int,
//    onRevealTimeChanged: (Int) -> Unit
//) {
//    var revealTimeText by remember(initialRevealTime) { mutableStateOf(initialRevealTime.toString()) }
//
//    LaunchedEffect(initialRevealTime) {
//        revealTimeText = initialRevealTime.toString()
//    }
//
//    Column(modifier = modifier.padding(vertical = 8.dp)) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Button(
//                onClick = {
//                    val newTime = (revealTimeText.toIntOrNull() ?: 0) - 1
//                    onRevealTimeChanged(if (newTime > 0) newTime else 0)
//                },
//                shape = RoundedCornerShape(8.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//            ) {
//                Text(text = "-", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
//            }
//
//            OutlinedTextField(
//                value = revealTimeText,
//                onValueChange = { newValue ->
//                    revealTimeText = newValue.filter { it.isDigit() }
//                    onRevealTimeChanged(revealTimeText.toIntOrNull() ?: 0)
//                },
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 8.dp)
//                    .height(56.dp),
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center),
//                shape = RoundedCornerShape(8.dp),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = MaterialTheme.colorScheme.primary,
//                    unfocusedBorderColor = Color.LightGray,
//                )
//            )
//
//            Button(
//                onClick = {
//                    val newTime = (revealTimeText.toIntOrNull() ?: 0) + 1
//                    onRevealTimeChanged(newTime)
//                },
//                shape = RoundedCornerShape(8.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//            ) {
//                Text(text = "+", style = MaterialTheme.typography.titleLarge, color = Color.DarkGray)
//            }
//        }
//
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
//                    onClick = {
//                        val newTime = (revealTimeText.toIntOrNull() ?: 0) + increment
//                        onRevealTimeChanged(newTime)
//                    },
//                    modifier = Modifier.weight(1f),
//                    shape = RoundedCornerShape(8.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF2FB475),
//                        contentColor = Color.White
//                    ),
//                    border = BorderStroke(1.dp, Color(0xFF2FB475).copy(alpha = 0.5f)),
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
//                                    text = placeholder,
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
//        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
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
//// ViewModel에 Context를 전달하기 위한 Factory
//class BarterPostRegisterViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(BarterPostRegisterViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return BarterPostRegisterViewModel(application) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}




package site.weshare.android.presentation.barter

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns // OpenableColumns import
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import site.weshare.android.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.BufferedSink
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import site.weshare.android.util.getAccessToken


// API 요청 바디를 위한 데이터 클래스
data class PostRequestBody(
    val itemName: String,
    val itemCategoryIdList: List<Long>,
    val itemCondition: String,
    val itemDescription: String,
    val activeDuration: Int,
    val locationId: Long
)

// 상품 상태를 API 스펙에 맞게 매핑하기 위한 Enum
enum class ItemCondition(val apiValue: String) {
    NEW("NEW"),
    LIKE_NEW("LIKE_NEW"),
    USED("USED")
}

// 교환 희망 카테고리를 API 스펙에 맞게 매핑하기 위한 맵
val tradeCategoryMap = mapOf(
    "의류" to 1L,
    "신발" to 2L,
    "디지털기기" to 3L,
    "뷰티/미용" to 4L,
    "가구" to 5L,
    "생활가전" to 6L,
    "게임" to 7L,
    "도서/티켓/음반" to 8L,
    "피규어/인형" to 9L,
    "스포츠" to 10L
)

// API 응답 바디를 위한 데이터 클래스 추가
data class BarterPostResponse(
    val isSuccess: Boolean,
    val exchangePostId: Long
)

// Retrofit API 인터페이스 정의
interface BarterPostApi {
    @Multipart
    @POST("https://we-share.site/exchanges/posts")
    suspend fun createExchangePost( // 함수 이름도 서버 API에 맞게 변경
        @Header("access") accessToken: String,
        @Part("post") post: RequestBody,
        @Part images: List<MultipartBody.Part> // 서버는 List<MultipartFile>을 받으므로, 클라이언트에서는 List<MultipartBody.Part>로 보냅니다.
    ): retrofit2.Response<BarterPostResponse>
}

/**
 * InputStream으로부터 데이터를 읽어 RequestBody를 생성하는 커스텀 클래스.
 * 파일을 디스크에 임시 저장하지 않고 스트리밍 방식으로 업로드할 때 사용합니다.
 *
 * @param contentType 전송할 데이터의 MIME 타입 (예: "image/jpeg")
 * @param inputStream 데이터를 읽어올 InputStream
 * @param contentLength 데이터의 총 길이 (알 수 없는 경우 -1L)
 */
class InputStreamRequestBody(
    private val context: Context, // Context를 생성자로 받습니다.
    private val uri: Uri, // Uri를 생성자로 받습니다.
    private val contentType: MediaType?,
    private val contentLength: Long = -1L
) : RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = contentLength

    override fun writeTo(sink: BufferedSink) {
        // writeTo가 호출될 때마다 새로운 InputStream을 엽니다.
        context.contentResolver.openInputStream(uri)?.use { stream ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesRead: Int
            while (stream.read(buffer).also { bytesRead = it } != -1) {
                sink.write(buffer, 0, bytesRead)
            }
        } ?: Log.e("InputStreamRequestBody", "Failed to open InputStream for URI: $uri during writeTo.")
    }

    companion object {
        // Uri로부터 InputStreamRequestBody를 생성하는 팩토리 메서드
        fun create(context: Context, uri: Uri): InputStreamRequestBody? {
            val contentResolver = context.contentResolver
            val resolvedType = contentResolver.getType(uri) // Get the resolved MIME type
            Log.d("InputStreamRequestBody", "Resolved MIME type for URI $uri: $resolvedType")

            // Fallback to a specific image MIME type if resolvedType is null or generic
            val contentType = resolvedType?.toMediaTypeOrNull()
                ?: "image/jpeg".toMediaTypeOrNull() // Default to JPEG if no specific type is found

            if (contentType == null) {
                Log.e("InputStreamRequestBody", "Critical: Could not determine a valid MediaType for URI: $uri. This image might fail upload.")
                return null // Or throw an exception if strict
            }

            // InputStream 자체는 여기서 열지 않고, RequestBody의 writeTo에서 열도록 변경
            val contentLength = try {
                var length = -1L
                val cursor = contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                        if (sizeIndex != -1) {
                            length = it.getLong(sizeIndex)
                        }
                    }
                }
                length
            } catch (e: Exception) {
                Log.e("InputStreamRequestBody", "Failed to get content length for URI: $uri, Error: ${e.localizedMessage}")
                -1L
            }

            return InputStreamRequestBody(context, uri, contentType, contentLength)
        }
    }
}

// Uri로부터 파일 이름을 가져오는 유틸리티 함수
private fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    result = it.getString(displayNameIndex)
                }
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut!! + 1)
        }
    }
    // 파일 확장자가 없는 경우를 대비하여 기본 확장자 추가 (MIME 타입과 일치하도록)
    return result ?: "image_${System.currentTimeMillis()}.jpeg"
}



// 실제 API 호출 서비스 구현
class BarterPostService(private val context: Context) {
    private val retrofit: Retrofit
    private val barterPostApi: BarterPostApi

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://we-share.site/") // 실제 백엔드 API 기본 URL로 변경 (로그에서 확인)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        barterPostApi = retrofit.create(BarterPostApi::class.java)
    }

    suspend fun createExchangePost(
        accessToken: String,
        postRequestBody: PostRequestBody,
        imageUris: List<Uri>
    ): Boolean {
        return try {
            Log.d("BarterPostService", "API Request preparation started.")
            Log.d("BarterPostService", "PostRequestBody: $postRequestBody")
            val postJson = Gson().toJson(postRequestBody)
            // 서버의 @RequestPart("post")에 매칭되도록 "post"라는 이름의 RequestBody 생성
            // JSON 데이터를 위한 MultipartBody.Part 생성 시 Content-Type을 application/json으로 명시
            val postPart = MultipartBody.Part.createFormData(
                "post", // Part 이름
                "post.json", // 파일 이름 (서버가 요구할 수 있음)
                postJson.toRequestBody("application/json".toMediaTypeOrNull()) // JSON Content-Type
            )
            Log.d("BarterPostService", "Post JSON part created with Content-Type: application/json.")

            Log.d("BarterPostService", "Processing ${imageUris.size} image URIs for streaming upload.")
            val imageParts = imageUris.mapNotNull { uri ->
                try {
                    val fileName = getFileName(context, uri)
                    val requestBody = InputStreamRequestBody.create(context, uri)
                    if (requestBody != null) {
                        Log.d("BarterPostService", "Successfully created streaming image part for URI: $uri, file: $fileName, Content-Type: ${requestBody.contentType()}")
                        // 서버의 @RequestPart("images") List<MultipartFile> images에 매칭되도록 "images"라는 이름으로 파일 파트 생성
                        MultipartBody.Part.createFormData("images", fileName, requestBody)
                    } else {
                        Log.e("BarterPostService", "Failed to create InputStreamRequestBody for URI: $uri. Skipping this image.")
                        null
                    }
                } catch (e: Exception) {
                    Log.e("BarterPostService", "Exception creating streaming image part for URI: $uri. Error: ${e.localizedMessage}", e)
                    null
                }
            }

            Log.d("BarterPostService", "Finished processing image URIs. Created ${imageParts.size} streaming image parts.")

            if (imageParts.isEmpty() && imageUris.isNotEmpty()) {
                Log.e("BarterPostService", "Error: No image parts could be created despite having URIs. This likely indicates a file access/permission issue or invalid URIs.")
                return false
            } else if (imageParts.isEmpty() && imageUris.isEmpty()) {
                Log.d("BarterPostService", "No images to upload, proceeding without image parts.")
            }


            Log.d("BarterPostService", "Attempting API call to ${retrofit.baseUrl()}posts with AccessToken: ${accessToken.take(10)}...")
            val response = barterPostApi.createExchangePost(
                accessToken = accessToken,
                post = postPart.body, // RequestBody만 전달
                images = imageParts
            )

            Log.d("BarterPostService", "API Response received. HTTP Code: ${response.code()}")
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Log.d("BarterPostService", "API Response Body (Parsed): $responseBody")
                    if (responseBody.isSuccess) {
                        Log.d("BarterPostService", "API call successful! isSuccess: true, exchangePostId: ${responseBody.exchangePostId}")
                        true
                    } else {
                        Log.e("BarterPostService", "API call failed: isSuccess is false in response body. Response: $responseBody")
                        false
                    }
                } else {
                    Log.e("BarterPostService", "Successful HTTP response (2xx) but response body is null.")
                    false
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("BarterPostService", "API call failed. HTTP Code: ${response.code()}, Error Body: $errorBody")
                false
            }
        } catch (e: Exception) {
            Log.e("BarterPostService", "Critical Exception during API call process: ${e.localizedMessage}", e)
            false
        }
    }
}

// ViewModel 정의
class BarterPostRegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val barterPostService = BarterPostService(application.applicationContext)

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
        Log.d("BarterPostRegisterVM", "Starting registerPost. Validating inputs...")
        if (itemName.isBlank()) {
            snackbarMessage = "상품명을 입력해주세요."
            showSnackbar = true
            Log.d("BarterPostRegisterVM", "Validation failed: Item name is blank.")
            return
        }
        if (selectedTradeCategories.isEmpty()) {
            snackbarMessage = "교환 희망 카테고리를 최소 하나 선택해주세요."
            showSnackbar = true
            Log.d("BarterPostRegisterVM", "Validation failed: No trade categories selected.")
            return
        }
        if (selectedProductState == null) {
            snackbarMessage = "상품 상태를 선택해주세요."
            showSnackbar = true
            Log.d("BarterPostRegisterVM", "Validation failed: Product state not selected.")
            return
        }
        if (description.isBlank()) {
            snackbarMessage = "상품 설명을 입력해주세요."
            showSnackbar = true
            Log.d("BarterPostRegisterVM", "Validation failed: Description is blank.")
            return
        }
        if (revealTime <= 0) {
            snackbarMessage = "희망 공개 시간을 0보다 크게 설정해주세요."
            showSnackbar = true
            Log.d("BarterPostRegisterVM", "Validation failed: Reveal time is zero or less.")
            return
        }
        if (selectedImages.isEmpty()) {
            snackbarMessage = "사진을 최소 한 장 추가해주세요."
            showSnackbar = true
            Log.d("BarterPostRegisterVM", "Validation failed: No images selected.")
            return
        }
        Log.d("BarterPostRegisterVM", "Input validation passed.")


        isRegistering = true
        val launch = viewModelScope.launch {
            try {
                // 저장된 Access Token 가져오기
                val accessToken = getAccessToken(getApplication())
                Log.d("BarterPostRegisterVM", "Retrieved Access Token: ${accessToken?.take(10)}...")
                if (accessToken == null) {
                    snackbarMessage = "로그인 정보가 없습니다. 다시 로그인해주세요."
                    showSnackbar = true
                    isRegistering = false
                    Log.e(
                        "BarterPostRegisterVM",
                        "Access Token is null. Cannot proceed with API call."
                    )
                    return@launch
                }

                // 데이터 매핑
                val itemCategoryIds = selectedTradeCategories.map { categoryName ->
                    tradeCategoryMap[categoryName] ?: 0L
                }
                Log.d("BarterPostRegisterVM", "Mapped itemCategoryIds: $itemCategoryIds")


                val itemConditionApiValue = when (selectedProductState) {
                    "새상품" -> ItemCondition.NEW.apiValue
                    "사용감 적음" -> ItemCondition.LIKE_NEW.apiValue
                    "사용감 많음" -> ItemCondition.USED.apiValue
                    else -> ""
                }
                Log.d("BarterPostRegisterVM", "Mapped itemCondition: $itemConditionApiValue")


                val postBody = PostRequestBody(
                    itemName = itemName,
                    itemCategoryIdList = itemCategoryIds,
                    itemCondition = itemConditionApiValue,
                    itemDescription = description,
                    activeDuration = revealTime,
                    locationId = 1L
                )
                Log.d("BarterPostRegisterVM", "Constructed PostRequestBody: $postBody")


                // API 호출
                val success = barterPostService.createExchangePost(
                    accessToken = accessToken,
                    postRequestBody = postBody,
                    imageUris = selectedImages
                )

                if (success) {
                    snackbarMessage = "상품이 성공적으로 등록되었습니다!"
                    Log.d("BarterPostRegisterVM", "API call successful!")
                    // 성공 후 폼 초기화 또는 화면 이동 로직 추가
                    // resetForm()
                } else {
                    snackbarMessage = "상품 등록에 실패했습니다. 다시 시도해주세요."
                    Log.e(
                        "BarterPostRegisterVM",
                        "API call failed (response not successful or isSuccess is false)."
                    )
                }
            } catch (e: Exception) {
                snackbarMessage = "오류 발생: ${e.localizedMessage}"
                Log.e("BarterPostRegisterVM", "Exception during API call: ${e.localizedMessage}", e)
            } finally {
                showSnackbar = true
                isRegistering = false
                Log.d(
                    "BarterPostRegisterVM",
                    "registerPost finished. isRegistering: $isRegistering, showSnackbar: $showSnackbar"
                )
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
