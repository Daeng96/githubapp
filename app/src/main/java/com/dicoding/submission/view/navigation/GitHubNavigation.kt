package com.dicoding.submission.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dicoding.submission.view.navigation.NavRoute

@Composable
fun GitHubNavigation(
	navController: NavHostController
) {

	NavHost(navController = navController, startDestination = NavRoute.SplashScreen.route) {

		composable(route = NavRoute.SplashScreen.route) {

		}
		composable(route = NavRoute.HomeScreen.route) {

		}
		composable(route = NavRoute.HomeScreen.UserDetail) {

		}
		composable(route = NavRoute.SettingsScreen.route) {

		}
	}
}