package com.latihan.githubconsumerapp

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.latihan.githubconsumerapp.R
import com.latihan.githubconsumerapp.Utils.CONTENT_URI
import com.latihan.githubconsumerapp.theme.GithubMaterialTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFavoriteActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		WindowCompat.setDecorFitsSystemWindows(window, false)
		setContent {
			GithubMaterialTheme {
				GithubApp()
			}
		}
	}
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Activity.HomeScreen(
	showBottomSheet: (Long) -> Unit
) {

	val favoriteList = remember { mutableStateListOf<Favorites>() }

	LaunchedEffect(key1 = favoriteList, block = {
		val cursor = contentResolver.query(
			CONTENT_URI,
			null,
			null,
			null,
			null,
			null
		)
		favoriteList.addAll(MappingHelper.mapCursor(cursor))
	})

	val coroutineScope = rememberCoroutineScope()
	var refreshing by remember { mutableStateOf(false) }
	fun refresh() = coroutineScope.launch(Dispatchers.IO) {
		refreshing = true
		favoriteList.clear()
		val cursor = contentResolver.query(
			CONTENT_URI,
			null,
			null,
			null,
			null,
			null
		)

		favoriteList.addAll(MappingHelper.mapCursor(cursor))
		refreshing = false
	}

	val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

	Box(
		modifier = Modifier
			.fillMaxSize()
			.pullRefresh(pullRefreshState)
	) {
		LazyColumn(
			state = rememberLazyListState(),
			contentPadding = PaddingValues(horizontal = 16.dp, 8.dp),
			modifier = Modifier.fillMaxSize(),
			//verticalArrangement = Arrangement.spacedBy(8.dp),
			content = {
				items(items = favoriteList, key = { it._ID }) { user ->
					ListItem(
						login = user.login,
						avatarUrl = user.avatarUrl,
						subTitle = stringResource(id = R.string.repo).plus(" ${user.publicRepos}"),
						itemOnClick = { showBottomSheet(user._ID) },
						deleteUser = {
							val uriWithId = Uri.parse("$CONTENT_URI/${user._ID}")
							contentResolver.delete(uriWithId, null, null)
							favoriteList.removeIf { it._ID == user._ID }
						}
					)
				}
			}
		)

		PullRefreshIndicator(
			refreshing = refreshing,
			state = pullRefreshState,
			modifier = Modifier.align(
				Alignment.TopCenter
			)
		)
	}

	BackHandler(true) {
		finish()
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ListItem(
	login: String,
	avatarUrl: String?,
	subTitle: String? = null,
	itemOnClick: (String) -> Unit,
	deleteUser: () -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.animateItemPlacement()
			.clickable { itemOnClick(login) },
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.width(IntrinsicSize.Max)
		) {
			AsyncImage(
				model = avatarUrl,
				contentDescription = login,
				modifier = Modifier
					.size(50.dp)
					.clip(CircleShape)
			)
			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier
					.wrapContentSize()
			) {
				Text(text = login)
				subTitle?.let {
					Text(text = it)
				}

			}
		}

		OutlinedButton(
			onClick = deleteUser,
			colors = ButtonDefaults.outlinedButtonColors(
				contentColor = MaterialTheme.colorScheme.primary
			),
			shape = CircleShape
		) {
			Text(text = stringResource(id = R.string.btn_remove))
		}
	}
	Divider(modifier = Modifier.padding(vertical = 4.dp))
}