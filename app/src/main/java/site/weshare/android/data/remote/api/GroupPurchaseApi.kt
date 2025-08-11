package site.weshare.android.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import site.weshare.android.model.GroupPurchasePostDto

interface GroupPurchaseApi {
    @GET("/group-purchases")
    suspend fun getGroupPurchasePosts(
        @Header("access") accessToken: String,
        @Query("locationId") locationId: Int,
        @Query("categoryId") categoryId: Int? = null,
        @Query("lastPostId") lastPostId: Int? = null
    ): Response<GroupPurchasePostResponse>
}

data class GroupPurchasePostResponse(
    val totalPostCount: Int,
    val groupPurchasePostDtoList: List<GroupPurchasePostDto>,
    val lastPostId: Int
)
