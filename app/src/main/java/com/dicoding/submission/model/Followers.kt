package com.dicoding.submission.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Followers {
    @SerializedName("login")
    @Expose
    var login: String? = null

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String? = null
}