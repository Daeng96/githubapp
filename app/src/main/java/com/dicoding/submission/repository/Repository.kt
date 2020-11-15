package com.dicoding.submission.repository

import androidx.lifecycle.LiveData
import com.dicoding.submission.db.Favorites
import com.dicoding.submission.db.FavoritesDao

class Repository(private val favoritesDao: FavoritesDao) {

    val listUsers: LiveData<List<Favorites>> = favoritesDao.getListUsers()

    suspend fun insertAll(favorites: Favorites) {
        favoritesDao.insertAll(favorites)
    }

    suspend fun unFavorite(favorites: Favorites) {
        favoritesDao.unFavorite(favorites)
    }

    fun isExists(login: String?) : LiveData<List<Favorites>> = favoritesDao.isExist(login)

}