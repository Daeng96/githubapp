package com.dicoding.submission.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dicoding.submission.view.home.HomeScreen
import com.dicoding.submission.view.navigation.NavRoute
import com.dicoding.submission.viewmodel.SearchResult

@Composable
fun GitHubNavigation(
	navController: NavHostController,
	paddingValues: PaddingValues,
	searchResult: SearchResult?
) {

	NavHost(
		navController = navController,
		startDestination = NavRoute.SplashScreen.route,
		modifier = Modifier
			.fillMaxSize()
			.padding(paddingValues)
	) {

		composable(route = NavRoute.SplashScreen.route) {

		}
		composable(route = NavRoute.HomeScreen.route) {
			HomeScreen()
		}
		composable(route = NavRoute.HomeScreen.UserDetail) {

		}
		composable(route = NavRoute.SettingsScreen.route) {

		}
	}
}