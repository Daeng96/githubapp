package com.dicoding.submission.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dicoding.submission.R
import kotlinx.coroutines.delay

private const val DELAY_MILLIS = 2000L

@Composable
fun SplashScreen(navigateToHomeScreen: () -> Unit) {

	LaunchedEffect(key1 = Unit, block = {
		delay(DELAY_MILLIS)
		navigateToHomeScreen()
	})

	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		Image(
			painter = painterResource(id = R.drawable.github_logo),
			contentDescription = "splash logo",
			modifier = Modifier.size(100.dp)
		)
	}
}