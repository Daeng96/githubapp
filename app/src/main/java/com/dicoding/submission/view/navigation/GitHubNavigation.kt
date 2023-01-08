package com.dicoding.submission.view.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dicoding.submission.model.User
import com.dicoding.submission.view.DetailUserScreen
import com.dicoding.submission.view.ListFavoritesScreen
import com.dicoding.submission.view.SplashScreen
import com.dicoding.submission.view.home.HomeScreen
import com.dicoding.submission.viewmodel.RequestResult

@Composable
fun GitHubNavigation(
	navController: NavHostController,
	paddingValues: PaddingValues,
	searchResult: RequestResult<User>?
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
			HomeScreen(searchResult = searchResult)
		}
		composable(
			route = NavRoute.HomeScreen.UserDetail,
			arguments = listOf(navArgument("login") {})
		) {
			val login = it.arguments?.getString("login") as String
			DetailUserScreen(login = login)
		}
		composable(route = NavRoute.SettingsScreen.route) {

		}

		composable(NavRoute.FavoriteScreen.route) {
			ListFavoritesScreen()
		}
	}
}