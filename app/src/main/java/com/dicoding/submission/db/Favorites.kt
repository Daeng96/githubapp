package com.dicoding.submission.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@kotlinx.parcelize.Parcelize
@Entity(tableName = "favorite")
data class Favorites (
    @PrimaryKey
    @ColumnInfo(name = "_ID")var _id: Int? = 0,
    @ColumnInfo(name = "Login")var login: String = "",
    @ColumnInfo(name = "Avatar Url")var avatarUrl: String = "",
    @ColumnInfo(name = "HTML Url")var htmlUrl: String? = "",
    @ColumnInfo(name = "Full Name")var name: String? = "",
    @ColumnInfo(name = "Company")var company: String? = "",
    @ColumnInfo(name = "Location")var location: String? = "",
    @ColumnInfo(name = "DbRepository")var publicRepos: Int? = 0,
    @ColumnInfo(name = "Followers")var followers: Int? = 0,
    @ColumnInfo(name = "Following")var following: Int? = 0
)  : Parcelable
