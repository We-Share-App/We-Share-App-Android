package site.weshare.android.util

import android.content.Context
import androidx.core.content.edit


fun saveAccessToken(context: Context, token: String) {
    context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        .edit() { putString("accessToken", token) }
}

fun saveRefreshToken(context: Context, token: String) {
    context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        .edit() { putString("refreshToken", token) }
}

fun getAccessToken(context: Context): String? {
    return context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        .getString("accessToken", null)
}