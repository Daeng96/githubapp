package com.dicoding.submission.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val DarkColorScheme = darkColorScheme(
	primary = Color(0xFFFF5722),
	onPrimary = Color(0xFFCDC3DE),
	secondary = Color(0xFF011B19),
	secondaryContainer = Color(0xFF764702),
	tertiary = Color(0xFFFABC62),
	tertiaryContainer = Color(0xFF885101),
	background =  Color(0xFF0D1010),
	inversePrimary = GreenLight,
	primaryContainer = Color(0xFF491605),
	onPrimaryContainer = Color(0xFFFF5722),
	surface = Color(0xFF101313),
	surfaceVariant = Color(0xFF222222),
	onSurfaceVariant = Color(0xFFFCFCFC)
)

private val LightColorScheme = lightColorScheme(
	background = Color(0xFFF5F7F8),
	onBackground = Color.Black,
	primary = Color(0xFF03A9F4),
	onPrimary = Color(0xFFE3EFF4),

	secondary = Color(0xFF2196F3),
	secondaryContainer = Color(0xFFC2DFF6),

	primaryContainer = Color(0xFFC2DFF6),
	onPrimaryContainer = Color(0xFF03A9F4),
	inversePrimary = GreenLight,

	tertiary = CreamDark,
	tertiaryContainer = Cream,

	surfaceVariant = Color(0xFFEFEFEF),
	onSurfaceVariant = Color(0xFF03A9F4),
	surface = Color(0xFFF9FBFC),
	onSurface = Color.Black,
	surfaceTint = Color.Black,


	/* Other default colors to override
	background = Color(0xFFFFFBFE),
	surface = Color(0xFFFFFBFE),
	onPrimary = Color.White,
	onSecondary = Color.White,
	onTertiary = Color.White,
	onBackground = Color(0xFF1C1B1F),
	onSurface = Color(0xFF1C1B1F),
	*/
)

@Composable
fun GithubMaterialTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	content: @Composable () -> Unit
) {

	val uiController = rememberSystemUiController()

	val colors = if (!darkTheme) {
		uiController.setUi(darkIcon = true, true)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			val context = LocalContext.current
			dynamicLightColorScheme(context)
		} else {
			LightColorScheme
		}
	} else {
		uiController.setUi(darkIcon = false, false)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			val context = LocalContext.current
			dynamicDarkColorScheme(context)
		} else {
			DarkColorScheme
		}
	}

	MaterialTheme(
		colorScheme = colors ,
		shapes = Shapes(),
		typography = Typography
	) {
		content()
	}
}

private fun SystemUiController.setUi(
	darkIcon: Boolean = false,
	navDarkIcon: Boolean = true
) {
	setNavigationBarColor(Color.Transparent, navDarkIcon)
	setStatusBarColor(Color.Transparent, darkIcon)
}