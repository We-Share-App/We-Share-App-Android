package site.weshare.android.model

data class ItemExchange(
    val id: String,
    val status: ItemExchangeStatus,
    val myItem: ExchangeItemDetail,
    val targetItem: ExchangeItemDetail,
    val exchangeDate: String? = null
)