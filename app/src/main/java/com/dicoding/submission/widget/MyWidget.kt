package com.dicoding.submission.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.TaskStackBuilder
import com.dicoding.submission.R
import com.dicoding.submission.view.MainActivity

class MyWidget : AppWidgetProvider() {

	override fun onUpdate(
		context: Context,
		appWidgetManager: AppWidgetManager,
		appWidgetIds: IntArray
	) {
		// There may be multiple widgets active, so update all of them
		for (appWidgetId in appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetId)
		}
	}

	override fun onEnabled(context: Context) {
		super.onEnabled(context)
		Log.d("Widget", "onEnabled")
	}
	companion object{
		fun sendBroadcast(context: Context){
			val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
			intent.component = ComponentName(context, MyWidget::class.java)
			context.sendBroadcast(intent)
		}
	}
	override fun onReceive(context: Context, intent: Intent?) {
		val action : String? = intent?.action
		if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
			val mgr : AppWidgetManager = AppWidgetManager.getInstance(context)
			val cn = ComponentName(context, MyWidget::class.java)
			mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.list_view)
		}
		super.onReceive(context, intent)
	}
	override fun onDisabled(context: Context) {
		Log.d("Widget", "onDisable")
	}

	override fun onAppWidgetOptionsChanged(
		context: Context?,
		appWidgetManager: AppWidgetManager?,
		appWidgetId: Int,
		newOptions: Bundle
	) {
		val view = RemoteViews(context?.packageName, R.layout.my_widget)
		val maxWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)

		if (maxWidth in 90..144){
			view.setTextViewText(R.id.title_widget, context?.getString(R.string.favorite))
		} else {
			view.setTextViewText(R.id.title_widget, context?.getString(R.string.list_favorite))
		}

		if(maxWidth <= 84){
			view.setViewVisibility(R.id.title_widget, View.GONE)
		} else {
			view.setViewVisibility(R.id.title_widget, View.VISIBLE)
		}
		appWidgetManager?.updateAppWidget(appWidgetId, view)
	}


}

internal fun updateAppWidget(
	context: Context,
	appWidgetManager: AppWidgetManager,
	appWidgetId: Int
) {
	val views = RemoteViews(context.packageName, R.layout.my_widget)
	val intent = Intent(context, MyRemoteViewsServices::class.java)
	views.setRemoteAdapter(R.id.list_view, intent)

	val listFav = Intent(context, MainActivity::class.java)
//	val listFav = Intent(context, ListFavoriteActivity::class.java)
	val listFavPending = PendingIntent.getActivity(context, 0, listFav, 0)
	views.setOnClickPendingIntent(R.id.arrow_to_list, listFavPending)

//	val detail = Intent(context, DetailUserActivity::class.java)
	val detail = Intent(context, MainActivity::class.java)
	val clickTemplate = TaskStackBuilder.create(context)
		.addNextIntentWithParentStack(detail)
		.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

	views.setPendingIntentTemplate(R.id.list_view, clickTemplate)
	appWidgetManager.updateAppWidget(appWidgetId, views)
}