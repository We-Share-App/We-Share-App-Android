package site.weshare.android.util.BarterScreen

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

// Uri로부터 파일 이름을 가져오는 유틸리티 함수
fun getFileName(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    result = it.getString(displayNameIndex)
                }
            }
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut!! + 1)
        }
    }
    // 파일 확장자가 없는 경우를 대비하여 기본 확장자 추가 (MIME 타입과 일치하도록)
    return result ?: "image_${System.currentTimeMillis()}.jpeg"
}

