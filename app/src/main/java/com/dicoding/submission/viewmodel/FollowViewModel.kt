package com.dicoding.submission.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submission.model.Followers
import com.dicoding.submission.model.Following
import com.dicoding.submission.repository.NetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(
	private val netRepository: NetRepository
) : ViewModel() {

	private val compositeDisposable = CompositeDisposable()

	private val _listFollowing = MutableLiveData<ArrayList<Following>>()
	val listFollowing: LiveData<ArrayList<Following>> = _listFollowing

	private val _listFollowers = MutableLiveData<ArrayList<Followers>>()
	val listFollowers: LiveData<ArrayList<Followers>> = _listFollowers


	private val _errorToast = MutableLiveData<String>()
	val errorToast: LiveData<String> = _errorToast

	private val _errorFlToast = MutableLiveData<String>()
	val errorFollowing: LiveData<String> = _errorFlToast


	private val _followers = MutableLiveData<RequestResult<ArrayList<Followers>>>(RequestResult.Progress)
	val followers: LiveData<RequestResult<ArrayList<Followers>>> = _followers


	fun setFollower(userName: String) {
		compositeDisposable.add(
			getFollowerList(userName).subscribe(
				{ follower ->
					_followers.postValue(RequestResult.Success(follower))
				},
				{ throwable ->
					_followers.postValue(RequestResult.Error(throwable.message.toString()))
				})
		)
	}

	private val _following = MutableLiveData<RequestResult<ArrayList<Following>>>(RequestResult.Progress)
	val following: LiveData<RequestResult<ArrayList<Following>>> = _following

	private fun getFollowerList(userName: String): Observable<ArrayList<Followers>> {
		return netRepository.getFollower(userName)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}

	fun setFollowing(userName: String) {
		compositeDisposable.add(
			getFollowingList(userName).subscribe({ follow ->
				_following.postValue(RequestResult.Success(data = follow))
			},
				{ throwable ->
					_following.postValue(RequestResult.Error(message = throwable.message.toString()))
				})
		)
	}

	private fun getFollowingList(userName: String): Observable<ArrayList<Following>> {
		return netRepository.getFollowing(userName)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
	}
}

