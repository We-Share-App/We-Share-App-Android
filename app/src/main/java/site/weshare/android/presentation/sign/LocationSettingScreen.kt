package site.weshare.android.presentation.location

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import jxl.Workbook
import kotlinx.coroutines.launch
import java.util.*

private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

@Composable
fun LocationSettingScreen(
    onLocationSet: (List<String>) -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val fusedLocationSource = remember { FusedLocationSource(context as Activity, LOCATION_PERMISSION_REQUEST_CODE) }
    val coroutineScope = rememberCoroutineScope()

    var marker by remember { mutableStateOf<Marker?>(null) }
    var nearbyCities by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedCities by remember { mutableStateOf<List<String>>(emptyList()) }
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

        if (selectedCities.isNotEmpty()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                selectedCities.forEach { city ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE0F2F1), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(city)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        AndroidView(factory = {
            android.widget.Button(it).apply {
                text = "지역 설정 완료"
                setOnClickListener {
                    if (selectedCities.isNotEmpty()) {
                        onLocationSet(selectedCities)
                    } else {
                        Toast.makeText(context, "지역을 선택해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }, modifier = Modifier.fillMaxWidth().padding(16.dp))

        if (showBottomSheet) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    nearbyCities.forEach { city ->
                        Text(
                            text = city,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clickable {
                                    if (!selectedCities.contains(city)) {
                                        if (selectedCities.size < 2) {
                                            selectedCities = selectedCities + city
                                            showBottomSheet = false
                                        } else {
                                            Toast.makeText(context, "최대 2개까지 선택 가능합니다", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
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
        "경기도" -> workbook.getSheet(2)
        "부산광역시" -> workbook.getSheet(8)
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


//package site.weshare.android.presentation.location
//
//import android.app.Activity
//import android.content.Context
//import android.location.Geocoder
//import android.location.Location
//import android.widget.Toast
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import com.naver.maps.geometry.LatLng
//import com.naver.maps.map.*
//import com.naver.maps.map.overlay.Marker
//import com.naver.maps.map.util.FusedLocationSource
//import jxl.Workbook
//import kotlinx.coroutines.launch
//import java.util.*
//
//private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
//
//@Composable
//fun LocationSettingScreen(
//    onLocationSet: () -> Unit
//) {
//    val context = LocalContext.current
//    val mapView = remember { MapView(context) }
//    val fusedLocationSource = remember { FusedLocationSource(context as Activity, LOCATION_PERMISSION_REQUEST_CODE) }
//    val coroutineScope = rememberCoroutineScope()
//
//    var marker by remember { mutableStateOf<Marker?>(null) }
//    var cityList by remember { mutableStateOf<List<String>>(emptyList()) }
//    var selectedCity by remember { mutableStateOf<String?>(null) }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        AndroidView(
//            factory = {
//                mapView.getMapAsync { naverMap ->
//                    naverMap.locationSource = fusedLocationSource
//                    naverMap.uiSettings.isLocationButtonEnabled = true
//                    naverMap.locationTrackingMode = LocationTrackingMode.Follow
//
//                    naverMap.setOnMapClickListener { _, coord ->
//                        marker?.map = null
//                        marker = Marker().apply {
//                            position = coord
//                            map = naverMap
//                        }
//
//                        coroutineScope.launch {
//                            val geocoder = Geocoder(context, Locale.KOREAN)
//                            val address = geocoder.getFromLocation(coord.latitude, coord.longitude, 1)?.firstOrNull()
//                            val local = address?.subLocality ?: address?.locality
//                            val admin = address?.adminArea
//
//                            if (admin != null && local != null) {
//                                val nearCities = readNearbyCities(context, admin, local, coord.latitude, coord.longitude)
//                                if (nearCities.isNotEmpty()) {
//                                    cityList = nearCities
//                                } else {
//                                    Toast.makeText(context, "인근 지역을 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
//                                }
//                            } else {
//                                Toast.makeText(context, "지역 정보를 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//                mapView
//            },
//            modifier = Modifier.weight(1f)
//        )
//
//        if (cityList.isNotEmpty()) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("인근 지역 선택:")
//                cityList.forEach { city ->
//                    Text(
//                        text = city,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 8.dp)
//                            .clickable {
//                                selectedCity = city
//                                Toast.makeText(context, "선택된 지역: $city", Toast.LENGTH_SHORT).show()
//                            }
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        AndroidView(factory = {
//            android.widget.Button(it).apply {
//                text = "위치 설정 완료"
//                setOnClickListener {
//                    if (selectedCity != null) {
//                        onLocationSet()
//                    } else {
//                        Toast.makeText(context, "지역을 선택해주세요", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }, modifier = Modifier.fillMaxWidth().padding(16.dp))
//    }
//}
//
//private fun readNearbyCities(
//    context: Context,
//    adminArea: String,
//    localName: String,
//    lat: Double,
//    lon: Double
//): List<String> {
//    val result = mutableListOf<String>()
//    val file = context.assets.open("coordinate.xls")
//    val workbook = Workbook.getWorkbook(file)
//
//    val sheet = when (adminArea) {
//        "서울특별시" -> workbook.getSheet(0)
//        "경기도" -> workbook.getSheet(2)
//        "부산광역시" -> workbook.getSheet(8)
//        else -> null
//    } ?: return emptyList()
//
//    for (row in 1 until sheet.rows) {
//        val rowLocal = sheet.getCell(1, row).contents
//        if (rowLocal == localName) {
//            val x = sheet.getCell(5, row).contents.toDoubleOrNull()
//            val y = sheet.getCell(6, row).contents.toDoubleOrNull()
//            if (x != null && y != null) {
//                val distance = calculateDistance(lat, lon, x, y)
//                if (distance < 10000) {
//                    val city = sheet.getCell(2, row).contents
//                    result.add("$localName $city")
//                }
//            }
//        }
//    }
//
//    return result
//}
//
//private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
//    val locA = Location("A").apply {
//        latitude = lat1
//        longitude = lon1
//    }
//    val locB = Location("B").apply {
//        latitude = lat2
//        longitude = lon2
//    }
//    return locA.distanceTo(locB).toDouble()
//}
