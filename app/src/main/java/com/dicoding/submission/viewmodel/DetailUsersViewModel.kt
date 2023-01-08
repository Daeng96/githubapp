package com.dicoding.submission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submission.db.Favorites
import com.dicoding.submission.model.DetailUser
import com.dicoding.submission.repository.DbRepository
import com.dicoding.submission.repository.NetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailUsersViewModel @Inject constructor(
	private val dbRepository: DbRepository,
	private val netRepository: NetRepository
) : ViewModel() {

	private val compositeDisposable = CompositeDisposable()

	private val _detailUser = MutableLiveData<UserDetailResult>()
	val detailUser: LiveData<UserDetailResult> = _detailUser

	val allUsers: LiveData<List<Favorites>> = dbRepository.listUsers

	fun isExists(login: String?): LiveData<List<Favorites>> {
		return dbRepository.isExists(login)
	}

	fun insertAll(favorite: Favorites) = viewModelScope.launch(Dispatchers.IO) {
		dbRepository.insertAll(favorite)
	}

	fun unFavorite(data: Favorites) = viewModelScope.launch(Dispatchers.IO) {
		dbRepository.unFavorite(data)
	}


	fun setDetailUsers(userName: String) {
		compositeDisposable.add(getDetailUser(userName).subscribe(
			{
				_detailUser.postValue(UserDetailResult.OnSuccess(it))
			}, { throwable -> UserDetailResult.OnError(throwable.message.toString()) }
		)
		)
	}

	private fun getDetailUser(username: String): Observable<DetailUser> {
		return netRepository.getDetailUserByLogin(username)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	override fun onCleared() {
		super.onCleared()
		compositeDisposable.clear()
	}
}

sealed class UserDetailResult {
	object OnProgress : UserDetailResult()
	data class OnSuccess(val user: DetailUser) : UserDetailResult()
	data class OnError(val errorMsg: String) : UserDetailResult()
}