package site.weshare.android.model

import androidx.annotation.DrawableRes

data class ExchangeItemDetail(
    @DrawableRes val imageUrl: Int,
    val name: String,
    val itemStatus: String,
    val desiredCategory: String,
    val category: String
)