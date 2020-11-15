package com.dicoding.submission.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.submission.db.FavoriteDatabase
import com.dicoding.submission.db.Favorites
import com.dicoding.submission.model.DetailUser
import com.dicoding.submission.net.Services
import com.dicoding.submission.repository.Repository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailUsersViewModel(application: Application) : AndroidViewModel(application) {

    private var gitHubFactory: Services = Services()
    private val compositeDisposable = CompositeDisposable()

    private val _detailUser = MutableLiveData<DetailUser>()
    val detailUser : LiveData<DetailUser>
        get() {
           return _detailUser
        }

    private val repository: Repository
    val allUsers: LiveData<List<Favorites>>


    init {
        val listDao = FavoriteDatabase.getDatabase(application).listUsersDao()
        repository = Repository(listDao)
        allUsers = repository.listUsers
    }


    fun isExists (login: String?) : LiveData<List<Favorites>> {
      return repository.isExists(login)
    }

    fun insertAll(favorite: Favorites) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertAll(favorite)
    }

    fun unFavorite(data: Favorites) = viewModelScope.launch(Dispatchers.IO) {
        repository.unFavorite(data)
    }


    fun setDetailUsers(userName: String) {
        compositeDisposable.add(getDetailUser(userName).subscribe(
            {
                _detailUser.postValue(it)
            }, {
                throwable -> Log.e("error detail", throwable.message.toString())}
            )
        )
    }

    private fun getDetailUser(username: String): Observable<DetailUser> {
        return gitHubFactory.getDetailUser(username)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}
