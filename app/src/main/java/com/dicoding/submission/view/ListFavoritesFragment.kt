package com.dicoding.submission.view

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.dicoding.submission.view.tools.ListItem
import com.dicoding.submission.viewmodel.DetailUsersViewModel

@Composable
fun ListFavoritesScreen() {
	val detailUsersViewModel: DetailUsersViewModel = hiltViewModel()
	val favorites = detailUsersViewModel.allUsers.observeAsState(emptyList()).value

	LazyColumn(content = {
		items(items = favorites) { user ->
			ListItem(login = user.login!!, avatarUrl = user.avatarUrl)
		}
	})

}