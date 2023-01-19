package com.arteneta.githubapp.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeAppWidgetReceiver : GlanceAppWidgetReceiver() {

	override val glanceAppWidget: GlanceAppWidget
		get() = ComposeAppWidget()

	override fun onDisabled(context: Context) {
		super.onDisabled(context)
		WidgetWorker.cancel(context)
	}

	override fun onEnabled(context: Context) {
		super.onEnabled(context)
		WidgetWorker.enqueueUniqueWork(context, false)
	}

}