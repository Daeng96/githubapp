package com.dicoding.submission.db

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

        @Query("SELECT * FROM favorite ORDER BY Login ASC")
        fun getListUsers(): LiveData<List<Favorites>>

        @Query("SELECT * FROM favorite WHERE Login =:login")
        fun getUserByLogin(login: String) : Flow<Favorites>


        @Query("SELECT * FROM favorite WHERE _ID =:id")
        fun getUserById(id: Long?) : Cursor

        @Query("SELECT * FROM favorite")
        fun cursorDB(): Cursor

        @Query("SELECT * FROM favorite WHERE Login = :login")
        fun isExist(login : String) : LiveData<List<Favorites>>

        @Query("DELETE  FROM favorite WHERE _ID =:id")
        fun deleteAll(id : Long?): Int

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertAll(data: Favorites)

        @Delete
        suspend fun unFavorite(data: Favorites)
}
