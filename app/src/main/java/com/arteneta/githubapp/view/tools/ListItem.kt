package com.arteneta.githubapp.view.tools

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ListItem(
	login: String,
	avatarUrl: String?,
	subTitle: String? = null,
	actionContainer: @Composable BoxScope.() -> Unit = {},
	itemOnClick: (String) -> Unit = {}
) {

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.animateItemPlacement(),
	) {
		Row(
			modifier = Modifier
				.align(CenterStart)
				.fillMaxWidth()
				.clickable { itemOnClick(login) },

			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = CenterVertically
		) {
			SubcomposeAsyncImage(
				model = avatarUrl,
				contentDescription = login,
				loading = { CircularProgressIndicator() },
				error = {
					Icon(
						imageVector = Icons.Default.BrokenImage,
						contentDescription = "error load $login avatar",
						tint = MaterialTheme.colorScheme.onError
					)
				},
				modifier = Modifier
					.size(50.dp)
					.clip(CircleShape)
			)

			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {
				Text(text = login)
				subTitle?.let {
					Text(text = it)
				}
				subTitle?.let {
					Divider()
				}
			}
		}

		actionContainer()

	}
}