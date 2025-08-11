package site.weshare.android.presentation.gonggu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import site.weshare.android.model.GroupPurchasePost

@Composable
fun GroupPurchaseListScreen(viewModel: GroupPurchaseListViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is GroupPurchaseListUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is GroupPurchaseListUiState.Success -> {
            val listState = rememberLazyListState()
            LazyColumn(state = listState) {
                items(state.posts) { post ->
                    GroupPurchaseListItem(post = post)
                }

                item {
                    LaunchedEffect(listState) {
                        if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1) {
                            viewModel.fetchGroupPurchasePosts()
                        }
                    }
                }
            }
        }
        is GroupPurchaseListUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }
        is GroupPurchaseListUiState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No posts found.")
            }
        }
    }
}

@Composable
fun GroupPurchaseListItem(post: GroupPurchasePost) {
    // TODO: Implement the item layout
    Text(text = post.title)
}
