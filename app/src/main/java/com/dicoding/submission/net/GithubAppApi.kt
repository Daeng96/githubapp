package com.dicoding.submission.net

import com.dicoding.submission.model.DetailUser
import com.dicoding.submission.model.Followers
import com.dicoding.submission.model.Following
import com.dicoding.submission.model.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubAppApi {

    @GET("/search/users")
    //@Headers("Authorization: token 0984861041fe39f8ec29cce522bbf03d09f69190")
    fun searchUserByUserName(@Query("q") username: String): Observable<User>

    @GET("users/{user_name}")
    //@Headers("Authorization: token 0984861041fe39f8ec29cce522bbf03d09f69190")
    fun getDetailUser(@Path("user_name") username: String): Observable<DetailUser>

    @GET("users/{user_name}/followers")
    //@Headers("Authorization: token 0984861041fe39f8ec29cce522bbf03d09f69190")
    fun getFollowerList(@Path("user_name") username: String): Observable<ArrayList<Followers>>

    @GET("users/{user_name}/following")
    //@Headers("Authorization: token 0984861041fe39f8ec29cce522bbf03d09f69190")
    fun getFollowingList(@Path("user_name") username: String): Observable<ArrayList<Following>>

}
