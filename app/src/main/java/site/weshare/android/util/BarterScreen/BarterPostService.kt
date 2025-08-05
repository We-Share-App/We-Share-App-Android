package site.weshare.android.util.BarterScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.weshare.android.presentation.barter.BarterPostApi
import site.weshare.android.presentation.barter.PostRequestBody

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