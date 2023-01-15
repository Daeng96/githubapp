package com.latihan.githubconsumerapp

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import com.latihan.githubconsumerapp.Utils.fixArgs

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
				navHostController.navigate("Home/$it")
			})
		}

		bottomSheet(
			route = "Home/{id}",
			arguments = listOf(navArgument("id") { type = NavType.LongType })
		) {
			DetailUserBottomSheet(
				id = it.arguments?.getLong("id") as Long,
				toWebView = { url ->
					navHostController.navigate("WebView/${url.fixArgs()}")
				}
			)
		}

		composable(
			route = "WebView/{url}",
			arguments = listOf(navArgument("url") { type = NavType.StringType })
		) {
			MyWebView(it.arguments?.getString("url") as String) {
				navHostController.navigateUp()
			}
		}

	}
}

