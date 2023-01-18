package com.arteneta.githubapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arteneta.githubapp.R
import com.arteneta.githubapp.view.tools.ListItem
import com.arteneta.githubapp.viewmodel.DetailUsersViewModel

@Composable
fun ListFavoritesScreen(
	showBottomSheet: (String) -> Unit
) {

	val detailUsersViewModel: DetailUsersViewModel = hiltViewModel()
	val favorites = detailUsersViewModel.allUsers.observeAsState(emptyList()).value

	LazyColumn(
		contentPadding = PaddingValues(all = 16.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		content = {
			items(items = favorites) { user ->
				ListItem(
					login = user.login,
					avatarUrl = user.avatarUrl,
					subTitle = stringResource(id = R.string.repo).plus(" ${user.publicRepos}"),
					itemOnClick = showBottomSheet,
					actionContainer = {
						IconButton(
							modifier = Modifier.align(Alignment.CenterEnd), onClick = {
								detailUsersViewModel.unFavorite(user)
							}) {
							Icon(imageVector = Icons.Outlined.Delete, contentDescription = "delete")
						}
					}
				)
			}
		}
	)
}