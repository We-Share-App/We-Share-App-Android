package site.weshare.android.model

data class GroupPurchaseProduct(
    val id: String,
    val imageUrl: String, // R.drawable.xxx.toString() 또는 실제 URL
    val name: String,
    val quantitySold: Int,
    val totalQuantity: Int,
    val remainingDays: Int
)