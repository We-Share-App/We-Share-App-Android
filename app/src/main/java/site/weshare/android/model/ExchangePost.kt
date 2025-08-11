package site.weshare.android.model

data class ExchangePost(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val location: String,
    val likes: Int,
    val comments: Int
)
