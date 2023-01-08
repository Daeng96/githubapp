package com.dicoding.submission.di

import android.content.Context
import com.dicoding.submission.db.FavoriteDatabase
import com.dicoding.submission.db.FavoritesDao
import com.dicoding.submission.net.GithubAppApi
import com.dicoding.submission.net.NetServices.BASE_URL
import com.dicoding.submission.repository.DbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

	@Provides
	fun providesFavoriteDatabase(@ApplicationContext context: Context) =
		FavoriteDatabase.getDatabase(context)

	@Singleton
	@Provides
	fun providesFavoriteDao(favoriteDatabase: FavoriteDatabase): FavoritesDao = favoriteDatabase.listUsersDao()

	@Singleton
	@Provides
	fun providesDbRepository(favoritesDao: FavoritesDao): DbRepository = DbRepository(favoritesDao)

	@Provides
	@Singleton
	fun provideRetrofit(): Retrofit {
		val loggingInterceptor =
			HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
		val client = OkHttpClient.Builder()
			.addInterceptor(loggingInterceptor)
			.build()

		return Retrofit
			.Builder()
			.client(client)
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			//.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	fun githubApi(retrofit: Retrofit): GithubAppApi =
		retrofit.create(GithubAppApi::class.java)
}