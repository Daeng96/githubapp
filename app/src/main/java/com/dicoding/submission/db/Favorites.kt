package com.dicoding.submission.db

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "favorite")
data class Favorites (
    @PrimaryKey
    @ColumnInfo(name = "_ID")var _id: Int?,
    @ColumnInfo(name = "Login")var login: String?,
    @ColumnInfo(name = "Avatar Url")var avatarUrl: String?,
    @ColumnInfo(name = "HTML Url")var htmlUrl: String?,
    @ColumnInfo(name = "Full Name")var name: String?,
    @ColumnInfo(name = "Company")var company: String?,
    @ColumnInfo(name = "Location")var location: String?,
    @ColumnInfo(name = "Repository")var publicRepos: Int?,
    @ColumnInfo(name = "Followers")var followers: Int?,
    @ColumnInfo(name = "Following")var following: Int?
)  : Parcelable
