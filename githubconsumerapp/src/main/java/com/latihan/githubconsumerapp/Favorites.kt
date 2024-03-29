package com.latihan.githubconsumerapp

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
data class Favorites(
	var _ID: Long = 0,
	var login: String = "",
	var avatarUrl: String = "",
	val name: String? = "",
	val company: String? = "",
	val location: String? = "",
	val publicRepos: Int? = 0,
	val followers: Int? = 0,
	val following: Int? = 0,
	val htmlUrl: String = ""
) : Parcelable

