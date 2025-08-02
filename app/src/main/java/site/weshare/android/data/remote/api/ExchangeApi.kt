package site.weshare.android.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import site.weshare.android.model.ExchangePostDto

interface ExchangeApi {
    @GET("/exchanges")
    suspend fun getExchangePosts(
        @Header("access") accessToken: String,
        @Query("locationId") locationId: Int,
        @Query("categoryId") categoryId: Int? = null,
        @Query("itemCondition") itemCondition: String? = null,
        @Query("lastPostId") lastPostId: Int? = null
    ): Response<ExchangePostResponse>
}

data class ExchangePostResponse(
    val totalPostCount: Int,
    val exchangePostDtoList: List<ExchangePostDto>,
    val lastPostId: Int
)
