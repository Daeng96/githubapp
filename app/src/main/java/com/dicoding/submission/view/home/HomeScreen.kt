package com.dicoding.submission.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.dicoding.submission.R
import com.dicoding.submission.model.ItemUser
import com.dicoding.submission.model.User
import com.dicoding.submission.view.tools.ErrorScreen
import com.dicoding.submission.view.tools.ListItem
import com.dicoding.submission.view.tools.ProgressIndicator
import com.dicoding.submission.viewmodel.RequestResult

@Composable
fun HomeScreen(
	searchResult: RequestResult<User>?
) {
	Box(modifier = Modifier.fillMaxSize()) {
		when (searchResult) {
			is RequestResult.Progress -> {
				ProgressIndicator()
			}
			is RequestResult.Error -> {
				ErrorScreen()
			}
			is RequestResult.Success -> {
				UserListContent(itemsUser = searchResult.data.items ?: emptyList())
			}
			else -> {
				Image(
					painter = painterResource(id = R.drawable.github_logo),
					modifier = Modifier.align(Alignment.Center),
					contentDescription = "github logo"
				)
			}
		}
	}
}

@Composable
private fun UserListContent(
	itemsUser: List<ItemUser>
) {
	LazyColumn(content = {
		items(items = itemsUser, key = { it.login }) { user ->
			ListItem(login = user.login, avatarUrl = user.avatarUrl, userScore = user.score)
		}
	})
}