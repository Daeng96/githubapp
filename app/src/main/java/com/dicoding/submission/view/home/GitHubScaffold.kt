package com.dicoding.submission.view.home

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dicoding.submission.theme.TopShape
import com.dicoding.submission.view.navigation.GitHubNavigation
import com.dicoding.submission.view.navigation.NavRoute
import com.dicoding.submission.viewmodel.SearchViewModel
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class)
@Composable
fun Activity.GitHubScaffold() {

	val bottomSheetNavigator = rememberBottomSheetNavigator()
	val navHostController = rememberNavController(bottomSheetNavigator)
	val navBackStackEntry by navHostController.currentBackStackEntryAsState()
	val searchViewModel: SearchViewModel = hiltViewModel()
	val searchResult = searchViewModel.searchUsersResult.observeAsState().value

	ModalBottomSheetLayout(
		bottomSheetNavigator,
		sheetShape = TopShape ,
		sheetBackgroundColor =  MaterialTheme.colorScheme.surfaceVariant
	) {
		Scaffold(
			modifier = Modifier.fillMaxSize(),
			topBar = {
				GitHubTopBar(
					navBackStackEntry = navBackStackEntry,
					navigateUp = { navHostController.navigateUp() },
					onSearchingUser = { userName -> searchViewModel.setUser(userName.trim()) },
					navigateToSetting = { navHostController.navigate(NavRoute.SettingsScreen.route) },
					navigateToFavorite = { navHostController.navigate(NavRoute.FavoriteScreen.route) }
				)
			}
		) {
			GitHubNavigation(
				navController = navHostController,
				paddingValues = it,
				searchResult = searchResult,
				onBackPressed = { finish() }
			)
		}
	}
}