package com.dicoding.submission.view.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dicoding.submission.view.GitHubNavigation
import com.dicoding.submission.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitHubScaffold() {

	val navHostController = rememberNavController()
	val navBackStackEntry by navHostController.currentBackStackEntryAsState()
	val searchViewModel : SearchViewModel = hiltViewModel()
	val searchResult = searchViewModel.searchUsersResult.observeAsState().value

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		topBar = {
			GitHubTopBar(
				navBackStackEntry = navBackStackEntry,
				navigateUp = { navHostController.navigateUp() },
				onSearchingUser = { userName ->
					searchViewModel.setUser(userName)
				}
			)
		}
	) {
		GitHubNavigation(
			navController = navHostController,
			paddingValues = it,
			searchResult = searchResult
		)
	}
}