package com.dicoding.submission.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.dicoding.submission.db.FavoriteDatabase

class MyContentProvider : ContentProvider() {

	private lateinit var favoriteDatabase: FavoriteDatabase

	companion object {
		const val CODE_ALL_DATA = 1
		const val CODE_DATA_ID = 2
		val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

		private const val AUTHORITY = "com.dicoding.submission"
		private const val TABLE_NAME = "favorite"

		val CONTENT_URI : Uri = Uri.Builder().scheme("content")
			.authority(AUTHORITY)
			.appendPath(TABLE_NAME)
			.build()


		init {
			uriMatcher.addURI(AUTHORITY, TABLE_NAME, CODE_ALL_DATA)
			uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", CODE_DATA_ID)
		}
	}

	override fun onCreate(): Boolean {
		favoriteDatabase = FavoriteDatabase.getDatabase(context as Context)
		favoriteDatabase.isOpen
		return true
	}

	override fun query(
		uri: Uri, projection: Array<String>?, selection: String?,
		selectionArgs: Array<String>?, sortOrder: String?
	): Cursor? {
		return when(uriMatcher.match(uri)) {
			CODE_ALL_DATA -> favoriteDatabase.listUsersDao().cursorDB()
			else -> null
		}
	}
	override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
		val d : Int = when( CODE_DATA_ID){
			uriMatcher.match(p0) -> favoriteDatabase.listUsersDao().deleteAll(p0.lastPathSegment?.toLong())
			else -> 0
		}
		context?.contentResolver?.notifyChange(CONTENT_URI, null)
		return d
	}


	override fun getType(uri: Uri): String? {
		return null
	}

	override fun insert(uri: Uri, values: ContentValues?): Uri? {
		return null
	}




	override fun update(
		uri: Uri, values: ContentValues?, selection: String?,
		selectionArgs: Array<String>?
	): Int {
		return 0
	}
}