package com.dicoding.submission.view

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
import com.dicoding.submission.view.home.HomeScreen
import com.dicoding.submission.view.navigation.NavRoute
import com.dicoding.submission.viewmodel.RequestResult
import com.dicoding.submission.viewmodel.SearchResult

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

		}
	}
}