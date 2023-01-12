package com.arteneta.githubapp.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Immutable
@Parcelize
data class User(
    val items: @RawValue List<ItemUser>? = null
) : Parcelable

@Immutable
data class ItemUser(
    @SerializedName("login")
    @Expose
    var login: String = "",

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String? = null,

    @SerializedName("score")
    @Expose
    var score: Double? = null
)