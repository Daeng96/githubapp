package com.latihan.gitgubconsumerapp

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.latihan.gitgubconsumerapp.Utils.CONTENT_URI
import com.latihan.gitgubconsumerapp.theme.GithubMaterialTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFavoriteActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		WindowCompat.setDecorFitsSystemWindows(window, false)
		setContent {
			GithubMaterialTheme {
				GithubApp()
			}
		}
	}
/*	@SuppressLint("Recycle")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_list_favorite)

		recyclerView = rv_favorite
		adapter = FavoriteAdapter(this)
		recyclerView.setHasFixedSize(true)
		recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

		// loadData()
		adapterClick()

		val handleThread = HandlerThread("DataObserver")
		handleThread.start()
		val handler = Handler(handleThread.looper)
		val observer = object : ContentObserver(handler) {
			override fun onChange(selfChange: Boolean) {
				loadData()
				super.onChange(selfChange)
			}
		}
		contentResolver.registerContentObserver(CONTENT_URI, true, observer)
		if (savedInstanceState == null) {
			loadData()
		} else {
			val list = savedInstanceState.getParcelableArrayList<Favorites>(EXTRA_KEY)
			if (list != null) {
				adapter.setListFavorite(list)
			}
		}
		swipe_refresh.setOnRefreshListener {
			refreshList()
			swipe_refresh.isRefreshing = false
		}
	}

	private fun adapterClick() {
		val f = supportFragmentManager
		adapter.click {
			DetailsFavoriteFragment.newInstance(it).showBottomModal(f)
		}

		adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
			override fun onItemClicked(data: Favorites) {

				uriWithId = Uri.parse((CONTENT_URI.toString() + "/" + data._ID))
				AlertDialog.Builder(this@ListFavoriteActivity)
					.setTitle(getString(R.string.btn_remove))
					.setMessage(getString(R.string.msg_remove_user))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.btn_remove)) { _, _ ->
						contentResolver.delete(uriWithId, null, null)
						Toast.makeText(
							this@ListFavoriteActivity,
							getString(R.string.msg_removed),
							Toast.LENGTH_LONG
						).show()
						refreshList()
					}
					.setNegativeButton(getString(R.string.btn_negative), null)
					.show()
			}
		})
	}

	private fun loadData() {
		GlobalScope.launch(Dispatchers.Main) {
			val sync = async(Dispatchers.IO) {
				val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
				MappingHelper.mapCursor(cursor)
			}
			val list = sync.await()
			recyclerView.layoutManager = LinearLayoutManager(applicationContext)
			recyclerView.adapter = adapter
			if (list.size > 0) {
				adapter.setListFavorite(list)
			} else {
				adapter.setListFavorite(ArrayList())
			}
		}
	}

	@SuppressLint("Recycle")
	private fun refreshList() {
		loadData()
		adapter.notifyDataSetChanged()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		val d = contentResolver?.query(CONTENT_URI, null, null, null, null)
		val e = MappingHelper.mapCursor(d)
		outState.putParcelableArrayList(EXTRA_KEY, e)
	}

	override fun onResume() {
		super.onResume()
		refreshList()
	}*/
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Activity.HomeScreen(
	showBottomSheet: (Int) -> Unit
) {

	val favoriteList = remember { mutableStateListOf<Favorites>() }

	LaunchedEffect(key1 = favoriteList, block = {
		val cursor = contentResolver.query(
			CONTENT_URI,
			null,
			null,
			null,
			null,
			null
		)
		favoriteList.addAll(MappingHelper.mapCursor(cursor))
	})

	val coroutineScope = rememberCoroutineScope()
	var refreshing by remember { mutableStateOf(false) }
	fun refresh() = coroutineScope.launch(Dispatchers.IO) {
		refreshing = true
		favoriteList.clear()
		val cursor = contentResolver.query(
			CONTENT_URI,
			null,
			null,
			null,
			null,
			null
		)

		favoriteList.addAll(MappingHelper.mapCursor(cursor))
		refreshing = false
	}

	val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

	Box(
		modifier = Modifier
			.fillMaxSize()
			.pullRefresh(pullRefreshState)
	) {
		LazyColumn(
			state = rememberLazyListState(),
			contentPadding = PaddingValues(horizontal = 16.dp, 8.dp),
			modifier = Modifier.fillMaxSize(),
			//verticalArrangement = Arrangement.spacedBy(8.dp),
			content = {
				items(items = favoriteList, key = { it._ID }) { user ->
					ListItem(
						login = user.login,
						avatarUrl = user.avatarUrl,
						subTitle = stringResource(id = R.string.repo).plus(" ${user.publicRepos}"),
						itemOnClick = { showBottomSheet(user._ID) },
						deleteUser = {
							val uriWithId = Uri.parse("$CONTENT_URI/${user._ID}")
							contentResolver.delete(uriWithId, null, null)
							favoriteList.removeIf { it._ID == user._ID }
						}
					)
				}
			}
		)

		PullRefreshIndicator(
			refreshing = refreshing,
			state = pullRefreshState,
			modifier = Modifier.align(
				Alignment.TopCenter
			)
		)
	}

	BackHandler(true) {
		finish()
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ListItem(
	login: String,
	avatarUrl: String?,
	subTitle: String? = null,
	itemOnClick: (String) -> Unit,
	deleteUser: () -> Unit
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.animateItemPlacement()
			.clickable { itemOnClick(login) },
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.width(IntrinsicSize.Max)
		) {
			AsyncImage(
				model = avatarUrl,
				contentDescription = login,
				modifier = Modifier
					.size(50.dp)
					.clip(CircleShape)
			)
			Column(
				verticalArrangement = Arrangement.spacedBy(8.dp),
				modifier = Modifier
					.wrapContentSize()
			) {
				Text(text = login)
				subTitle?.let {
					Text(text = it)
				}

			}
		}

		OutlinedButton(
			onClick = deleteUser,
			colors = ButtonDefaults.outlinedButtonColors(
				contentColor = MaterialTheme.colorScheme.primary
			),
			shape = CircleShape
		) {
			Text(text = stringResource(id = R.string.btn_remove))
		}
	}
	Divider(modifier = Modifier.padding(vertical = 4.dp))
}