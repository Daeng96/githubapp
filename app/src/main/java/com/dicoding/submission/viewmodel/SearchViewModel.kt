package com.dicoding.submission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submission.model.User
import com.dicoding.submission.net.Services
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel : ViewModel() {

    private val gitHubFactory: Services = Services()
    private val compositeDisposable = CompositeDisposable()

    private val _errorConnection = MutableLiveData<String>()
    val  errorConnection : LiveData<String>
        get() {
            return _errorConnection
        }

    private val _notFound = MutableLiveData<String>()
    val notFound : LiveData<String>
        get() {
            return _notFound
        }
    private val _searchUsers = MutableLiveData<User>()
    val searchUsers : LiveData<User>
        get() {
            return _searchUsers
        }


    //get users
    private fun getUsers(userName: String): Observable<User> {
        return gitHubFactory.getUsers(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
    //set user
    fun setUser(userName: String) {
        compositeDisposable.add(
            getUsers(userName).subscribe({ users ->
                if (users.items.isNullOrEmpty()) {
                    _notFound.postValue(userName)
                } else {
                    _searchUsers.postValue(users)}
            },{ throwable ->
                _errorConnection.postValue(throwable.localizedMessage)
                Log.e("ERROR", throwable.message.toString())}
            )
        )
    }
}