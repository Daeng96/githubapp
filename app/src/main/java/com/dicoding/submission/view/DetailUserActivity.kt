package com.dicoding.submission.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.dicoding.submission.R
import com.dicoding.submission.model.DetailUser
import com.dicoding.submission.theme.Typography
import com.dicoding.submission.view.tools.ErrorScreen
import com.dicoding.submission.view.tools.ListItem
import com.dicoding.submission.view.tools.ProgressIndicator
import com.dicoding.submission.viewmodel.DetailUsersViewModel
import com.dicoding.submission.viewmodel.FollowViewModel
import com.dicoding.submission.viewmodel.RequestResult
import com.dicoding.submission.viewmodel.UserDetailResult
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@Composable
fun DetailUserScreen(
	login: String
) {
	val detailUsersViewModel: DetailUsersViewModel = hiltViewModel()

	SideEffect {
		detailUsersViewModel.setDetailUsers(login)
	}

	val userDetailResult =
		detailUsersViewModel.detailUser.observeAsState(UserDetailResult.OnProgress).value

	Box(modifier = Modifier.fillMaxSize()) {
		when (userDetailResult) {
			is UserDetailResult.OnProgress -> {
				ProgressIndicator()
			}
			is UserDetailResult.OnSuccess -> {
				DetailUserContent(user = userDetailResult.user)
			}
			is UserDetailResult.OnError -> {
				ErrorScreen()
			}
		}
	}
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun DetailUserContent(
	user: DetailUser,
) {
	val followViewModel: FollowViewModel = hiltViewModel()
	SideEffect {
		followViewModel.setFollowing(user.login)
		followViewModel.setFollower(user.login)
	}
	val pagerState = rememberPagerState()
	val tabTitle = listOf(
		stringResource(id = R.string.follower),
		stringResource(id = R.string.following)
	)
	val coroutineScope = rememberCoroutineScope()

	Column(modifier = Modifier.fillMaxSize()) {
		Row(modifier = Modifier.fillMaxWidth()) {
			Column(modifier = Modifier.fillMaxWidth(0.4f)) {
				AsyncImage(model = user.avatarUrl, contentDescription = user.login)
				Text(text = user.name ?: "unknown")
			}

			Column(modifier = Modifier.fillMaxWidth(0.6f)) {
				RightPanel(
					modifier = Modifier.fillMaxWidth(),
					icon = Icons.Default.Person,
					title = user.login
				)

				RightPanel(
					modifier = Modifier.fillMaxWidth(),
					icon = Icons.Default.LocationOn,
					title = user.location ?: "unknown"
				)

				FlowRow(modifier = Modifier.fillMaxWidth(), mainAxisSpacing = 8.dp) {
					RightBottomPanel(
						modifier = Modifier.fillMaxWidth(),
						title = stringResource(id = R.string.follower),
						subtitle = user.followers.toString()
					)
					RightBottomPanel(
						modifier = Modifier.fillMaxWidth(),
						title = stringResource(id = R.string.following),
						subtitle = user.following.toString()
					)
					RightBottomPanel(
						modifier = Modifier.fillMaxWidth(),
						title = stringResource(id = R.string.repo),
						subtitle = user.publicRepos.toString()
					)
				}
			}
		}
		Text(
			text = user.company ?: "unknown",
			modifier = Modifier.fillMaxWidth(),
			textAlign = TextAlign.Center,
			style = Typography.titleLarge
		)

		TabRow(selectedTabIndex = pagerState.currentPage) {
			tabTitle.forEachIndexed { index, title ->
				Tab(selected = pagerState.currentPage == index, onClick = {
					coroutineScope.launch {
						pagerState.scrollToPage(index)
					}
				}) {
					Text(text = title)
				}
			}
		}

		Box(modifier = Modifier.fillMaxWidth()) {
			HorizontalPager(
				count = 2,
				state = pagerState
			) { page ->
				when (page) {
					0 -> {
						val followers =
							followViewModel.followers.observeAsState(RequestResult.Progress).value
						when (followers) {
							is RequestResult.Success -> {
								val data = followers.data
								LazyColumn(content = {
									items(items = data) { following ->
										ListItem(
											login = following.login!!,
											avatarUrl = following.avatarUrl
										)
									}
								})
							}
							is RequestResult.Error -> {
								ErrorScreen()
							}
							is RequestResult.Progress -> {
								RequestResult.Progress
							}
						}
					}
					1 -> {
						val following =
							followViewModel.following.observeAsState(RequestResult.Progress).value
						when (following) {
							is RequestResult.Success -> {
								val data = following.data
								LazyColumn(content = {
									items(items = data) { following ->
										ListItem(
											login = following.login!!,
											avatarUrl = following.avatarUrl
										)
									}
								})
							}
							is RequestResult.Error -> {
								ErrorScreen()
							}
							is RequestResult.Progress -> {
								RequestResult.Progress
							}
						}
					}
				}
			}
		}
	}
}

@Composable
private fun RightPanel(
	modifier: Modifier,
	icon: ImageVector,
	title: String
) {
	Row(
		modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(imageVector = icon, contentDescription = title)
		Text(text = title, maxLines = 1)
	}
}

@Composable
private fun RightBottomPanel(
	modifier: Modifier,
	title: String,
	subtitle: String
) {
	Row(
		modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(text = title, maxLines = 1)
		Text(text = subtitle, maxLines = 1)
	}
}

