package com.arteneta.githubapp.widget

import android.content.Intent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.glance.*
import androidx.glance.action.clickable
import androidx.glance.appwidget.*
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.itemsIndexed
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight.Companion.Bold
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.arteneta.githubapp.R
import com.arteneta.githubapp.model.WidgetStateData

class ComposeAppWidget : GlanceAppWidget() {

	companion object {
		val columnMode = DpSize(120.dp, 184.dp)
		val lazyColumnMode = DpSize(120.dp, 120.dp)
	}

	override val stateDefinition: GlanceStateDefinition<*>
		get() = WidgetDataDefinition

	override val sizeMode: SizeMode
		get() = SizeMode.Responsive(setOf(columnMode, lazyColumnMode))

	@Composable
	override fun Content() {
		val context = LocalContext.current
		val state = currentState<WidgetStateData>()
		val sizeState = LocalSize.current

		Box(
			modifier = GlanceModifier
				.fillMaxSize()
				.appWidgetBackground()
				.background(imageProvider = ImageProvider(R.drawable.background_widget))
				.appWidgetBackgroundCornerRadius(),
			contentAlignment = Alignment.Center
		) {

			when (state) {
				is WidgetStateData.Loading -> { CircularProgressIndicator() }
				is WidgetStateData.Data -> {

					Column(
						modifier = GlanceModifier.fillMaxSize()
					) {

						Header(
							title = context.getString(R.string.list_favorite),
							modifier = GlanceModifier
								.fillMaxWidth()
								.background(imageProvider = ImageProvider(R.drawable.background_title_widget))
								.padding(vertical = 4.dp)
						)

						when (sizeState) {
							columnMode -> {
								ContentColumnWidget(data = state)
							}
							lazyColumnMode -> {
								ContentLazyColumnWidget(data = state)
							}
						}
					}
				}
				is WidgetStateData.Unavailable -> { Text(text = state.message) }
			}
		}
	}
}

@Composable
private fun ContentColumnWidget(
	data: WidgetStateData.Data
) {
	data.list.forEachIndexed { index, it ->
		ListItem(
			user = it.login,
			index = index,
			modifier = GlanceModifier.fillMaxWidth()
				.background(
					color = if (index % 2 == 0) Color.LightGray.copy(0.3f) else Color.DarkGray.copy(
						alpha = 0.3f
					)
				)
				.padding(vertical = 2.dp, horizontal = 8.dp)
				.clickable(
					onClick = actionStartActivity(
						intent = Intent(Intent.ACTION_VIEW).apply {
							this.data =
								"http://www.arteneta.github.app/Home/${it.login}".toUri()
							setPackage("com.arteneta.githubapp")
							flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
						}
					)
				)
		)
	}
}

@Composable
private fun ContentLazyColumnWidget(
	data: WidgetStateData.Data
) {
	// terjadi bug pada lazy column dimana list kadang tidak terupdate meski data sudah berubah
	// tampilan list bisa diganti dengan melakukan perulangan pada daftar data namun ini tidak bisa
	// di scroll
	//Log.i("tag1", data.list.size.toString())
	LazyColumn(
		content = {
			itemsIndexed(data.list) { index, user ->
				ListItem(
					index = index,
					user = user.login,
					modifier = GlanceModifier.fillMaxWidth().background(
						color = if (index % 2 == 0) Color.LightGray.copy(0.3f) else Color.DarkGray.copy(
							alpha = 0.3f
						)
					)
						.padding(vertical = 2.dp, horizontal = 8.dp)
						.clickable(
							onClick = actionStartActivity(
								intent = Intent(Intent.ACTION_VIEW).apply {
									this.data =
										"http://www.arteneta.github.app/Home/${user.login}".toUri()
									setPackage("com.arteneta.githubapp")
									flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
								}
							)
						)
				)
			}
		}
	)

}


@Composable
private fun Header(title: String, modifier: GlanceModifier) {
	Text(
		text = title, maxLines = 1,
		modifier = modifier,
		style = TextStyle(
			fontSize = 18.sp,
			textAlign = TextAlign.Center,
			color = ColorProvider(Color.White)
		)
	)
}

@Composable
private fun ListItem(
	user: String,
	index: Int,
	modifier: GlanceModifier
) {
	Row(modifier) {
		Text(
			text = "${index + 1}. ",
			style = TextStyle(
				textAlign = TextAlign.Start,
				fontSize = 20.sp,
				color = ColorProvider(Color.White),
				fontWeight = Bold
			)
		)
		Text(
			text = user,
			modifier = GlanceModifier.padding(start = 4.dp),
			maxLines = 1,
			style = TextStyle(
				textAlign = TextAlign.Start,
				fontSize = 18.sp,
				color = ColorProvider(Color.White),
				fontWeight = Bold
			)
		)
	}
}

fun GlanceModifier.appWidgetBackgroundCornerRadius(): GlanceModifier {
	if (Build.VERSION.SDK_INT >= 31) {
		cornerRadius(android.R.dimen.system_app_widget_background_radius)
	} else {
		cornerRadius(16.dp)
	}
	return this
}

