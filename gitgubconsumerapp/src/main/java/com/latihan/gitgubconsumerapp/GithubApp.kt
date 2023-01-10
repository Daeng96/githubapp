package com.latihan.gitgubconsumerapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun GithubApp() {

	val bottomSheetNavigator = rememberBottomSheetNavigator()
	val navHostController = rememberNavController(bottomSheetNavigator)

	ModalBottomSheetLayout(bottomSheetNavigator = bottomSheetNavigator) {
		GithubNavigation(navHostController = navHostController)
	}
}