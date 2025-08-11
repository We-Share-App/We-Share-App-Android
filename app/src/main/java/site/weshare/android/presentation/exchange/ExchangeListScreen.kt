package site.weshare.android.presentation.exchange

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
import site.weshare.android.model.ExchangePost

@Composable
fun ExchangeListScreen(viewModel: ExchangeListViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ExchangeListUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is ExchangeListUiState.Success -> {
            val listState = rememberLazyListState()
            LazyColumn(state = listState) {
                items(state.posts) { post ->
                    ExchangeListItem(post = post)
                }

                item {
                    LaunchedEffect(listState) {
                        if (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1) {
                            viewModel.fetchExchangePosts()
                        }
                    }
                }
            }
        }
        is ExchangeListUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message)
            }
        }
        is ExchangeListUiState.Empty -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No posts found.")
            }
        }
    }
}

@Composable
fun ExchangeListItem(post: ExchangePost) {
    // TODO: Implement the item layout
    Text(text = post.title)
}
