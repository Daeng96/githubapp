package com.dicoding.submission.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.RawValue

class User {
    val items: @RawValue List<ItemUser>? = null
}

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