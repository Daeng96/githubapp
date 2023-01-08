package com.dicoding.submission.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.submission.theme.GithubMaterialTheme
import com.dicoding.submission.view.home.GitHubScaffold
import dagger.hilt.android.AndroidEntryPoint

private const val USER_PREF_NAME = "ajajsjnd"
val Context.githubPref by preferencesDataStore(USER_PREF_NAME)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		WindowCompat.setDecorFitsSystemWindows(window, false)
		//migration UI
		setContent {
			GithubMaterialTheme {
				GitHubScaffold()
			}
		}
	}

	companion object {
		const val EXTRA_PERSON = "person"
	}
}


