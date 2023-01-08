package com.dicoding.submission.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.dicoding.submission.view.tools.GithubTabRowDefault
import com.dicoding.submission.view.tools.GithubTabRowDefault.pagerM3TabIndicatorOffset
import com.dicoding.submission.view.tools.ListItem
import com.dicoding.submission.view.tools.ProgressIndicator
import com.dicoding.submission.viewmodel.DetailUsersViewModel
import com.dicoding.submission.viewmodel.FollowViewModel
import com.dicoding.submission.viewmodel.RequestResult
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.launch


@Composable
fun DetailUserScreen(
	login: String,
	navigateToDetail: (String) -> Unit
) {
	val detailUsersViewModel: DetailUsersViewModel = hiltViewModel()

	LaunchedEffect(key1 = Unit) {
		detailUsersViewModel.setDetailUsers(login)
	}
	val isExists by detailUsersViewModel.isExists(login).observeAsState(false)

	val userDetailResult =
		detailUsersViewModel.detailUser.observeAsState(RequestResult.Progress).value

	Box(modifier = Modifier.fillMaxSize()) {
		when (userDetailResult) {
			is RequestResult.Progress -> {
				ProgressIndicator()
			}
			is RequestResult.Success -> {
				DetailUserContent(user = userDetailResult.data, navigateToDetail = navigateToDetail)
				ExtendedFloatingActionButton(
					onClick = {
						if (isExists) {
							detailUsersViewModel.unFavorite(userDetailResult.data.asFavorites())
						} else {
							detailUsersViewModel.insertAll(userDetailResult.data.asFavorites())
						}
					},
					modifier = Modifier
						.align(Alignment.BottomEnd)
						.padding(16.dp)
				) {

					Icon(
						imageVector = if (!isExists) Icons.Outlined.FavoriteBorder else Icons.Default.Favorite,
						contentDescription = null
					)
					Text(text = stringResource(id = R.string.btn_favorited))
				}
			}
			is RequestResult.Error -> {
				ErrorScreen(userDetailResult.message)
			}
		}
	}
}

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
private fun DetailUserContent(
	user: DetailUser,
	navigateToDetail: (String) -> Unit
) {
	val followViewModel: FollowViewModel = hiltViewModel()
	LaunchedEffect(key1 = Unit) {
		followViewModel.setFollowing(user.login)
		followViewModel.setFollower(user.login)
	}
	val pagerState = rememberPagerState()
	val tabTitle =
		listOf(
			stringResource(id = R.string.follower),
			stringResource(id = R.string.following)
		)

	val coroutineScope = rememberCoroutineScope()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = 16.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp)
				.verticalScroll(ScrollState(0)),
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Column(
				modifier = Modifier.fillMaxWidth(0.3f),
				verticalArrangement = Arrangement.spacedBy(4.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				AsyncImage(model = user.avatarUrl, contentDescription = user.login)
				Text(text = user.name ?: "unknown")
			}

			Column(modifier = Modifier.fillMaxWidth(0.7f)) {
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

				FlowRow(modifier = Modifier.fillMaxWidth()) {
					RightBottomPanel(
						modifier = Modifier.fillMaxWidth(),
						title = stringResource(id = R.string.follower),
						subtitle = "${user.followers}"
					)
					RightBottomPanel(
						modifier = Modifier.fillMaxWidth(),
						title = stringResource(id = R.string.following),
						subtitle = "${user.following}"
					)
					RightBottomPanel(
						modifier = Modifier.fillMaxWidth(),
						title = stringResource(id = R.string.repo),
						subtitle = "${user.publicRepos}"
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

		TabRow(
			selectedTabIndex = pagerState.currentPage,
			indicator = { pos ->
				GithubTabRowDefault.Indicator(
					modifier = Modifier.pagerM3TabIndicatorOffset(pagerState = pagerState, pos),
					height = 2.dp
				)
			},
			containerColor = Color.Transparent,
			divider = {}
		) {

			tabTitle.forEachIndexed { index, title ->
				Tab(
					selected = pagerState.currentPage == index,
					onClick = {
						coroutineScope.launch {
							pagerState.scrollToPage(index)
						}
					},
					modifier = Modifier.heightIn(min = 48.dp),
					selectedContentColor = MaterialTheme.colorScheme.primary,
					unselectedContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
				) {
					Text(
						text = title.removeSuffix(":"),
						modifier = Modifier.padding(vertical = 8.dp)
					)
				}
			}
		}


		HorizontalPager(
			pageCount = 2,
			state = pagerState,
			verticalAlignment = Alignment.Top,
			modifier = Modifier.fillMaxHeight(),
			pageSpacing = 16.dp,
			contentPadding = PaddingValues(horizontal = 16.dp)
		) { page ->
			Box(modifier = Modifier.fillMaxWidth()) {
				when (page) {
					0 -> {
						val followers =
							followViewModel.followers.observeAsState(RequestResult.Progress).value
						when (followers) {
							is RequestResult.Success -> {
								val data = followers.data
								LazyColumn(
									modifier = Modifier.fillMaxSize(),
									verticalArrangement = Arrangement.spacedBy(4.dp),
									content = {
										items(items = data) { following ->
											ListItem(
												login = following.login!!,
												avatarUrl = following.avatarUrl,
												itemOnClick = { navigateToDetail(it) }
											)
										}
									})
							}
							is RequestResult.Error -> {
								ErrorScreen(followers.message)
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
								LazyColumn(
									modifier = Modifier.fillMaxSize(),
									verticalArrangement = Arrangement.spacedBy(4.dp),
									content = {
										items(items = data) { following ->
											ListItem(
												login = following.login!!,
												avatarUrl = following.avatarUrl,
												itemOnClick = { navigateToDetail(it) }
											)
										}
									})
							}
							is RequestResult.Error -> {
								ErrorScreen(following.message)
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

