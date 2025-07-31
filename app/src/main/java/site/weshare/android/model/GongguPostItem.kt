package site.weshare.android.model

data class GongguPostItem(
    val id: String,
    val imageUrl: Int, // R.drawable 리소스 ID를 사용하므로 Int 타입 유지
    val name: String,
    val totalPurchaseCount: Int,
    val participantCount: Int,
    val myPurchaseCount: Int,
    val status: String,
    val date: String,
    val displayCountOverlay: String?
)
