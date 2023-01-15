package com.arteneta.githubapp.widget

import android.content.Context
import android.database.Cursor
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.work.*
import com.arteneta.githubapp.db.FavoriteDatabase
import com.arteneta.githubapp.model.WidgetData
import com.arteneta.githubapp.model.WidgetStateData
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class WidgetWorker(
	private val context: Context,
	workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

	override suspend fun doWork(): Result {
		val manager = GlanceAppWidgetManager(context)
		val glanceIds = manager.getGlanceIds(ComposeAppWidget::class.java)
		val db = FavoriteDatabase.getDatabase(applicationContext)
		val dao = db.listUsersDao()

		if (glanceIds.isEmpty()) return Result.failure()

		return try {
			// Update state to indicate loading

			val data = getFavorites(dao.cursorDB()).toList()
			setWidgetState(glanceIds, WidgetStateData.Loading)

			delay(100L)
			// Update state with new data
			setWidgetState(glanceIds, WidgetStateData.Data(data))
			Result.success()
		} catch (e: Exception) {
			setWidgetState(glanceIds, WidgetStateData.Unavailable(e.message.orEmpty()))
			Result.failure()
		}
	}

	/**
	 * Update the state of all widgets and then force update UI
	 */
	private suspend fun setWidgetState(glanceIds: List<GlanceId>, newState: WidgetStateData) {
		glanceIds.forEach { glanceId ->
			updateAppWidgetState(
				context = context,
				definition = WidgetDataDefinition,
				glanceId = glanceId,
				updateState = { newState }
			)
		}

		ComposeAppWidget().updateAll(context)
	}

	companion object {

		private val uniqueWorkName = WidgetWorker::class.java.simpleName

		fun enqueueUniqueWork(context: Context, force: Boolean) {
			val manager = WorkManager.getInstance(context)
			val requestBuilder = OneTimeWorkRequestBuilder<WidgetWorker>()
				.setInitialDelay(2, TimeUnit.SECONDS)

			var workPolicy = ExistingWorkPolicy.KEEP
			if (force) {
				workPolicy = ExistingWorkPolicy.REPLACE
			}
			manager.enqueueUniqueWork(uniqueWorkName, workPolicy, requestBuilder.build())
		}

		/**
		 * Cancel any ongoing worker
		 */
		fun cancel(context: Context) {
			WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName)
		}

		private fun getFavorites(cursor: Cursor?): ArrayList<WidgetData> {
			val widgetData = arrayListOf<WidgetData>()
			cursor?.apply {
				while (moveToNext()) {
					val id = getLong(getColumnIndexOrThrow("_ID"))
					val login = getString(getColumnIndexOrThrow("Login"))
					val avatarUrl = getString(getColumnIndexOrThrow("Avatar Url"))
					widgetData.add(WidgetData(id, login, avatarUrl))
				}
			}
			return widgetData
		}

	}
}