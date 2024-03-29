package com.arteneta.githubapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Favorites::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {

	abstract fun listUsersDao(): FavoritesDao

	companion object {
		@Volatile
		private var INSTANCE: FavoriteDatabase? = null

		fun getDatabase(context: Context): FavoriteDatabase {
			val temp = INSTANCE
			if (temp != null) {
				return temp
			}
			synchronized(this) {
				val instance =
					Room.databaseBuilder(
						context,
						FavoriteDatabase::class.java,
						"listfavorite.db"
					)
						.fallbackToDestructiveMigration()
						.build()
				INSTANCE = instance
				return instance
			}

		}
	}
}