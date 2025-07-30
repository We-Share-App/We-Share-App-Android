package site.weshare.android.data.remote.api.model

data class NicknameAvailabilityResponse(
    val isSuccess: Boolean,
    val updatedNickname: String // 서버에서 던지는 값이 여기에 매핑됨 (형싱맞춰야하니)
)