package com.latihan.gitgubconsumerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyWebView : ComponentActivity() {


	companion object{
		const val URL_KEY = "html url"
	}
	@OptIn(ExperimentalMaterialApi::class)
	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			val coroutineScope = rememberCoroutineScope()
			var refreshing by remember { mutableStateOf(false) }
			fun refresh() = coroutineScope.launch(Dispatchers.IO) {
				refreshing = true
				refreshing = false
			}
			val pullRefreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)

			val url = intent.getStringExtra(URL_KEY)
			Box(modifier = Modifier.fillMaxSize().pullRefresh()) {
				AndroidView(factory = { context ->
					WebView(context).apply {
						settings.javaScriptEnabled = true
						webViewClient = object : WebViewClient() {
							override fun onPageFinished(view: WebView?, url: String?) {
								super.onPageFinished(view, url)
								//progress_horizontal.visibility = View.GONE
							}
						}
					}
				}) { wv ->
					url?.let { wv.loadUrl(it) }
				}
			}

		}



		refreshWebView.setOnRefreshListener{
			url?.let { web_view.loadUrl(it) }
			refreshWebView.isRefreshing = false
		}

	}


	override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
		if (keyCode == event?.keyCode && web_view.canGoBack()) {
			web_view.goBack()
			return true
		}
		return super.onKeyDown(keyCode, event)
	}

}