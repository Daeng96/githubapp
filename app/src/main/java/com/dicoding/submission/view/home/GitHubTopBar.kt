package com.dicoding.submission.view.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import com.dicoding.submission.R
import com.dicoding.submission.view.navigation.NavRoute

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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

				TopAppBar(
					title = {
						AnimatedContent(targetState = isSearch) { searching ->
							when (searching) {
								true -> {
									OutlinedTextField(
										value = userName,
										onValueChange = setUserName,
										trailingIcon = {
											IconButton(onClick = {
												onSearchingUser(userName)
												isSearch = false
											}) {
												Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
											}
										}
									)
								}
								false -> {
									Text(text = stringResource(id = R.string.app_name))
								}
							}
						}
					},
					actions = {
						if (!isSearch) {

							IconButton(onClick = { isSearch = true }) {
								Icon(imageVector = Icons.Outlined.Search, contentDescription = "Search")
							}

							IconButton(onClick = navigateToFavorite) {
								Icon(imageVector = Icons.Outlined.Favorite, contentDescription = "Favorite")
							}

							IconButton(onClick = navigateToSetting) {
								Icon(imageVector = Icons.Outlined.Settings, contentDescription = "Setting")
							}
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
							Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "back")
						}
					}
				)
			}
			NavRoute.SettingsScreen.route -> {
				CenterAlignedTopAppBar(
					title = { Text(text = stringResource(id = R.string.setting)) },
					navigationIcon = {
						IconButton(onClick = { navigateUp() }) {
							Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "back")
						}
					}
				)
			}
			else -> {}
		}
	}
}

