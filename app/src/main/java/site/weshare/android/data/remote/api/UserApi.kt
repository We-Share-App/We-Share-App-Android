package site.weshare.android.data.remote.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Query
import site.weshare.android.data.remote.api.model.EmailVerifyRequest
import site.weshare.android.data.remote.api.model.EmailRequest  // 👈 이거 새로 추가해야 함
import site.weshare.android.data.remote.api.model.NicknameAvailabilityResponse
import site.weshare.android.data.remote.api.model.NicknameCheckResponse
import site.weshare.android.data.remote.api.model.NicknameUpdateResponse

interface UserApi {
    @POST("/user/email/certification")
    suspend fun requestEmailCode(
        @Body request: EmailRequest  // 👈 수정 포인트
    ): Response<Unit>

    @POST("/user/email/certification/verify")
    suspend fun verifyEmailCode(
        @Body request: EmailVerifyRequest  // ✅ Authorization 제거
    ): Response<Unit>


    @POST("/users/nicknames/available")
    suspend fun checkNicknameAvailability(
        @Query("nickname") nickname: String,
        @Header("access") accessToken: String  // ✅ 토큰 다시 추가
    ): Response<NicknameCheckResponse>

    @POST("/users/nicknames")
    suspend fun updateNickname(
        @Query("nickname") nickname: String,
        @Header("access") accessToken: String
    ): Response<NicknameUpdateResponse>
}


