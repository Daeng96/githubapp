package com.dicoding.submission.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submission.R
import com.dicoding.submission.db.Favorites
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.details_favorite_fragment.*

class DetailsFavoriteFragment : BottomSheetDialogFragment() {

	private lateinit var listFavorites: Favorites
	companion object {

		const val FV_KEY = "fav"
		fun newInstance( favorites: Favorites) : DetailsFavoriteFragment{
			val fragment = DetailsFavoriteFragment()
			val args = Bundle().apply {
				putParcelable(FV_KEY, favorites)
			}
			fragment.arguments = args
			return fragment
		}

	}

	fun showBottomModal(fragmentManager: FragmentManager) {
		if (fragmentManager.findFragmentByTag(FV_KEY) != null) return showNow(fragmentManager, FV_KEY)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.details_favorite_fragment, container, false)
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		arguments?.getParcelable<Favorites>(FV_KEY)?.let {
			listFavorites = it
		}
		tv_company_fv.text = listFavorites.company
		tv_detail_fv_na.text = listFavorites.name
		tv_username_fv.text = listFavorites.login
		tv_followers_fv.text = listFavorites.followers.toString()
		tv_following_fv.text = listFavorites.following.toString()
		tv_repos_fv.text = listFavorites.publicRepos.toString()
		tv_location_fv.text = listFavorites.location

		Glide.with(this)
			.load(listFavorites.avatarUrl)
			.apply(RequestOptions.circleCropTransform())
			.into(img_detail_fv)

		btn_more.setOnClickListener{
			val i = Intent(context, DetailUserActivity::class.java)
			i.putExtra(DetailUserActivity.EXTRA_PERSON, listFavorites.login)
			startActivity(i)
		}
	}

}