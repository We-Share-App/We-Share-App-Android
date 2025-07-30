package site.weshare.android.model

data class NomineeItem(
    val id: String,
    val detail: ExchangeItemDetail,
    val exchangeStatus: String,
    val likesCount: Int,
    val postingDate: String? = null // nullable String으로 처리
)
