package com.dicoding.submission.net

import com.dicoding.submission.model.DetailUser
import com.dicoding.submission.model.Followers
import com.dicoding.submission.model.Following
import com.dicoding.submission.model.User
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class Services {
    private val urlBase = "https://api.github.com"
    private var gitInterface: EndpointInterface

    init {
        gitInterface = createService(retrofitInstance())
    }

    private fun retrofitInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createService(retrofit: Retrofit): EndpointInterface{
        return retrofit.create(EndpointInterface::class.java)
    }

    fun getUsers(userName: String): Observable<User> {
        return gitInterface.getUsers(userName)
    }

    fun getDetailUser(userName: String): Observable<DetailUser> {
        return gitInterface.getDetailUser(userName)
    }

    fun getFollowerList(userName: String): Observable<ArrayList<Followers>> {
        return gitInterface.getFollowerList(userName)
    }

    fun getFollowingList(userName: String): Observable<ArrayList<Following>> {
        return gitInterface.getFollowingList(userName)
    }
}