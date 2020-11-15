package com.latihan.gitgubconsumerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.web_view_layout.*

class MyWebView : AppCompatActivity() {
	companion object{
		const val URL_KEY = "html url"
	}
	@SuppressLint("SetJavaScriptEnabled")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.web_view_layout)

		supportActionBar?.apply {
			this.setDisplayHomeAsUpEnabled(true)
			this.title = getString(R.string.web_view_txt)
		}

		val url = intent.getStringExtra(URL_KEY)
		web_view.settings.javaScriptEnabled = true
		web_view.webViewClient = object : WebViewClient() {
			override fun onPageFinished(view: WebView?, url: String?) {
				super.onPageFinished(view, url)
				progress_horizontal.visibility = View.GONE
			}
		}

		url?.let { web_view.loadUrl(it) }
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

	override fun onSupportNavigateUp(): Boolean {
		onBackPressed()
		return super.onSupportNavigateUp()
	}
}