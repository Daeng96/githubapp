package com.arteneta.githubapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arteneta.githubapp.model.Followers
import com.arteneta.githubapp.model.Following
import com.arteneta.githubapp.repository.NetRepository
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

	private val _followers = MutableLiveData<RequestResult<ArrayList<Followers>>>(RequestResult.Progress)
	val followers: LiveData<RequestResult<ArrayList<Followers>>> = _followers


	fun setFollower(userName: String) {
		_followers.postValue(RequestResult.Progress)
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
		_following.postValue(RequestResult.Progress)
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

