package com.latihan.gitgubconsumerapp

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.latihan.gitgubconsumerapp.theme.TopShape

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Activity.GithubApp() {

	val bottomSheetNavigator = rememberBottomSheetNavigator()
	val navHostController = rememberNavController(bottomSheetNavigator)

	ModalBottomSheetLayout(
		bottomSheetNavigator = bottomSheetNavigator,
		sheetBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
		sheetShape = TopShape
	) {
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			topBar = {
				navHostController.currentBackStackEntryFlow.collectAsState(null).value?.let {
					if (it.destination.route != "Splash") {
						CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.app_name))})
					}
				}
			}
		) {
			GithubNavigation(
				navHostController = navHostController,
				modifier = Modifier
					.fillMaxSize()
					.padding(it)
			)
		}
	}
}