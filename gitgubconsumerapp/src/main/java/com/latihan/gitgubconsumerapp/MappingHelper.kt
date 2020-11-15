package com.latihan.gitgubconsumerapp

import android.database.Cursor

object MappingHelper {
	fun mapCursor(cursor: Cursor? ) :  ArrayList<Favorites> {
		val fvList = ArrayList<Favorites>()
		cursor?.apply {
			while (moveToNext()){
				val id = getInt(getColumnIndexOrThrow("_ID"))
				val login = getString(getColumnIndexOrThrow("Login"))
				val avatarUrl = getString(getColumnIndexOrThrow("Avatar Url"))
				val fullName = getString(getColumnIndexOrThrow("Full Name"))
				val company = getString(getColumnIndexOrThrow("Company"))
				val location = getString(getColumnIndexOrThrow("Location"))
				val publicRepos = getInt(getColumnIndexOrThrow("Repository"))
				val followers = getInt(getColumnIndexOrThrow("Followers"))
				val following = getInt(getColumnIndexOrThrow("Following"))
				val htmlUrl = getString(getColumnIndexOrThrow("HTML Url"))

				fvList.add(Favorites(id, login, avatarUrl,fullName, company,location, publicRepos,
					followers, following, htmlUrl))
			}
		}
		return fvList
	}
}