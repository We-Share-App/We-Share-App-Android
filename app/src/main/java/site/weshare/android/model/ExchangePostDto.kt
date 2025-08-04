package site.weshare.android.model

data class ExchangePostDto(
    val id: Int,
    val itemName: String,
    val itemCondition: String,
    val categoryName: List<String>,
    val createdAt: String,
    val likes: Int,
    val imageUrlList: List<String>,
    val isUserLiked: Boolean
)
