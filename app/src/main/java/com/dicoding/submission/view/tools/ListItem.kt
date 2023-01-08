package com.dicoding.submission.view.tools

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ListItem(
	login: String,
	avatarUrl: String?,
	userScore: Double? = null
) {
	Row {
		AsyncImage(
			model = avatarUrl,
			contentDescription = login,
			modifier = Modifier.size(65.dp)
		)
		Column {
			Text(text = login)
			userScore?.let {
				Text(text = "Scores: $it")
			}
		}
	}
}