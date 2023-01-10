package com.latihan.gitgubconsumerapp

import android.database.Cursor

object MappingHelper {


	fun mapCursor(cursor: Cursor?): ArrayList<Favorites> {
		val fvList = ArrayList<Favorites>()
		cursor?.apply {
			while (moveToNext()) {
				val id = getInt(getColumnIndexOrThrow("_ID"))
				val login = getString(getColumnIndexOrThrow("login"))
				val avatarUrl = getString(getColumnIndexOrThrow("Avatar Url"))
				val fullName = getString(getColumnIndexOrThrow("Full Name"))
				val company = getString(getColumnIndexOrThrow("Company"))
				val location = getString(getColumnIndexOrThrow("Location"))
				val publicRepos = getInt(getColumnIndexOrThrow("DbRepository"))
				val followers = getInt(getColumnIndexOrThrow("Followers"))
				val following = getInt(getColumnIndexOrThrow("Following"))
				val htmlUrl = getString(getColumnIndexOrThrow("HTML Url"))

				fvList.add(
					Favorites(
						id, login, avatarUrl, fullName, company, location, publicRepos,
						followers, following, htmlUrl
					)
				)
			}
		}
		return fvList
	}

	fun getUserById(cursor: Cursor?): Favorites {
		var favorites = Favorites()
		cursor?.apply {
			if (moveToFirst()) {
				val id = getInt(getColumnIndexOrThrow("_ID"))
				val login = getString(getColumnIndexOrThrow("login"))
				val avatarUrl = getString(getColumnIndexOrThrow("Avatar Url"))
				val fullName = getString(getColumnIndexOrThrow("Full Name"))
				val company = getString(getColumnIndexOrThrow("Company"))
				val location = getString(getColumnIndexOrThrow("Location"))
				val publicRepos = getInt(getColumnIndexOrThrow("DbRepository"))
				val followers = getInt(getColumnIndexOrThrow("Followers"))
				val following = getInt(getColumnIndexOrThrow("Following"))
				val htmlUrl = getString(getColumnIndexOrThrow("HTML Url"))
				favorites = Favorites(
					id, login, avatarUrl, fullName, company, location, publicRepos,
					followers, following, htmlUrl
				)
			}

		}
		return favorites
	}
}