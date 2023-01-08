package com.dicoding.submission.repository

import com.dicoding.submission.net.GithubAppApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetRepository @Inject constructor(
	private val githubAppApi: GithubAppApi
) {
	fun searchUser(userName: String) = githubAppApi.searchUserByUserName(userName)
	fun getDetailUserByLogin(login: String) = githubAppApi.getDetailUser(login)
	fun getFollower(login: String) = githubAppApi.getFollowerList(login)
	fun getFollowing(login: String) = githubAppApi.getFollowingList(login)
}