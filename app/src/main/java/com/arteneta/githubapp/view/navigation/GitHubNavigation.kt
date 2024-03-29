package com.arteneta.githubapp.view.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.arteneta.githubapp.model.User
import com.arteneta.githubapp.view.*
import com.arteneta.githubapp.view.home.HomeScreen
import com.arteneta.githubapp.view.navigation.NavRoute.FavoriteScreen.BottomSheetRoute
import com.arteneta.githubapp.viewmodel.RequestResult
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun GitHubNavigation(
	navController: NavHostController,
	paddingValues: PaddingValues,
	searchResult: RequestResult<User>?,
	onBackPressed: () -> Unit
) {

	NavHost(
		navController = navController,
		startDestination = NavRoute.SplashScreen.route,
		modifier = Modifier
			.fillMaxSize()
			.padding(paddingValues)
	) {

		composable(route = NavRoute.SplashScreen.route) {
			SplashScreen(navigateToHomeScreen = { navController.navigate(NavRoute.HomeScreen.route) })
		}
		composable(route = NavRoute.HomeScreen.route) {
			HomeScreen(
				searchResult = searchResult,
				onBackPressed = onBackPressed,
				itemOnClick = {
					navController.navigate(NavRoute.HomeScreen.navigateToDetail(it)) {
						launchSingleTop = true
						popUpTo(NavRoute.HomeScreen.route) {
							saveState = true
						}
						restoreState = true
					}
				})
		}
		composable(
			route = NavRoute.HomeScreen.UserDetail,
			arguments = listOf(navArgument("login") {}),
			deepLinks = listOf(navDeepLink {
				action = "android.intent.action.VIEW"
				uriPattern = "http://www.arteneta.github.app/Home/{login}" }
			)
		) {
			val login = it.arguments?.getString("login")

			login?.let { it1 ->
				DetailUserScreen(login = it1, navigateToDetail = { us ->
					navController.navigate(NavRoute.HomeScreen.navigateToDetail(us))
				})
			}
		}
		composable(route = NavRoute.SettingsScreen.route) {
			SettingScreen()
		}

		composable(route = NavRoute.FavoriteScreen.route){
			ListFavoritesScreen(
				showBottomSheet = { login ->
					navController.navigate(
						NavRoute.FavoriteScreen.showBottomSheet(
							login
						)
					)
				})
		}

		bottomSheet(route = BottomSheetRoute, arguments = listOf(
			navArgument("login") {}
		)) {
			FavoriteBottomSheet(login = it.arguments?.getString("login") as String,
				navigateToDetail = { login ->
					navController.navigate(
						NavRoute.HomeScreen.navigateToDetail(
							login
						)
					)
				}
			)
		}
	}
}