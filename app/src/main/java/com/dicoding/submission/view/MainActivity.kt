package com.dicoding.submission.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dicoding.submission.theme.GithubMaterialTheme
import com.dicoding.submission.theme.Theme
import com.dicoding.submission.view.home.GitHubScaffold
import dagger.hilt.android.AndroidEntryPoint

//import kotlinx.android.synthetic.main.activity_main.*


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
        }
    }
}


