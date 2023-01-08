package com.dicoding.submission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submission.model.ItemUser
import com.dicoding.submission.model.User
import com.dicoding.submission.repository.NetRepository
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

	private val _searchResult = MutableLiveData<SearchResult?>()
	val searchUsersResult: LiveData<SearchResult?> = _searchResult


	//get users
	private fun getUsers(userName: String): Observable<User> {
		return netRepository.searchUser(userName)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	//set user
	fun setUser(userName: String) {
		_searchResult.postValue(SearchResult.OnSearch)
		compositeDisposable.add(
			getUsers(userName).subscribe({ users ->
				if (users.items.isNullOrEmpty()) {
					_searchResult.postValue(SearchResult.OnError("User Not Found"))
				} else {
					_searchResult.postValue(SearchResult.OnSuccess(users.items))
				}
			}, { throwable ->
				_searchResult.postValue(SearchResult.OnError(throwable.message.toString()))
			}
			)
		)
	}
}

sealed class SearchResult {
	object OnSearch : SearchResult()
	data class OnSuccess(val users: List<ItemUser>) : SearchResult()
	data class OnError(val errorMessage: String) : SearchResult()
}