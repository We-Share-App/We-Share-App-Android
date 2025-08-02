package site.weshare.android.model

// ExchangeItemDetail은 이미 정의되어 있다고 가정합니다.
data class PostingItem(
    val id: String,
    val detail: ExchangeItemDetail,
    val exchangeStatus: String,
    val likesCount: Int
)
