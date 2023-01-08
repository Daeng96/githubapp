package com.dicoding.submission.model

import androidx.compose.runtime.Immutable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Immutable
class DetailUser {
    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("login")
    @Expose
    var login: String = ""

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String = ""

    @SerializedName("html_url")
    @Expose
    var htmlUrl: String = ""

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("company")
    @Expose
    var company: String? = null

    @SerializedName("location")
    @Expose
    var location: String? = null

    @SerializedName("public_repos")
    @Expose
    var publicRepos: Int? = null

    @SerializedName("followers")
    @Expose
    var followers: Int? = null

    @SerializedName("following")
    @Expose
    var following: Int? = null

}