package site.weshare.android.presentation.gonggu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.weshare.android.data.GroupPurchaseRepository
import site.weshare.android.model.GroupPurchasePost

sealed class GroupPurchaseListUiState {
    object Loading : GroupPurchaseListUiState()
    data class Success(val posts: List<GroupPurchasePost>) : GroupPurchaseListUiState()
    data class Error(val message: String) : GroupPurchaseListUiState()
    object Empty : GroupPurchaseListUiState()
}

class GroupPurchaseListViewModel : ViewModel() {

    private val repository = GroupPurchaseRepository()

    private val _uiState = MutableStateFlow<GroupPurchaseListUiState>(GroupPurchaseListUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var lastPostId: Int? = null
    private var isLoading = false

    // TODO: Get these values from a reliable source
    private val accessToken = "your_access_token"
    private val locationId = 1

    init {
        fetchGroupPurchasePosts()
    }

    fun fetchGroupPurchasePosts() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            try {
                val newPosts = repository.getGroupPurchasePosts(accessToken, locationId, lastPostId)
                if (newPosts.isNotEmpty()) {
                    lastPostId = newPosts.last().id.toInt()
                    val currentPosts = if (_uiState.value is GroupPurchaseListUiState.Success) {
                        (_uiState.value as GroupPurchaseListUiState.Success).posts
                    } else {
                        emptyList()
                    }
                    _uiState.value = GroupPurchaseListUiState.Success(currentPosts + newPosts)
                } else {
                    if (_uiState.value is GroupPurchaseListUiState.Loading) {
                        _uiState.value = GroupPurchaseListUiState.Empty
                    }
                }
            } catch (e: Exception) {
                _uiState.value = GroupPurchaseListUiState.Error(e.message ?: "Unknown error")
            }
            isLoading = false
        }
    }
}
