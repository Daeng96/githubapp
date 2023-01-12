package com.arteneta.githubapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.datastore.preferences.preferencesDataStore
import com.arteneta.githubapp.theme.GithubMaterialTheme
import com.arteneta.githubapp.view.home.GitHubScaffold
import dagger.hilt.android.AndroidEntryPoint

private const val USER_PREF_NAME = "ajajsjnd"
val Context.githubPref by preferencesDataStore(USER_PREF_NAME)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		WindowCompat.setDecorFitsSystemWindows(window, false)
		//migration UI

		val id = intent.getIntExtra("id_key", 0)

		setContent {
			Log.i("intent", "$id")
			GithubMaterialTheme {
				GitHubScaffold()
			}
		}
	}

	companion object {
		const val EXTRA_PERSON = "person"
	}
}


