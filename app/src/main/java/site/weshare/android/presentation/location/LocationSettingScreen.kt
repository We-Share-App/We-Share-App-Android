// ✅ 파일: LocationSettingScreen.kt (최종 정리본)
package site.weshare.android.presentation.location

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import site.weshare.android.R
import site.weshare.android.data.remote.model.UserLocationRequest
import site.weshare.android.util.getAccessToken
import java.util.*
import jxl.Workbook
import site.weshare.android.util.saveRepresentativeLocationId
import site.weshare.android.util.saveSecondaryLocationId

private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

@Composable
fun LocationSettingScreen(
    onLocationSet: () -> Unit
) {
    val context = LocalContext.current
    val appContext = context.applicationContext
    val mapView = remember { MapView(context) }
    val fusedLocationSource = remember { FusedLocationSource(context as Activity, LOCATION_PERMISSION_REQUEST_CODE) }
    val coroutineScope = rememberCoroutineScope()

    var marker by remember { mutableStateOf<Marker?>(null) }
    var nearbyCities by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedType by remember { mutableStateOf<String?>(null) } // "대표" or "보조"
    var selectedCities by remember { mutableStateOf<Map<String, String>>(emptyMap()) }
    var showBottomSheet by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = {
            mapView.getMapAsync { naverMap ->
                naverMap.locationSource = fusedLocationSource
                naverMap.uiSettings.isLocationButtonEnabled = true
                naverMap.locationTrackingMode = LocationTrackingMode.Follow

                naverMap.setOnMapClickListener { _, coord ->
                    marker?.map = null
                    marker = Marker().apply {
                        position = coord
                        map = naverMap
                    }

                    coroutineScope.launch {
                        val geocoder = Geocoder(context, Locale.KOREAN)
                        val address = geocoder.getFromLocation(coord.latitude, coord.longitude, 1)?.firstOrNull()
                        val local = address?.subLocality ?: address?.locality
                        val admin = address?.adminArea

                        if (admin != null && local != null) {
                            val results = readNearbyCities(context, admin, local, coord.latitude, coord.longitude)
                            Log.d("LocationAPI", "📍 선택 위치: $admin $local, 결과: $results")
                            if (results.isNotEmpty()) {
                                nearbyCities = results
                                showBottomSheet = true
                            } else {
                                Toast.makeText(context, "인근 지역을 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "주소를 확인할 수 없습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            mapView
        }, modifier = Modifier.weight(1f))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val main = selectedCities["대표"] ?: "대표 지역 선택"
            val sub = selectedCities["보조"] ?: "보조 지역 선택"
            listOf("대표" to main, "보조" to sub).forEach { (type, label) ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .background(Color(0xFFD7F5E9), RoundedCornerShape(12.dp))
                        .clickable {
                            selectedType = type
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        Text(text = label, modifier = Modifier.padding(start = 8.dp))
                        if (selectedCities.containsKey(type)) {
                            IconButton(onClick = {
                                selectedCities = selectedCities - type
                            }) {
                                Icon(painter = painterResource(id = R.drawable.ic_arrow_back), contentDescription = "삭제")
                            }
                        }
                    }
                }
            }
        }

        AndroidView(factory = {
            android.widget.Button(it).apply {
                text = "지역 설정 완료"
                setOnClickListener {
                    val token = getAccessToken(appContext)
                    if (token == null) {
                        Toast.makeText(context, "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    selectedCities.forEach { (role, fullCity) ->
                        val parts = fullCity.split(" ")
                        if (parts.size >= 2) {
                            val request = UserLocationRequest(
                                stateName = extractState(parts[0]),
                                cityName = parts[0],
                                townName = parts[1]
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    Log.d("LocationAPI", "📤 $role 지역 요청 전송: $request")
                                    val response = ApiClient.userLocationApi.registerUserLocation(token, request)
                                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                                        val locationId = response.body()?.locationId
                                        val userLocationId = response.body()?.userLocationId
                                        Log.d("LocationAPI", "✅ 등록 성공 ($role): locationId=$locationId, userLocationId=$userLocationId")
                                        if (role == "대표") {
                                            if (locationId != null) saveRepresentativeLocationId(context, locationId)
                                            saveRepresentativeLocation(context, fullCity)
                                        } else {
                                            if (locationId != null) saveSecondaryLocationId(context, locationId)
                                            saveSecondaryLocation(context, fullCity)
                                        }
                                    } else {
                                        Log.e("LocationAPI", "❌ 응답 실패 ($role): ${response.code()} ${response.message()} ${response.errorBody()?.string()}")
                                    }
                                } catch (e: Exception) {
                                    Log.e("LocationAPI", "❗ 예외 발생 ($role): ${e.message}", e)
                                }
                            }
                        }
                    }
                    onLocationSet()
                }
            }
        }, modifier = Modifier.fillMaxWidth().padding(16.dp))

        if (showBottomSheet && selectedType != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .background(Color.White)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    nearbyCities.forEach { city ->
                        Text(
                            text = city,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clickable {
                                    selectedCities = selectedCities + (selectedType!! to city)
                                    showBottomSheet = false
                                }
                        )
                    }
                }
            }
        }
    }
}

fun saveRepresentativeLocation(context: Context, location: String) {
    context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        .edit().putString("main_location", location).apply()
}

fun saveSecondaryLocation(context: Context, location: String) {
    context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        .edit().putString("sub_location", location).apply()
}

fun getRepresentativeLocation(context: Context): String? {
    return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        .getString("main_location", null)
}

fun getSecondaryLocation(context: Context): String? {
    return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        .getString("sub_location", null)
}

private fun readNearbyCities(
    context: Context,
    adminArea: String,
    localName: String,
    lat: Double,
    lon: Double
): List<String> {
    val result = mutableListOf<String>()
    val file = context.assets.open("coordinate.xls")
    val workbook = Workbook.getWorkbook(file)
    val sheet = when (adminArea) {
        "서울특별시" -> workbook.getSheet(0)
        "강원도" -> workbook.getSheet(1)
        "경기도" -> workbook.getSheet(2)
        "경상남도" -> workbook.getSheet(3)
        "경상북도" -> workbook.getSheet(4)
        "광주광역시" -> workbook.getSheet(5)
        "대구광역시" -> workbook.getSheet(6)
        "대전광역시" -> workbook.getSheet(7)
        "부산광역시" -> workbook.getSheet(8)
        "세종특별자치시" -> workbook.getSheet(9)
        "울산광역시" -> workbook.getSheet(10)
        "전라남도" -> workbook.getSheet(11)
        "전라북도" -> workbook.getSheet(12)
        "제주특별자치도" -> workbook.getSheet(13)
        "충청남도" -> workbook.getSheet(14)
        "충청북도" -> workbook.getSheet(15)
        "인천광역시" -> workbook.getSheet(16)
        else -> null
    } ?: return emptyList()

    for (row in 1 until sheet.rows) {
        val rowLocal = sheet.getCell(1, row).contents
        if (rowLocal == localName) {
            val x = sheet.getCell(5, row).contents.toDoubleOrNull()
            val y = sheet.getCell(6, row).contents.toDoubleOrNull()
            if (x != null && y != null) {
                val distance = calculateDistance(lat, lon, x, y)
                if (distance < 10000) {
                    val city = sheet.getCell(2, row).contents
                    result.add("$localName $city")
                }
            }
        }
    }
    return result
}

private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val locA = Location("A").apply {
        latitude = lat1
        longitude = lon1
    }
    val locB = Location("B").apply {
        latitude = lat2
        longitude = lon2
    }
    return locA.distanceTo(locB).toDouble()
}

private fun extractState(cityName: String): String {
    return when {
        cityName.contains("서울") -> "서울특별시"
        cityName.contains("경기") -> "경기도"
        cityName.contains("부산") -> "부산광역시"
        cityName.contains("인천") -> "인천광역시"
        cityName.contains("대구") -> "대구광역시"
        cityName.contains("광주") -> "광주광역시"
        cityName.contains("대전") -> "대전광역시"
        cityName.contains("울산") -> "울산광역시"
        cityName.contains("세종") -> "세종특별자치시"
        cityName.contains("강원") -> "강원도"
        cityName.contains("충북") -> "충청북도"
        cityName.contains("충남") -> "충청남도"
        cityName.contains("전북") -> "전라북도"
        cityName.contains("전남") -> "전라남도"
        cityName.contains("경북") -> "경상북도"
        cityName.contains("경남") -> "경상남도"
        cityName.contains("제주") -> "제주특별자치도"
        else -> cityName
    }
} 