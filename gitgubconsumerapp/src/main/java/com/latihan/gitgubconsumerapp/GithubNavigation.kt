package com.latihan.gitgubconsumerapp

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun Activity.GithubNavigation(
	navHostController: NavHostController,
	modifier: Modifier
) {
	NavHost(navController = navHostController, "Splash", modifier = modifier) {
		composable("Splash") {
			SplashScreen {
				navHostController.navigate("Home")
			}
		}

		composable("Home") {
			HomeScreen(showBottomSheet = {
				navHostController.navigate("UserDetail/$it")
			})
		}

		bottomSheet(
			route = "UserDetail/{id}",
			arguments = listOf(navArgument("id") { type = NavType.IntType })
		) {

			DetailUserBottomSheet(
				id = it.arguments?.getInt("id") as Int,
				toWebView = {

				})
		}

		activity("WebView/{url}"){
			Web
		}
	}
}

