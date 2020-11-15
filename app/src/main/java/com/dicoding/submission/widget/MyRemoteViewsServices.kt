package com.dicoding.submission.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Binder
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submission.R
import com.dicoding.submission.view.DetailUserActivity

class MyRemoteViewsServices : RemoteViewsService(){
	override fun onGetViewFactory(p0: Intent): RemoteViewsFactory {
		return MyView(p0,this.applicationContext)
	}

	companion object {
		private const val AUTHORITY = "com.dicoding.submission"
		private const val TABLE_NAME = "favorite"

		val CONTENT_URI: Uri = Uri.Builder().scheme("content")
			.authority(AUTHORITY)
			.appendPath(TABLE_NAME)
			.build()

		const val ID = "_id"
		const val Login = "login"
		const val AvatarUrl = "avatarUrl"

	}


	internal inner class MyView(intent: Intent,context: Context) :
		RemoteViewsFactory {
		private val myIntent : Intent = intent
		private val myContext : Context = context
		private lateinit var cursor : Cursor


		override fun onCreate() {
			Log.d("widget", "onCreate services")
		}

		@SuppressLint("Recycle")
		override fun onDataSetChanged() {
			val identityToken : Long = Binder.clearCallingIdentity()
			cursor = myContext.contentResolver.query(CONTENT_URI, arrayOf(ID, Login, AvatarUrl), null,
			null, null) as Cursor
			Binder.restoreCallingIdentity(identityToken)
		}



		override fun getCount(): Int {
			return cursor.count
		}

		override fun getViewAt(position: Int): RemoteViews? {

			if (position == AdapterView.INVALID_POSITION || !cursor.moveToPosition(position)) {
				return null
			}
			val rv = RemoteViews(myContext.packageName, R.layout.item_list_widget)

			val url = cursor.getString(2)

			val first = cursor.getString(1)
			val firstChart = first[0]
			val builder = Glide.with(myContext)
				.asBitmap()
				.load(url)
				.apply(RequestOptions.circleCropTransform())
				.error(ColorDrawable(Color.GRAY))
				.submit()

			try {
				viewVisibility(true, rv, String())
				rv.setImageViewBitmap(R.id.img_widget, builder.get() as Bitmap)
			} catch (e: Exception) {
				e.printStackTrace()
				viewVisibility(false, rv, firstChart.toString())
			}

			rv.setTextViewText(R.id.text_widget, cursor.getString(1))

			myIntent.putExtra(DetailUserActivity.EXTRA_PERSON, cursor.getString(1))
			rv.setOnClickFillInIntent(R.id.container_list, myIntent)

			return rv
		}

		private fun viewVisibility(
			v: Boolean, rv: RemoteViews,
			firstChart: String
		) {
			if (v) {
				rv.setViewVisibility(R.id.img_widget, View.VISIBLE)
				rv.setViewVisibility(R.id.first_chart, View.GONE)
			} else {
				rv.setViewVisibility(R.id.img_widget, View.GONE)
				rv.setViewVisibility(R.id.first_chart, View.VISIBLE)
				rv.setTextViewText(R.id.first_chart, firstChart)
			}
		}

		override fun getLoadingView(): RemoteViews? {
			return null
		}

		override fun getViewTypeCount(): Int {
			return 1
		}

		override fun getItemId(p0: Int): Long {
			return cursor.getLong(cursor.getColumnIndexOrThrow("_ID"))
		}

		override fun hasStableIds(): Boolean {
			return true
		}
		override fun onDestroy() {
			cursor.close()
		}
	}
}
