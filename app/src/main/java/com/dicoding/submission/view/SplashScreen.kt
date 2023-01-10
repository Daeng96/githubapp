package com.dicoding.submission.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dicoding.submission.R

private const val DELAY_MILLIS = 2000L

@Composable
fun SplashScreen(navigateToHomeScreen: () -> Unit) {

	val alphaAnimation = remember { Animatable(initialValue = 0f) }

	LaunchedEffect(key1 = Unit, block = {
		alphaAnimation.animateTo(1f, tween(2000, delayMillis = 1000, easing = FastOutSlowInEasing))
		if (!alphaAnimation.isRunning) navigateToHomeScreen()
	})

	Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		Image(
			painter = painterResource(id = R.drawable.github_logo),
			contentDescription = "splash logo",
			modifier = Modifier
				.size(100.dp)
				.alpha(alphaAnimation.value)
		)
	}
}