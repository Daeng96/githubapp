package com.latihan.gitgubconsumerapp

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.latihan.gitgubconsumerapp.Utils.fixUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyWebView(url: String, onBackPressed: () -> Unit) {

	val coroutineScope = rememberCoroutineScope()
	var refreshing by remember { mutableStateOf(false) }
	var loaded by remember { mutableStateOf(true) }
	var webView: WebView? = null
	fun refresh() = coroutineScope.launch(Dispatchers.IO) {
		refreshing = true
	}

	val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

	Box(
		modifier = Modifier
			.fillMaxSize()
			.pullRefresh(pullRefreshState)
	) {

		AndroidView(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(ScrollState(0)),
			factory = { context ->
				WebView(context).apply {
					if (refreshing) webView?.loadUrl(url.fixUri())
					settings.javaScriptEnabled = true
					webViewClient = object : WebViewClient() {
						override fun onPageFinished(view: WebView?, url: String?) {
							super.onPageFinished(view, url)
							loaded = false
							refreshing = false
						}
					}
				}
			}
		) { wv ->
			url.let { wv.loadUrl(it.fixUri()) }
			webView = wv
		}

		AnimatedVisibility(
			visible = loaded,
			modifier = Modifier.align(
				Alignment.Center
			)
		) {
			CircularProgressIndicator()
		}

		PullRefreshIndicator(
			refreshing = refreshing,
			state = pullRefreshState,
			modifier = Modifier.align(
				Alignment.TopCenter
			)
		)
	}

	BackHandler(true) {
		if (webView?.canGoBack() == true) webView?.goBack() else onBackPressed()
	}
}
