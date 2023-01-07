package com.dicoding.submission.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val DarkColorScheme = darkColorScheme(
	primary = GreenDark80,
	onPrimary = GreenLight,
	secondary = Color(0xFF011B19),
	secondaryContainer = Color(0xFF02100F), // ayat 2


	tertiary = Color(0xFFCFDAD9),
	tertiaryContainer = Color(0xFF323232),

	inversePrimary = GreenLight,
	primaryContainer = GreenDark100,
	onPrimaryContainer = GreenLight,

	surface = Color(0xFF011B19) // ayat 1
)

private val LightColorScheme = lightColorScheme(
	background = CreamLight,
	onBackground = Color.Black,
	primary = GreenDark,
	onPrimary = GreenLight100,

	secondary = Beige,
	secondaryContainer = Cream, // ayat 2

	primaryContainer = GreenDark100,
	onPrimaryContainer = GreenLight,
	inversePrimary = GreenLight,

	tertiary = CreamDark,
	tertiaryContainer = Cream,

	surfaceVariant = Color.White,
	onSurfaceVariant = Color.DarkGray,
	surface = Beige, // ayat 1
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

private val SpecialLightColorScheme = lightColorScheme(
	background = bgSE,
	onBackground = onBgSE,
	primary = primarySE,
	onPrimary = onPrimarySE,

	secondary = ayatBgSE,
	secondaryContainer = ayat1BgSE, // ayat 2

	primaryContainer = primaryContainerSE,
	onPrimaryContainer = onPrimaryContainerSE,
	inversePrimary = GreenLight,

	tertiary = CreamDark,
	tertiaryContainer = Cream,

	surfaceVariant = surfaceVariantSE,
	onSurfaceVariant = onSurfaceVariantSE,
	surface = surfaceSE, // ayat 1
	onSurface = onSurfaceSE,
	surfaceTint = onBgSE
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

enum class Theme(val icon: ImageVector, val price: Double? = null) {
	LIGHT(Icons.Default.LightMode),
	DARK(Icons.Default.DarkMode),
	SPECIAL(Icons.Outlined.Favorite, 0.64),
	DYNAMIC_DARK(Icons.Default.DarkMode),
	DYNAMIC_LIGHT(Icons.Default.LightMode),
	SYSTEM(Icons.Default.PhoneAndroid)
}

@Composable
fun GithubMaterialTheme(
	darkTheme: Boolean = isSystemInDarkTheme(),
	themeMode: Theme,
	content: @Composable () -> Unit
) {

	val uiController = rememberSystemUiController()
	val appColorScheme = when (themeMode) {
		Theme.LIGHT -> {
			uiController.setUi()
			LightColorScheme
		}
		Theme.DARK -> {
			uiController.setUi(navDarkIcon = false)
			DarkColorScheme
		}
		Theme.DYNAMIC_LIGHT -> {
			uiController.setUi(true)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				val context = LocalContext.current
				dynamicLightColorScheme(context)
			} else {
				LightColorScheme
			}
		}
		Theme.DYNAMIC_DARK -> {
			uiController.setUi()
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				val context = LocalContext.current
				dynamicDarkColorScheme(context)
			} else {
				DarkColorScheme
			}
		}

		Theme.SPECIAL -> {
			uiController.setUi()
			SpecialLightColorScheme
		}

		Theme.SYSTEM -> {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				uiController.setUi(!darkTheme, navDarkIcon = !darkTheme)
				val context = LocalContext.current
				if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
			} else {
				uiController.setUi(navDarkIcon = !darkTheme)
				if (darkTheme) DarkColorScheme else LightColorScheme
			}
		}
	}


	MaterialTheme(
		colorScheme = appColorScheme ,
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