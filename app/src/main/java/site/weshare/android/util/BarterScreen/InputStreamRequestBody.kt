package site.weshare.android.util.BarterScreen

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink

/**
 * InputStream으로부터 데이터를 읽어 RequestBody를 생성하는 커스텀 클래스.
 * 파일을 디스크에 임시 저장하지 않고 스트리밍 방식으로 업로드할 때 사용합니다.
 *
 * @param contentType 전송할 데이터의 MIME 타입 (예: "image/jpeg")
 * @param inputStream 데이터를 읽어올 InputStream
 * @param contentLength 데이터의 총 길이 (알 수 없는 경우 -1L)
 */
class InputStreamRequestBody(
    private val context: Context, // Context를 생성자로 받습니다.
    private val uri: Uri, // Uri를 생성자로 받습니다.
    private val contentType: MediaType?,
    private val contentLength: Long = -1L
) : RequestBody() {

    override fun contentType(): MediaType? = contentType

    override fun contentLength(): Long = contentLength

    override fun writeTo(sink: BufferedSink) {
        // writeTo가 호출될 때마다 새로운 InputStream을 엽니다.
        context.contentResolver.openInputStream(uri)?.use { stream ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesRead: Int
            while (stream.read(buffer).also { bytesRead = it } != -1) {
                sink.write(buffer, 0, bytesRead)
            }
        } ?: Log.e("InputStreamRequestBody", "Failed to open InputStream for URI: $uri during writeTo.")
    }

    companion object {
        // Uri로부터 InputStreamRequestBody를 생성하는 팩토리 메서드
        fun create(context: Context, uri: Uri): InputStreamRequestBody? {
            val contentResolver = context.contentResolver
            val resolvedType = contentResolver.getType(uri) // Get the resolved MIME type
            Log.d("InputStreamRequestBody", "Resolved MIME type for URI $uri: $resolvedType")

            // Fallback to a specific image MIME type if resolvedType is null or generic
            val contentType = resolvedType?.toMediaTypeOrNull()
                ?: "image/jpeg".toMediaTypeOrNull() // Default to JPEG if no specific type is found

            if (contentType == null) {
                Log.e("InputStreamRequestBody", "Critical: Could not determine a valid MediaType for URI: $uri. This image might fail upload.")
                return null // Or throw an exception if strict
            }

            // InputStream 자체는 여기서 열지 않고, RequestBody의 writeTo에서 열도록 변경
            val contentLength = try {
                var length = -1L
                val cursor = contentResolver.query(uri, arrayOf(OpenableColumns.SIZE), null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                        if (sizeIndex != -1) {
                            length = it.getLong(sizeIndex)
                        }
                    }
                }
                length
            } catch (e: Exception) {
                Log.e("InputStreamRequestBody", "Failed to get content length for URI: $uri, Error: ${e.localizedMessage}")
                -1L
            }

            return InputStreamRequestBody(context, uri, contentType, contentLength)
        }
    }
}