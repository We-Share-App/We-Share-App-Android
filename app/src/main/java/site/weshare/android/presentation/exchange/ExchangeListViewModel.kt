package site.weshare.android.presentation.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.weshare.android.data.ExchangeRepository
import site.weshare.android.model.ExchangePost

sealed class ExchangeListUiState {
    object Loading : ExchangeListUiState()
    data class Success(val posts: List<ExchangePost>) : ExchangeListUiState()
    data class Error(val message: String) : ExchangeListUiState()
    object Empty : ExchangeListUiState()
}

class ExchangeListViewModel : ViewModel() {

    private val repository = ExchangeRepository()

    private val _uiState = MutableStateFlow<ExchangeListUiState>(ExchangeListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var lastPostId: Int? = null
    private var isLoading = false

    // TODO: Get these values from a reliable source
    private val accessToken = "your_access_token"
    private val locationId = 1

    init {
        fetchExchangePosts()
    }

    fun fetchExchangePosts() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            try {
                val newPosts = repository.getExchangePosts(accessToken, locationId, lastPostId)
                if (newPosts.isNotEmpty()) {
                    lastPostId = newPosts.last().id.toInt()
                    val currentPosts = if (_uiState.value is ExchangeListUiState.Success) {
                        (_uiState.value as ExchangeListUiState.Success).posts
                    } else {
                        emptyList()
                    }
                    _uiState.value = ExchangeListUiState.Success(currentPosts + newPosts)
                } else {
                    if (_uiState.value is ExchangeListUiState.Loading) {
                        _uiState.value = ExchangeListUiState.Empty
                    }
                }
            } catch (e: Exception) {
                _uiState.value = ExchangeListUiState.Error(e.message ?: "Unknown error")
            }
            isLoading = false
        }
    }
}
