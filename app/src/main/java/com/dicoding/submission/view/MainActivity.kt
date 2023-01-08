package com.dicoding.submission.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.submission.theme.GithubMaterialTheme
import com.dicoding.submission.theme.Theme
import com.dicoding.submission.view.home.GitHubScaffold
import dagger.hilt.android.AndroidEntryPoint

private const val USER_PREF_NAME = "ajajsjnd"
val Context.githubPref by preferencesDataStore(USER_PREF_NAME)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		//migration UI
		setContent {
			Surface(modifier = Modifier.fillMaxSize()) {
				GithubMaterialTheme(themeMode = Theme.LIGHT) {
					GitHubScaffold()
				}
			}
			BackHandler(true) { finish() }
		}
	}
}


