package site.weshare.android.data

import site.weshare.android.data.remote.api.ApiClient
import site.weshare.android.data.remote.api.ExchangeApi
import site.weshare.android.model.ExchangePost
import site.weshare.android.model.ExchangePostDto

class ExchangeRepository(private val exchangeApi: ExchangeApi = ApiClient.exchangeApi) {

    suspend fun getExchangePosts(accessToken: String, locationId: Int, lastPostId: Int?): List<ExchangePost> {
        val response = exchangeApi.getExchangePosts(accessToken, locationId, lastPostId = lastPostId)
        if (response.isSuccessful) {
            return response.body()?.exchangePostDtoList?.map { it.toExchangePost() } ?: emptyList()
        } else {
            throw Exception("Failed to fetch exchange posts")
        }
    }
}

private fun ExchangePostDto.toExchangePost(): ExchangePost {
    return ExchangePost(
        id = this.id.toLong(),
        title = this.itemName,
        imageUrl = this.imageUrlList.firstOrNull() ?: "",
        location = "", // TODO: Get location from somewhere
        likes = this.likes,
        comments = 0 // TODO: Get comments from somewhere
    )
}
