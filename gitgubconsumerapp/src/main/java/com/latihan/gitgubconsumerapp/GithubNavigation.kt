package com.latihan.gitgubconsumerapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

@Composable
fun GithubNavigation(
	navHostController: NavHostController
) {
	NavHost(navController = navHostController, "Splash") {
		composable("Splash") {
			SplashScreen {
				navHostController.navigate("Home")
			}
		}

		composable("Home") {
			HomeScreen()
		}

		composable("UserDetail") {
			DetailUserBottomSheet(login = "")
		}
	}
}