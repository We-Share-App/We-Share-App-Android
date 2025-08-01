package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ==================== 위치 데이터 모델 ====================
data class LocationResponse(
    val id: Int,
    val name: String,
    val isSelected: Boolean,
    val coordinates: LocationCoordinates? = null
)

data class LocationCoordinates(
    val latitude: Double,
    val longitude: Double
)

data class Location(
    val id: Int,
    val name: String,
    val isSelected: Boolean
)

fun LocationResponse.toUiModel(): Location {
    return Location(
        id = id,
        name = name,
        isSelected = isSelected
    )
}

// ==================== API Service & Repository ====================
interface LocationApiService {
    suspend fun getUserLocations(userId: Int): List<LocationResponse>
    suspend fun updateSelectedLocation(userId: Int, locationId: Int): Boolean
}

class LocationRepository(
    private val apiService: LocationApiService? = null
) {
    suspend fun getUserLocations(userId: Int): Result<List<Location>> = try {
        if (apiService != null) {
            val response = apiService.getUserLocations(userId)
            Result.success(response.map { it.toUiModel() })
        } else {
            // Mock 데이터
            Result.success(getMockLocations())
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun updateSelectedLocation(userId: Int, locationId: Int): Result<Boolean> = try {
        if (apiService != null) {
            val success = apiService.updateSelectedLocation(userId, locationId)
            Result.success(success)
        } else {
            Result.success(true)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    private fun getMockLocations(): List<Location> = listOf(
        Location(id = 1, name = "흑석동", isSelected = true),
        Location(id = 2, name = "이태원2동", isSelected = false)
    )
}

// ==================== UI State & ViewModel ====================
data class LocationDialogUiState(
    val locations: List<Location> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentSelectedLocation: Location? = null
)

class LocationViewModel(
    private val repository: LocationRepository = LocationRepository(),
    private val userId: Int = 1 // 실제로는 로그인한 사용자 ID를 사용
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationDialogUiState())
    val uiState: StateFlow<LocationDialogUiState> = _uiState.asStateFlow()

    init {
        loadUserLocations()
    }

    fun loadUserLocations() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            repository.getUserLocations(userId).fold(
                onSuccess = { locations ->
                    val currentSelected = locations.find { it.isSelected }
                    _uiState.value = _uiState.value.copy(
                        locations = locations,
                        currentSelectedLocation = currentSelected,
                        isLoading = false
                    )
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "위치 정보를 불러오는데 실패했습니다",
                        isLoading = false
                    )
                }
            )
        }
    }

    fun selectLocation(location: Location) {
        viewModelScope.launch {
            repository.updateSelectedLocation(userId, location.id).fold(
                onSuccess = { success ->
                    if (success) {
                        // 로컬 상태 업데이트
                        val updatedLocations = _uiState.value.locations.map { loc ->
                            loc.copy(isSelected = loc.id == location.id)
                        }
                        _uiState.value = _uiState.value.copy(
                            locations = updatedLocations,
                            currentSelectedLocation = location
                        )
                    }
                },
                onFailure = { e ->
                    _uiState.value = _uiState.value.copy(
                        error = e.message ?: "위치 변경에 실패했습니다"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

// ==================== UI Components ====================
@Composable
fun LocationSelectionDialog(
    viewModel: LocationViewModel = androidx.compose.runtime.remember { LocationViewModel() },
    onLocationSelected: (Location) -> Unit,
    onRegionSettingClick: () -> Unit = {}, // 지역 설정 화면으로 이동하는 콜백 추가
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val backgroundAlpha = 0.3f

    // 에러 처리
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            // 에러 메시지 표시 후 자동으로 클리어
            kotlinx.coroutines.delay(3000)
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 배경 오버레이
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha))
                .clickable { onDismiss() }
        )

        // 다이얼로그 카드
        Card(
            modifier = Modifier
                .width(200.dp)
                .wrapContentHeight()
                .padding(start = 20.dp, top = 80.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(0.dp)
            ) {
                when {
                    uiState.isLoading -> {
                        // 로딩 상태
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color(0xFF2FB475)
                            )
                        }
                    }

                    uiState.error != null -> {
                        // 에러 상태
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.error!!,
                                fontSize = 12.sp,
                                color = Color.Red
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { viewModel.loadUserLocations() }
                            ) {
                                Text("다시 시도", fontSize = 12.sp)
                            }
                        }
                    }

                    else -> {
                        // 정상 상태 - 위치 목록 표시
                        uiState.locations.forEach { location ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectLocation(location)
                                        onLocationSelected(location)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = location.name,
                                    fontSize = 16.sp,
                                    color = if (location.isSelected) Color.Black else Color.Gray,
                                    fontWeight = if (location.isSelected) FontWeight.ExtraBold else FontWeight.Normal
                                )
                            }

                            if (location != uiState.locations.last()) {
                                Divider(
                                    color = Color.LightGray.copy(alpha = 0.5f),
                                    thickness = 0.5.dp
                                )
                            }
                        }

                        // 재설정하기 구분선 추가
                        if (uiState.locations.isNotEmpty()) {
                            Divider(
                                color = Color.LightGray.copy(alpha = 0.5f),
                                thickness = 0.5.dp
                            )
                        }

                        // 재설정하기 항목 (별도 처리)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDismiss() // 다이얼로그 닫기
                                    onRegionSettingClick() // 지역 설정 화면으로 이동
                                }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "재설정하기",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== Previews ====================
@Preview(showBackground = true)
@Composable
fun LocationSelectionDialogPreview() {
    MaterialTheme {
        LocationSelectionDialog(
            onLocationSelected = {},
            onRegionSettingClick = {},
            onDismiss = {}
        )
    }
}