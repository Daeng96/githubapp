package com.arteneta.githubapp.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.arteneta.githubapp.R
import com.arteneta.githubapp.model.DetailUser
import com.arteneta.githubapp.theme.*
import com.arteneta.githubapp.view.tools.ErrorScreen
import com.arteneta.githubapp.view.tools.GithubTabRowDefault
import com.arteneta.githubapp.view.tools.GithubTabRowDefault.pagerM3TabIndicatorOffset
import com.arteneta.githubapp.view.tools.ListItem
import com.arteneta.githubapp.view.tools.ProgressIndicator
import com.arteneta.githubapp.viewmodel.DetailUsersViewModel
import com.arteneta.githubapp.viewmodel.FollowViewModel
import com.arteneta.githubapp.viewmodel.RequestResult
import com.arteneta.githubapp.widget.ComposeAppWidget
import com.arteneta.githubapp.widget.WidgetWorker
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailUserScreen(
	login: String,
	navigateToDetail: (String) -> Unit
) {

	val detailUsersViewModel: DetailUsersViewModel = hiltViewModel()
	val context = LocalContext.current

	LaunchedEffect(key1 = login) {
		detailUsersViewModel.setDetailUsers(login)
	}
	val isExists by detailUsersViewModel.isExists(login).observeAsState(false)
	val coroutineScope = rememberCoroutineScope()

	val userDetailResult =
		detailUsersViewModel.detailUser.observeAsState(RequestResult.Progress).value



	var refreshing by remember { mutableStateOf(false) }
	fun refresh() = coroutineScope.launch(Dispatchers.IO) {
		refreshing = true
		detailUsersViewModel.setDetailUsers(login)
		refreshing = false
	}

	val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

	Box(
		modifier = Modifier
			.fillMaxSize()
			.pullRefresh(pullRefreshState)
	) {
		when (userDetailResult) {
			is RequestResult.Progress -> {
				ProgressIndicator()
			}
			is RequestResult.Success -> {
				DetailUserContent(
					user = userDetailResult.data,
					navigateToDetail = navigateToDetail,
					coroutineScope = coroutineScope
				)
				ExtendedFloatingActionButton(
					onClick = {
						coroutineScope.launch {
							WidgetWorker.enqueueUniqueWork(context, true)
						}
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
					Text(
						text = stringResource(id = R.string.favorite),
						modifier = Modifier.padding(start = 8.dp)
					)
				}
			}
			is RequestResult.Error -> {
				ErrorScreen(
					msg = userDetailResult.message,
					modifier = Modifier
						.align(Center)
						.verticalScroll(ScrollState(0))
						.padding(16.dp)
				)
			}
		}

		PullRefreshIndicator(
			refreshing = refreshing,
			state = pullRefreshState,
			modifier = Modifier.align(
				TopCenter
			)
		)
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailUserContent(
	user: DetailUser,
	coroutineScope: CoroutineScope,
	navigateToDetail: (String) -> Unit
) {

	val followViewModel: FollowViewModel = hiltViewModel()
	LaunchedEffect(key1 = user.login) {
		followViewModel.setFollowing(user.login)
		followViewModel.setFollower(user.login)
	}

	val pagerState = rememberPagerState()
	val tabTitle =
		listOf(
			stringResource(id = R.string.follower),
			stringResource(id = R.string.following)
		)


	Column(
		modifier = Modifier
			.padding(horizontal = 16.dp)
			.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {

		ProfileContent(
			avatarUrl = user.avatarUrl,
			login = user.login,
			userName = user.name,
			location = user.location,
			followers = user.followers ?: 0,
			following = user.following ?: 0,
			publicRepos = user.publicRepos ?: 0,
			company = user.company,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp)
				.verticalScroll(ScrollState(0), false)
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
			divider = { Divider() },
			modifier = Modifier.wrapContentSize()
		) {
			tabTitle.forEachIndexed { index, title ->
				Tab(
					selected = pagerState.currentPage == index,
					onClick = {
						coroutineScope.launch {
							pagerState.scrollToPage(index)
						}
					},
					selectedContentColor = MaterialTheme.colorScheme.primary,
					unselectedContentColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
					modifier = Modifier.heightIn(min = 48.dp)
				) {
					Text(
						text = title
					)
				}
			}
		}


		HorizontalPager(
			pageCount = 2,
			state = pagerState,
			verticalAlignment = Top,
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
								ErrorScreen(
									followers.message, modifier = Modifier
										.align(Center)
										.padding(16.dp)
								)
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
								ErrorScreen(
									following.message, modifier = Modifier
										.align(Center)
										.padding(16.dp)
								)
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
fun ColumnScope.ProfileContent(
	avatarUrl: String,
	login: String,
	userName: String?,
	location: String?,
	followers: Int,
	following: Int,
	publicRepos: Int,
	company: String?,
	modifier: Modifier
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Column(
			modifier = Modifier.fillMaxWidth(0.3f),
			verticalArrangement = Arrangement.spacedBy(4.dp),
			horizontalAlignment = CenterHorizontally
		) {
			AsyncImage(model = avatarUrl, contentDescription = login)
			Text(
				text = userName ?: "unknown",
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth()
			)
		}

		Column(
			modifier = Modifier.fillMaxWidth(0.7f),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			RightPanel(
				modifier = Modifier.fillMaxWidth(),
				icon = Icons.Default.Person,
				title = login
			)

			RightPanel(
				modifier = Modifier.fillMaxWidth(),
				icon = Icons.Default.LocationOn,
				title = location ?: "unknown"
			)

			FlowRow(
				modifier = Modifier.fillMaxWidth(),
				crossAxisSpacing = 6.dp,
				mainAxisSpacing = 8.dp,
				mainAxisSize = SizeMode.Wrap,
				mainAxisAlignment = FlowMainAxisAlignment.Start
			) {
				RightBottomPanel(
					modifier = Modifier.wrapContentSize(),
					title = stringResource(id = R.string.follower),
					subtitle = "$followers",
					colors = listOf(Color.Red, Orange)
				)
				RightBottomPanel(
					modifier = Modifier.wrapContentSize(),
					title = stringResource(id = R.string.following),
					subtitle = "$following",
					colors = listOf(Purple, Purple1)
				)
				RightBottomPanel(
					modifier = Modifier.wrapContentSize(),
					title = stringResource(id = R.string.repo),
					subtitle = "$publicRepos",
					colors = listOf(Blue, Blue1)
				)
			}
		}
	}

	Row(
		modifier = Modifier.align(CenterHorizontally),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(imageVector = Icons.Default.HomeWork, contentDescription = "Company Icon")
		Text(
			text = company ?: "unknown",
			textAlign = TextAlign.Center,
			style = Typography.titleMedium
		)
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
	subtitle: String,
	colors: List<Color>
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = title,
			maxLines = 1,
			modifier = Modifier
				.background(
					shape = RoundStart,
					brush = Brush.linearGradient(
						colors = listOf(Color.Black, Color.DarkGray)
					)
				)
				.padding(8.dp, 2.dp),
			fontFamily = FontFamily.Monospace,
			style = Typography.titleSmall.copy(color = Color.White)
		)
		Text(
			text = subtitle,
			maxLines = 1,
			modifier = Modifier
				.background(
					shape = RoundEnd,
					brush = Brush.linearGradient(
						colors = colors
					)
				)
				.padding(8.dp, 2.dp),
			fontFamily = FontFamily.Monospace,
			style = Typography.titleSmall.copy(color = Color.White)
		)
	}
}

