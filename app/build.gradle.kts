plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "site.weshare.android"
    compileSdk = 35

    defaultConfig {
        applicationId = "site.weshare.android"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.browser:browser:1.6.0")
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.animation.core.lint)  // 외부 브라우저 띄우기 위함
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)



    //네이버 지도 api
    implementation("com.naver.maps:map-sdk:3.22.1")
    //jxl 엑셀 변환
    implementation("net.sourceforge.jexcelapi:jxl:2.6.12")

    implementation("com.google.android.gms:play-services-location:21.0.1")


    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON 파싱을 위해 Gson 컨버터 사용

    // OkHttp (Retrofit의 내부 HTTP 클라이언트)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // API 요청/응답 로깅 (디버깅용)

    // Gson (JSON 직렬화/역직렬화)
    implementation("com.google.code.gson:gson:2.10.1")

    // Jetpack Compose ViewModel 
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0-beta01") // 또는 최신 버전

    // Coil (이미지 로딩 라이브러리)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
}