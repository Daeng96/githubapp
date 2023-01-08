package com.dicoding.submission.view.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.dicoding.submission.R
import com.dicoding.submission.theme.Typography
import com.dicoding.submission.view.navigation.NavRoute

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
	ExperimentalComposeUiApi::class
)
@Composable
fun GitHubTopBar(
	navBackStackEntry: NavBackStackEntry?,
	onSearchingUser: (String) -> Unit,
	navigateToFavorite: () -> Unit,
	navigateToSetting: () -> Unit,
	navigateUp: () -> Unit
) {
	navBackStackEntry?.let {
		when (it.destination.route) {
			NavRoute.HomeScreen.route -> {

				var isSearch by rememberSaveable { mutableStateOf(false) }
				val (userName, setUserName) = rememberSaveable {
					mutableStateOf("")
				}
				val keyboard = LocalSoftwareKeyboardController.current

				TopAppBar(
					title = {
						AnimatedContent(targetState = isSearch) { searching ->
							when (searching) {
								true -> {
									TextField(
										value = userName,
										onValueChange = setUserName,
										shape = MaterialTheme.shapes.medium,
										modifier = Modifier.heightIn(max = 50.dp),
										label = { Text(text = stringResource(id = R.string.search_hint))},
										textStyle = Typography.titleMedium,
										trailingIcon = {
											Row {
												IconButton(onClick = {
													setUserName("")
												}, enabled = userName.isNotEmpty()) {
													Icon(
														imageVector = Icons.Outlined.Close,
														contentDescription = "Delete"
													)
												}
											}
										},
										keyboardActions = KeyboardActions(
											onSearch = {
												onSearchingUser(userName)
												keyboard?.hide()
												isSearch = false
											}
										),
										keyboardOptions = KeyboardOptions.Default.copy(
											imeAction = ImeAction.Search,
											keyboardType = KeyboardType.Text
										)
									)
								}
								false -> {
									Text(text = stringResource(id = R.string.app_name), color = MaterialTheme.colorScheme.primary)
								}
							}
						}
					},
					actions = {
						if (!isSearch) {
							IconButton(onClick = { isSearch = true }) {
								Icon(
									imageVector = Icons.Outlined.Search,
									contentDescription = "Search"
								)
							}
						}

						IconButton(onClick = navigateToFavorite) {
							Icon(
								imageVector = Icons.Outlined.Favorite,
								contentDescription = "Favorite"
							)
						}

						IconButton(onClick = navigateToSetting) {
							Icon(
								imageVector = Icons.Outlined.Settings,
								contentDescription = "Setting"
							)
						}
					}
				)
			}
			NavRoute.HomeScreen.UserDetail -> {
				val userName = it.arguments?.getString("login") ?: "User Name"
				CenterAlignedTopAppBar(
					title = { Text(text = userName) },
					navigationIcon = {
						IconButton(onClick = { navigateUp() }) {
							Icon(
								imageVector = Icons.Outlined.ArrowBack,
								contentDescription = "back"
							)
						}
					}
				)
			}
			NavRoute.SettingsScreen.route, NavRoute.FavoriteScreen.route -> {
				CenterAlignedTopAppBar(
					title = { Text(text = it.destination.route!!) },
					navigationIcon = {
						IconButton(onClick = { navigateUp() }) {
							Icon(
								imageVector = Icons.Outlined.ArrowBack,
								contentDescription = "back"
							)
						}
					}
				)
			}
			else -> {}
		}
	}
}

