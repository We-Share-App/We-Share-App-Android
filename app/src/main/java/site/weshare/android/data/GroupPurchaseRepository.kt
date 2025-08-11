package site.weshare.android.data

import site.weshare.android.data.remote.api.ApiClient
import site.weshare.android.data.remote.api.GroupPurchaseApi
import site.weshare.android.model.GroupPurchasePost
import site.weshare.android.model.GroupPurchasePostDto

class GroupPurchaseRepository(private val groupPurchaseApi: GroupPurchaseApi = ApiClient.groupPurchaseApi) {

    suspend fun getGroupPurchasePosts(accessToken: String, locationId: Int, lastPostId: Int?): List<GroupPurchasePost> {
        val response = groupPurchaseApi.getGroupPurchasePosts(accessToken, locationId, lastPostId = lastPostId)
        if (response.isSuccessful) {
            return response.body()?.groupPurchasePostDtoList?.map { it.toGroupPurchasePost() } ?: emptyList()
        } else {
            throw Exception("Failed to fetch group purchase posts")
        }
    }
}

private fun GroupPurchasePostDto.toGroupPurchasePost(): GroupPurchasePost {
    return GroupPurchasePost(
        id = this.id.toLong(),
        title = this.itemName,
        imageUrl = this.imageUrlList.firstOrNull() ?: "",
        location = "", // TODO: Get location from somewhere
        participants = 0, // TODO: Get participants from somewhere
        total = 0 // TODO: Get total from somewhere
    )
}
