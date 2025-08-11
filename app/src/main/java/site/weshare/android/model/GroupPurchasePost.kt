package site.weshare.android.model

data class GroupPurchasePost(
    val id: Long,
    val title: String,
    val imageUrl: String,
    val location: String,
    val participants: Int,
    val total: Int
)
