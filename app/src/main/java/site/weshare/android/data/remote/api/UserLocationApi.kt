package site.weshare.android.data.remote.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import site.weshare.android.data.remote.model.UserLocationRequest
import site.weshare.android.data.remote.model.UserLocationResponse

interface UserLocationApi {
    @POST("users/locations")
    suspend fun registerUserLocation(
        @Header("access") accessToken: String,
        @Body request: UserLocationRequest
    ): Response<UserLocationResponse>
}