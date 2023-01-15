package com.latihan.githubconsumerapp

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.latihan.githubconsumerapp.Utils.CONTENT_URI
import com.latihan.githubconsumerapp.theme.*

@Composable
fun DetailUserBottomSheet(id: Long, toWebView: (String) -> Unit) {

	val context = LocalContext.current
	val uri = Uri.parse("$CONTENT_URI/$id")
	val cursor = context.contentResolver.query(uri, null, null, null, null, null)
	var user by rememberSaveable { mutableStateOf(Favorites()) }

	LaunchedEffect(key1 = id, block = {
		user = MappingHelper.getUserById(cursor)
	})

	Surface(
		contentColor = MaterialTheme.colorScheme.onBackground,
		modifier = Modifier.fillMaxWidth(),
	) {
		Column(
			modifier = Modifier.fillMaxWidth().wrapContentHeight(Top),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Spacer(modifier = Modifier.height(8.dp))
			ProfileContent(
				avatarUrl = user.avatarUrl,
				userName = user.name,
				location = user.location,
				followers = user.followers ?: 0,
				following = user.following ?: 0,
				publicRepos = user.publicRepos ?: 0,
				company = user.company,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 8.dp),
				login = user.login
			)
			OutlinedButton(
				onClick = { toWebView(user.htmlUrl)},
				modifier = Modifier.align(Alignment.CenterHorizontally),
				shape = CircleShape
			) {
				Text(text = stringResource(id = R.string.more))
			}
			Spacer(modifier = Modifier.height(8.dp))
		}
	}
}


@Composable
fun ColumnScope.ProfileContent(
	avatarUrl: String,
	login: String,
	userName: String?,
	location: String?,
	followers: Int,
	following: Int,
	publicRepos: Int,
	company: String?,
	modifier: Modifier
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		Column(
			modifier = Modifier.fillMaxWidth(0.3f),
			verticalArrangement = Arrangement.spacedBy(4.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			AsyncImage(model = avatarUrl, contentDescription = login)
			Text(
				text = userName ?: "unknown",
				textAlign = TextAlign.Center,
				modifier = Modifier.fillMaxWidth()
			)
		}

		Column(
			modifier = Modifier.fillMaxWidth(0.7f),
			verticalArrangement = Arrangement.spacedBy(8.dp)
		) {
			RightPanel(
				modifier = Modifier.fillMaxWidth(),
				icon = Icons.Default.Person,
				title = login
			)

			RightPanel(
				modifier = Modifier.fillMaxWidth(),
				icon = Icons.Default.LocationOn,
				title = location ?: "unknown"
			)

			FlowRow(
				modifier = Modifier.fillMaxWidth(),
				crossAxisSpacing = 6.dp,
				mainAxisSpacing = 8.dp,
				mainAxisSize = SizeMode.Wrap,
				mainAxisAlignment = FlowMainAxisAlignment.Start
			) {
				RightBottomPanel(
					modifier = Modifier.wrapContentSize(),
					title = stringResource(id = R.string.follower),
					subtitle = "$followers",
					colors = listOf(Color.Red, Orange)
				)
				RightBottomPanel(
					modifier = Modifier.wrapContentSize(),
					title = stringResource(id = R.string.following),
					subtitle = "$following",
					colors = listOf(Purple, Purple1)
				)
				RightBottomPanel(
					modifier = Modifier.wrapContentSize(),
					title = stringResource(id = R.string.repo),
					subtitle = "$publicRepos",
					colors = listOf(Blue, Blue1)
				)
			}
		}
	}

	Row(
		modifier = Modifier.align(Alignment.CenterHorizontally),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(imageVector = Icons.Default.HomeWork, contentDescription = "Company Icon")
		Text(
			text = company ?: "unknown",
			textAlign = TextAlign.Center,
			style = Typography.titleMedium
		)
	}
}


@Composable
private fun RightPanel(
	modifier: Modifier,
	icon: ImageVector,
	title: String
) {
	Row(
		modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Icon(imageVector = icon, contentDescription = title)
		Text(text = title, maxLines = 1)
	}
}

@Composable
private fun RightBottomPanel(
	modifier: Modifier,
	title: String,
	subtitle: String,
	colors: List<Color>
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = title,
			maxLines = 1,
			modifier = Modifier
				.background(
					shape = RoundStart,
					brush = Brush.linearGradient(
						colors = listOf(Color.Black, Color.DarkGray)
					)
				)
				.padding(8.dp, 2.dp),
			fontFamily = FontFamily.Monospace,
			style = Typography.titleSmall.copy(color = Color.White)
		)
		Text(
			text = subtitle,
			maxLines = 1,
			modifier = Modifier
				.background(
					shape = RoundEnd,
					brush = Brush.linearGradient(
						colors = colors
					)
				)
				.padding(8.dp, 2.dp),
			fontFamily = FontFamily.Monospace,
			style = Typography.titleSmall.copy(color = Color.White)
		)
	}
}
