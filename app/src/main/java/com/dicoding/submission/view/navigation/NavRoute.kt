package com.dicoding.submission.view.navigation

sealed class NavRoute(val route: String) {
	object SplashScreen : NavRoute("Splash")
	object HomeScreen : NavRoute("Home") {
		fun navigateToDetail(login: String) = "$route/$login"
		val UserDetail = "$route/{login}"
	}

	object SettingsScreen : NavRoute("Setting")
	object FavoriteScreen : NavRoute("Favorite") {
		fun showBottomSheet(userName: String) = "$route/$userName"
		val BottomSheetRoute = "$route/{login}"
	}

}