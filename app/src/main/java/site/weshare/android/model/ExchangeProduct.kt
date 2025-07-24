package site.weshare.android.model

data class ExchangeProduct(
    val id: String,
    val imageUrl: String, // R.drawable.xxx.toString() 또는 실제 URL
    val name: String,
    val category: String,
    val exchangeCondition: String
)