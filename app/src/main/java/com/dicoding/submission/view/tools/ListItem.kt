package com.dicoding.submission.view.tools

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ListItem(
	login: String,
	avatarUrl: String?,
	userScore: Double? = null,
	itemOnClick: (String) -> Unit = {}
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable { itemOnClick(login) },
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = CenterVertically
	) {
		AsyncImage(
			model = avatarUrl,
			contentDescription = login,
			modifier = Modifier
				.size(50.dp)
				.clip(CircleShape)
		)
		Column(
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Text(text = login)
			userScore?.let {
				Text(text = "Scores: $it")
			}
			userScore?.let {
				Divider()
			}
		}
	}
}