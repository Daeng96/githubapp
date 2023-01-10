package com.latihan.gitgubconsumerapp

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
import com.latihan.gitgubconsumerapp.Utils.CONTENT_URI
import com.latihan.gitgubconsumerapp.theme.*


/*
class DetailsFavoriteFragment : BottomSheetDialogFragment() {

	private lateinit var listFavorites: Favorites
	companion object {

		const val FV_KEY = "fav"
		fun newInstance( favorites: Favorites) : DetailsFavoriteFragment{
			val fragment = DetailsFavoriteFragment()
			val args = Bundle().apply {
				putParcelable(FV_KEY, favorites)
			}
			fragment.arguments = args
			return fragment
		}

	}

	fun showBottomModal(fragmentManager: FragmentManager) {
		if (fragmentManager.findFragmentByTag(FV_KEY) != null) return
		showNow(fragmentManager, FV_KEY)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.details_favorite_fragment, container, false)
	}

	@SuppressLint("SetJavaScriptEnabled")
	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		arguments?.getParcelable<Favorites>(FV_KEY)?.let {
			listFavorites = it
		}
		tv_company_fv.text = listFavorites.company
		tv_detail_fv_na.text = listFavorites.name
		tv_username_fv.text = listFavorites.login
		tv_followers_fv.text = listFavorites.followers.toString()
		tv_following_fv.text = listFavorites.following.toString()
		tv_repos_fv.text = listFavorites.publicRepos.toString()
		tv_location_fv.text = listFavorites.location

		Glide.with(this)
			.load(listFavorites.avatarUrl)
			.apply(RequestOptions.circleCropTransform())
			.into(img_detail_fv)

		btn_more.setOnClickListener{

			val i = Intent(context, MyWebView::class.java)
			i.putExtra(MyWebView.URL_KEY, listFavorites.htmlUrl)
			startActivity(i)
		}
	}

}
*/

@Composable
fun DetailUserBottomSheet(id: Int, toWebView: (String) -> Unit) {

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
				onClick = { },
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
