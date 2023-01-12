package com.arteneta.githubapp.viewmodel

import androidx.lifecycle.*
import com.arteneta.githubapp.db.Favorites
import com.arteneta.githubapp.model.DetailUser
import com.arteneta.githubapp.repository.DbRepository
import com.arteneta.githubapp.repository.NetRepository
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

	private val _detailUser = MutableLiveData<RequestResult<DetailUser>>()
	val detailUser: LiveData<RequestResult<DetailUser>> = _detailUser

	fun getUserByLogin(login: String) = dbRepository.getUserByLogin(login)
	val allUsers: LiveData<List<Favorites>> = dbRepository.listUsers
	fun isExists(login: String)  = dbRepository.isExists(login).map { it.isNotEmpty() }

	fun insertAll(favorite: Favorites) = viewModelScope.launch(Dispatchers.IO) {
		dbRepository.insertAll(favorite)
	}

	fun unFavorite(data: Favorites) = viewModelScope.launch(Dispatchers.IO) {
		dbRepository.unFavorite(data)
	}


	fun setDetailUsers(userName: String) {
		_detailUser.postValue(RequestResult.Progress)
		compositeDisposable.add(
			getDetailUser(userName).subscribe(
				{
					_detailUser.postValue(RequestResult.Success(it))
				},
				{ throwable -> _detailUser.postValue(RequestResult.Error(throwable.message.toString())) })
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
