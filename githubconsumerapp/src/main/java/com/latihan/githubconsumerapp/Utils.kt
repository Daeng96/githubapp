package com.latihan.githubconsumerapp

import android.net.Uri

object Utils {

	private const val AUTHORITY = "com.arteneta.githubapp"
	private const val TABLE_NAME = "favorite"

	val CONTENT_URI: Uri = Uri.Builder().scheme("content")
		.authority(AUTHORITY)
		.appendPath(TABLE_NAME)
		.build()

	fun String.fixArgs() = replace("/", "\\")
	fun String.fixUri() = replace("\\", "/")

}