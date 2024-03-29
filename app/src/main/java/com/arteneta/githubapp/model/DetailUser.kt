package com.arteneta.githubapp.model

import androidx.compose.runtime.Immutable
import com.arteneta.githubapp.db.Favorites
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Immutable
data class DetailUser (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("login")
    @Expose
    var login: String = "",

    @SerializedName("avatar_url")
    @Expose
    var avatarUrl: String = "",

    @SerializedName("html_url")
    @Expose
    var htmlUrl: String = "",

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("company")
    @Expose
    var company: String? = null,

    @SerializedName("location")
    @Expose
    var location: String? = null,

    @SerializedName("public_repos")
    @Expose
    var publicRepos: Int? = null,

    @SerializedName("followers")
    @Expose
    var followers: Int? = null,

    @SerializedName("following")
    @Expose
    var following: Int? = null

) {
    fun asFavorites () = Favorites(
        _id = id!!.toLong(),
        login = login,
        avatarUrl = avatarUrl,
        htmlUrl, name, company, location, publicRepos, followers, following
    )
}