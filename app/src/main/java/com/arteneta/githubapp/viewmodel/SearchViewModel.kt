package com.arteneta.githubapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arteneta.githubapp.model.User
import com.arteneta.githubapp.repository.NetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
	private val netRepository: NetRepository
) : ViewModel() {

	private val compositeDisposable = CompositeDisposable()

	private val _searchResult = MutableLiveData<RequestResult<User>?>(null)
	val searchUsersResult: LiveData<RequestResult<User>?> = _searchResult


	//get users
	private fun getUsers(userName: String): Observable<User> {
		return netRepository.searchUser(userName)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	//set user
	fun setUser(userName: String) {
		_searchResult.postValue(RequestResult.Progress)
		compositeDisposable.add(
			getUsers(userName).subscribe({ users ->
				if (users.items.isNullOrEmpty()) {
					_searchResult.postValue(RequestResult.Error("user not found"))
				} else {
					_searchResult.postValue(RequestResult.Success(users))
				}
			}, { throwable ->
				_searchResult.postValue(RequestResult.Error(throwable.message.toString()))
			}
			)
		)
	}
}
