import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import site.weshare.android.data.remote.api.UserApi
import java.nio.charset.StandardCharsets

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // ✅ JSON 인코딩을 위한 Gson 설정
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .disableHtmlEscaping()
        .create()

    // ✅ UTF-8 Content-Type 헤더 강제 추가용 인터셉터
    private val utf8Interceptor = Interceptor { chain ->
        val original = chain.request()
        val requestWithUtf8 = original.newBuilder()
            .header("Content-Type", "application/json; charset=utf-8")
            .build()
        chain.proceed(requestWithUtf8)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(utf8Interceptor) // ✅ 인코딩 명시
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson)) // ✅ 커스텀 Gson
        .client(client)
        .build()

    val userApi: UserApi = retrofit.create(UserApi::class.java)
}