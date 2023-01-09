package com.dicoding.submission.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dicoding.submission.R
import com.dicoding.submission.db.Favorites
import com.dicoding.submission.viewmodel.DetailUsersViewModel

@Composable
fun FavoriteBottomSheet(
	login: String,
	navigateToDetail: (String) -> Unit
) {

	val detailUsersViewModel: DetailUsersViewModel = hiltViewModel()
	val user by detailUsersViewModel.getUserByLogin(login).collectAsState(initial = Favorites())

	Surface(contentColor = MaterialTheme.colorScheme.onBackground) {
		Column(
			modifier = Modifier.fillMaxWidth(),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Spacer(modifier = Modifier.height(8.dp))
			ProfileContent(
				avatarUrl = user.avatarUrl,
				login = user.login,
				userName = user.name,
				location = user.location,
				followers = user.followers ?: 0,
				following = user.following ?: 0,
				publicRepos = user.publicRepos ?: 0,
				company = user.company,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 8.dp)
			)
			OutlinedButton(
				onClick = { navigateToDetail(login) },
				modifier = Modifier.align(CenterHorizontally),
				shape = CircleShape
			) {
				Text(text = stringResource(id = R.string.more))
			}
			Spacer(modifier = Modifier.height(8.dp))
		}
	}

}
