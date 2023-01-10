package com.latihan.gitgubconsumerapp

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
import kotlinx.coroutines.delay

/*class SplashScreen : AppCompatActivity() {

	private val delayMillis : Long  = 2000

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash_screen)

		supportActionBar?.hide()
		motionLayout.startLayoutAnimation()

		Looper.myLooper()?.let { Handler(it).postDelayed({
			startActivity(Intent(this, ListFavoriteActivity::class.java))
			finish()
		}, delayMillis) }

	}
}*/

@Composable
fun SplashScreen(
	navigateToHome: () -> Unit
) {

	LaunchedEffect(key1 = Unit, block = {
		delay(1000L)
		navigateToHome()
	})

	Box(modifier = Modifier.fillMaxSize()) {
		Image(
			painter = painterResource(id = R.drawable.github_logo),
			contentDescription = "github logo",
			modifier = Modifier
				.size(120.dp)
				.align(Alignment.Center)
		)
	}
}