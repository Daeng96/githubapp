package com.latihan.gitgubconsumerapp

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorites(
    var _ID: Int,
    var Login: String,
    var avatarUrl: String,
    val name: String?,
    val company: String?,
    val location: String?,
    val publicRepos: Int?,
    val followers: Int?,
    val following: Int?,
    val htmlUrl : String
) : Parcelable

