package com.dicoding.submission.view.navigation

sealed class NavRoute(val route: String) {
	object SplashScreen : NavRoute("splash")
	object HomeScreen : NavRoute("home") {
		fun navigateToDetail(login: String) = "$route/$login"
		val UserDetail = "$route/{login}"
	}
	object SettingsScreen: NavRoute("setting")
	object FavoriteScreen: NavRoute("favorite")
}