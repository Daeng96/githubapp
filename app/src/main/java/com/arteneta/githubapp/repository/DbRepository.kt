package com.arteneta.githubapp.repository

import androidx.lifecycle.LiveData
import com.arteneta.githubapp.db.Favorites
import com.arteneta.githubapp.db.FavoritesDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbRepository @Inject constructor(
	private val favoritesDao: FavoritesDao
) {

	val listUsers: LiveData<List<Favorites>> = favoritesDao.getListUsers()
	fun getUserByLogin(login: String) = favoritesDao.getUserByLogin(login)
	suspend fun insertAll(favorites: Favorites)  { favoritesDao.insertAll(favorites) }
	suspend fun unFavorite(favorites: Favorites) { favoritesDao.unFavorite(favorites) }
	fun isExists(login: String): LiveData<List<Favorites>> = favoritesDao.isExist(login)

}