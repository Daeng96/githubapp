package com.arteneta.githubapp.view.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arteneta.githubapp.R
import com.arteneta.githubapp.model.ItemUser
import com.arteneta.githubapp.model.User
import com.arteneta.githubapp.view.tools.ErrorScreen
import com.arteneta.githubapp.view.tools.ListItem
import com.arteneta.githubapp.view.tools.ProgressIndicator
import com.arteneta.githubapp.viewmodel.RequestResult

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
				ErrorScreen(searchResult.message, modifier = Modifier
					.align(Alignment.Center)
					.padding(16.dp))
			}
			is RequestResult.Success -> {
				UserListContent(
					itemsUser = searchResult.data.items ?: emptyList(),
					itemOnClick = itemOnClick
				)
			}
			else -> {
				val infiniteTransition = rememberInfiniteTransition()
				val alphaAnimation by infiniteTransition.animateFloat(
					initialValue = 0f,
					targetValue = 1f,
					animationSpec = infiniteRepeatable(tween(5000), repeatMode = RepeatMode.Reverse)
				)
				Image(
					painter = painterResource(id = R.drawable.github_logo),
					modifier = Modifier
						.align(Alignment.Center)
						.size(100.dp)
						.alpha(alphaAnimation)
						.rotate(alphaAnimation.times(360f)),
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
		contentPadding = PaddingValues(all = 16.dp),
		verticalArrangement = Arrangement.spacedBy(4.dp),
		content = {
			items(items = itemsUser, key = { it.login }) { user ->
				ListItem(
					login = user.login,
					avatarUrl = user.avatarUrl,
					subTitle = "Score: ${user.score}",
					itemOnClick = itemOnClick
				)
			}
		})
}