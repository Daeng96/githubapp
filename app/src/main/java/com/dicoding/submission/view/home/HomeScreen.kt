package com.dicoding.submission.view.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dicoding.submission.R
import com.dicoding.submission.model.ItemUser
import com.dicoding.submission.model.User
import com.dicoding.submission.view.tools.ErrorScreen
import com.dicoding.submission.view.tools.ListItem
import com.dicoding.submission.view.tools.ProgressIndicator
import com.dicoding.submission.viewmodel.RequestResult

@Composable
fun HomeScreen(
	searchResult: RequestResult<User>?,
	onBackPressed: () -> Unit,
	itemOnClick: (String) -> Unit
) {
	Box(modifier = Modifier.fillMaxSize()) {
		when (searchResult) {
			is RequestResult.Progress -> {
				ProgressIndicator()
			}
			is RequestResult.Error -> {
				ErrorScreen(searchResult.message)
			}
			is RequestResult.Success -> {
				UserListContent(
					itemsUser = searchResult.data.items ?: emptyList(),
					itemOnClick = itemOnClick
				)
			}
			else -> {
				Image(
					painter = painterResource(id = R.drawable.github_logo),
					modifier = Modifier
						.align(Alignment.Center)
						.size(100.dp),
					contentDescription = "github logo"
				)
			}
		}
	}

	BackHandler(true) { onBackPressed() }
}

@Composable
private fun UserListContent(
	itemsUser: List<ItemUser>,
	itemOnClick: (String) -> Unit
) {
	LazyColumn(
		state = rememberLazyListState(),
		contentPadding = PaddingValues(horizontal = 16.dp),
		verticalArrangement = Arrangement.spacedBy(4.dp),
		content = {
			items(items = itemsUser, key = { it.login }) { user ->
				ListItem(
					login = user.login,
					avatarUrl = user.avatarUrl,
					userScore = user.score,
					itemOnClick = itemOnClick
				)
			}
		})
}