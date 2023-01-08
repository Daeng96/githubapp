package com.dicoding.submission.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dicoding.submission.R
import com.dicoding.submission.model.ItemUser
import com.dicoding.submission.viewmodel.SearchResult

@Composable
fun HomeScreen(
	searchResult: SearchResult?
) {
	Box(modifier = Modifier.fillMaxSize()) {
		when (searchResult) {
			is SearchResult.OnSearch -> {
				CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
			}
			is SearchResult.OnError -> {
				Text(text = searchResult.errorMessage, modifier = Modifier.align(Alignment.Center))
			}
			is SearchResult.OnSuccess -> {
				UserListContent(itemsUser = searchResult.users)
			}
			else -> {
				Image(
					painter = painterResource(id = R.drawable.github_logo),
					modifier = Modifier.align(Alignment.Center),
					contentDescription = "github logo"
				)
			}
		}
	}
}

@Composable
private fun UserListContent(
	itemsUser: List<ItemUser>
) {
	LazyColumn(content = {
		items(itemsUser) { user ->
			Row {
				AsyncImage(
					model = user.avatarUrl,
					contentDescription = user.login,
					modifier = Modifier.size(65.dp)
				)
				Column {
					Text(text = user.login)
					user.score?.let {
						Text(text = "Scores: $it")
					}
				}
			}
		}
	})
}