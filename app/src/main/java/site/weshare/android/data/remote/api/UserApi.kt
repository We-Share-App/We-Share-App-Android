package site.weshare.android.data.remote.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response
import site.weshare.android.data.remote.api.model.EmailVerifyRequest
import site.weshare.android.data.remote.api.model.EmailRequest  // 👈 이거 새로 추가해야 함

interface UserApi {
    @POST("/user/email/certification")
    suspend fun requestEmailCode(
        @Body request: EmailRequest  // 👈 수정 포인트
    ): Response<Unit>

    @POST("/user/email/certification/verify")
    suspend fun verifyEmailCode(
        @Body request: EmailVerifyRequest  // ✅ Authorization 제거
    ): Response<Unit>
}