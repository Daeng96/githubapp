package com.arteneta.githubapp.view.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.arteneta.githubapp.R
import com.arteneta.githubapp.theme.Typography
import com.arteneta.githubapp.view.navigation.NavRoute

@OptIn(
	ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class,
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
				val (userName, setUserName) = rememberSaveable { mutableStateOf("") }
				val keyboard = LocalSoftwareKeyboardController.current

				TopAppBar(
					title = {
						AnimatedContent(targetState = isSearch) { searching ->
							when (searching) {
								true -> {
									SearchView(userName = userName, setUserName = setUserName) {
										onSearchingUser(userName)
										keyboard?.hide()
										isSearch = false
									}
								}
								false -> {
									Text(
										text = stringResource(id = R.string.app_name),
										color = MaterialTheme.colorScheme.primary
									)
								}
							}
						}
					},
					actions = {
						AnimatedVisibility(visible = !isSearch) {
							IconButton(onClick = { isSearch = true; keyboard?.show() }) {
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
				val userName = it.arguments?.getString("login") ?: "User Not Found"
				CenterAlignedTopAppBar(
					title = { Text(text = userName) },
					navigationIcon = {
						IconButton(onClick = { navigateUp() }) {
							Icon(
								imageVector = Icons.Outlined.ArrowBack,
								contentDescription = "back"
							)
						}
					},
					colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
						navigationIconContentColor = MaterialTheme.colorScheme.primary,
						titleContentColor = MaterialTheme.colorScheme.primary,
						actionIconContentColor = MaterialTheme.colorScheme.primary,
					)
				)
			}
			NavRoute.SettingsScreen.route, NavRoute.FavoriteScreen.route, NavRoute.FavoriteScreen.BottomSheetRoute -> {
				CenterAlignedTopAppBar(
					title = { Text(text = it.destination.route!!.substringBefore("/")) },
					navigationIcon = {
						IconButton(onClick = { navigateUp() }) {
							Icon(
								imageVector = Icons.Outlined.ArrowBack,
								contentDescription = "back"
							)
						}
					},
					colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
						navigationIconContentColor = MaterialTheme.colorScheme.primary,
						titleContentColor = MaterialTheme.colorScheme.primary,
						actionIconContentColor = MaterialTheme.colorScheme.primary,
					)
				)
			}
			else -> {}
		}
	}
}

@Composable
fun SearchView(
	userName: String,
	setUserName: (String) -> Unit,
	searchAction: () -> Unit
) {

	BasicTextField(
		modifier = Modifier
			.fillMaxWidth()
			.background(
				color = MaterialTheme.colorScheme.primary.copy(0.1f),
				shape = CircleShape
			)
			.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
			.padding(start = 16.dp),
		value = userName, onValueChange = setUserName,
		keyboardActions = KeyboardActions(
			onSearch = {
				defaultKeyboardAction(ImeAction.Search)
				searchAction()
			}
		),
		keyboardOptions = KeyboardOptions.Default.copy(
			imeAction = ImeAction.Search,
			keyboardType = KeyboardType.Text
		), cursorBrush = Brush.verticalGradient(
			listOf(
				MaterialTheme.colorScheme.primary,
				MaterialTheme.colorScheme.primary.copy(0.2f)
			)
		),
		decorationBox = { innerTextField ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				innerTextField()
				IconButton(
					onClick = { setUserName("") },
					enabled = userName.isNotEmpty(),
					colors = IconButtonDefaults.iconButtonColors(
						contentColor = Color.Red
					)
				) {
					Icon(Icons.Outlined.Close, contentDescription = "Delete")
				}
			}
		},
		maxLines = 1,
		textStyle = Typography.titleMedium.copy(
			color = MaterialTheme.colorScheme.onBackground,
			fontFamily = FontFamily.Monospace
		)
	)
}

