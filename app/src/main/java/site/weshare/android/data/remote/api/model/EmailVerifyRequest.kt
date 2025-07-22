package site.weshare.android.data.remote.api.model

import com.google.gson.annotations.SerializedName

data class EmailVerifyRequest(
    @SerializedName("email") val email: String,
    @SerializedName("key")   val key: String
)