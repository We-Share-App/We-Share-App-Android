package site.weshare.android.presentation.sign

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import site.weshare.android.R

@Composable
fun TermsAgreementScreen() {
    var allChecked by remember { mutableStateOf(false) }
    var termsChecked by remember { mutableStateOf(false) }
    var privacyCollectionChecked by remember { mutableStateOf(false) }
    var locationServiceChecked by remember { mutableStateOf(false) }
    var wifiLocationChecked by remember { mutableStateOf(false) }
    var privacyProvisionChecked by remember { mutableStateOf(false) }
    var marketingChecked by remember { mutableStateOf(false) }
    var advertisingChecked by remember { mutableStateOf(false) }

    // 전체 동의 상태 업데이트
    LaunchedEffect(
        termsChecked, privacyCollectionChecked, locationServiceChecked,
        wifiLocationChecked, privacyProvisionChecked, marketingChecked, advertisingChecked
    ) {
        allChecked = termsChecked && privacyCollectionChecked && locationServiceChecked &&
                wifiLocationChecked && privacyProvisionChecked && marketingChecked && advertisingChecked
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0E0E0))
    ) {
        // 상단 상태바 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "9:30",
                fontSize = 14.sp,
                color = Color.Black
            )

            Row {
                // WiFi, 배터리, 신호 아이콘들 (간단한 도형으로 표현)
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Black, CircleShape)
                )
            }
        }

        // 약관 동의 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // 제목
                Text(
                    text = "약관에 동의해주세요",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 15.dp)
                )

                // 전체 동의 체크박스
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            allChecked = !allChecked
                            termsChecked = allChecked
                            privacyCollectionChecked = allChecked
                            locationServiceChecked = allChecked
                            wifiLocationChecked = allChecked
                            privacyProvisionChecked = allChecked
                            marketingChecked = allChecked
                            advertisingChecked = allChecked
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.checkbox_outline), // 체크 아이콘
                        contentDescription = "전체 동의",
                        modifier = Modifier
                            .size(35.dp)
                            .padding(end = 5.dp),
                        colorFilter = if (allChecked) ColorFilter.tint(Color(0xff2FB475)) else ColorFilter.tint(Color(0xffC5C5C5))
                    )
                    Text(
                        text = "전체 동의",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                // 구분선
                Divider(
                    color = Color(0xffC5C5C5),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // 개별 약관 항목들
                TermsItem(
                    text = "까치 이용약관 (필수)",
                    isChecked = termsChecked,
                    onCheckedChange = { termsChecked = it }
                )

                TermsItem(
                    text = "개인정보 수집 이용 동의 (필수)",
                    isChecked = privacyCollectionChecked,
                    onCheckedChange = { privacyCollectionChecked = it }
                )

                TermsItem(
                    text = "휴대폰 본인확인서비스 (필수)",
                    isChecked = locationServiceChecked,
                    onCheckedChange = { locationServiceChecked = it }
                )

                TermsItem(
                    text = "위치정보 이용약관 동의 (필수)",
                    isChecked = wifiLocationChecked,
                    onCheckedChange = { wifiLocationChecked = it }
                )

                TermsItem(
                    text = "개인정보 수집 이용 동의 (선택)",
                    isChecked = privacyProvisionChecked,
                    onCheckedChange = { privacyProvisionChecked = it }
                )

                TermsItem(
                    text = "마케팅 수신 동의 (선택)",
                    isChecked = marketingChecked,
                    onCheckedChange = { marketingChecked = it }
                )

                TermsItem(
                    text = "개인정보 광고활용 동의 (선택)",
                    isChecked = advertisingChecked,
                    onCheckedChange = { advertisingChecked = it }
                )
            }
        }
    }
}

@Composable
fun TermsItem(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.check_bold), // 체크 아이콘
            contentDescription = "체크",
            modifier = Modifier
                .size(32.dp)
                .padding(start = 5.dp, end = 8.dp),
            colorFilter = if (isChecked) ColorFilter.tint(Color(0xff2FB475)) else ColorFilter.tint(Color(0xffC5C5C5))
        )

        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xff706E6E),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Image(
            painter = painterResource(id = R.drawable.chevron_down), // 화살표 아이콘
            contentDescription = "상세보기",
            modifier = Modifier.size(25.dp),
            colorFilter = ColorFilter.tint(Color(0xff706E6E))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAgreementScreenPreview() {
    TermsAgreementScreen()
}