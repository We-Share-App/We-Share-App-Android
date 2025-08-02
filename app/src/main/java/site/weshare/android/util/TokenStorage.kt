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

fun saveSelectedRegions(context: Context, regions: List<String>) {
    context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        .edit() { putString("selectedRegions", regions.joinToString(",")) }
}

fun getSelectedRegions(context: Context): List<String> {
    val regionsString = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        .getString("selectedRegions", null)
    return regionsString?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() } ?: emptyList()
}

