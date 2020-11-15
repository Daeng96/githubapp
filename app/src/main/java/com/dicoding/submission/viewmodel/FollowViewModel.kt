package com.dicoding.submission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submission.model.Followers
import com.dicoding.submission.model.Following
import com.dicoding.submission.net.Services
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FollowViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val services : Services = Services()

    private val _listFollowing = MutableLiveData<ArrayList<Following>>()
    val listFollowing : LiveData<ArrayList<Following>>
        get() {
            return _listFollowing
        }

    private val _listFollowers = MutableLiveData<ArrayList<Followers>>()
    val listFollowers : LiveData<ArrayList<Followers>>
        get() {
            return _listFollowers
        }

    private val _errorToast = MutableLiveData<String>()
    val errorToast : LiveData<String>
        get() {
            return _errorToast
        }

    private val _errorFlToast = MutableLiveData<String>()
    val errorFollowing : LiveData<String>
        get() {
            return _errorFlToast
        }


    fun setFollower(userName: String) {
        compositeDisposable.add(getFollowerList(userName).subscribe ({ follower ->
            _listFollowers.postValue(follower)},
            {throwable ->
            _errorToast.postValue(throwable.message)
            Log.e("Error Follower", throwable.message.toString())}))
    }

    private fun getFollowerList(userName: String): Observable<ArrayList<Followers>> {
        return services.getFollowerList(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun setFollowing(userName: String) {
        compositeDisposable.add(getFollowingList(userName).subscribe ({ follow ->
            _listFollowing.postValue(follow)},
            { throwable ->
            _errorFlToast.postValue(throwable.message)
            Log.e("Error Following", throwable.message.toString())
        }))
    }

    private fun getFollowingList(userName: String): Observable<ArrayList<Following>> {
        return services.getFollowingList(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}