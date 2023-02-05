package com.latihan.githubconsumerapp

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyWebView(url: String, onBackPressed: () -> Unit) {

	val coroutineScope = rememberCoroutineScope()
	var refreshing by remember { mutableStateOf(false) }
	fun refresh() =
		coroutineScope.launch(Dispatchers.IO) { refreshing = true }

	val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)
	val webViewState = rememberWebViewState(url = url)
	val webNavigator = rememberWebViewNavigator()

	LaunchedEffect(
		key1 = refreshing,
		key2 = webViewState.loadingState is LoadingState.Finished,
		block = {
			if (refreshing) webNavigator.reload()
			val finished = webViewState.loadingState is LoadingState.Finished
			if (finished) { refreshing = false }
		}
	)
	Box(
		modifier = Modifier
			.fillMaxSize()
			.pullRefresh(pullRefreshState)
	) {

		WebView(
			state = webViewState,
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState()),
			captureBackPresses = true,
			navigator = webNavigator,
			onCreated = {
				it.settings.javaScriptEnabled = true
			}
		)

		AnimatedVisibility(
			visible = webViewState.isLoading || refreshing,
			modifier = Modifier.align(Alignment.Center)
		) {
			CircularProgressIndicator()
		}

		PullRefreshIndicator(
			backgroundColor = MaterialTheme.colorScheme.primary,
			contentColor = MaterialTheme.colorScheme.onPrimary,
			modifier = Modifier.align(Alignment.TopCenter),
			refreshing = refreshing,
			state = pullRefreshState
		)
	}

	BackHandler(true) {
		if (webNavigator.canGoBack) webNavigator.navigateBack() else onBackPressed()
	}
}
