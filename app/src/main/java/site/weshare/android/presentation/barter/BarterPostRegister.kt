package site.weshare.android.presentation.barter

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import site.weshare.android.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberAsyncImagePainter
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import site.weshare.android.util.BarterScreen.BarterPostService
import site.weshare.android.util.BarterScreen.CategorySelectionGroup
import site.weshare.android.util.BarterScreen.DesiredRevealTime
import site.weshare.android.util.BarterScreen.InputField
import site.weshare.android.util.BarterScreen.Title
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
