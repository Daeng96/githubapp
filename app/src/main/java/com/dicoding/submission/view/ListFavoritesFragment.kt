package com.dicoding.submission.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.submission.R
import com.dicoding.submission.adapter.FavoriteAdapter
import com.dicoding.submission.db.Favorites
import com.dicoding.submission.viewmodel.DetailUsersViewModel
import com.dicoding.submission.widget.MyWidget

class ListFavoritesFragment : Fragment() {

	private lateinit var adapterFavorites : FavoriteAdapter
	private lateinit var favoriteViewModel: DetailUsersViewModel

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_list_favorite, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		favoriteViewModel = ViewModelProvider(this).get(DetailUsersViewModel::class.java)

		val recView = activity?.findViewById<RecyclerView>(R.id.list)
		adapterFavorites = FavoriteAdapter(context)

		recView?.apply {
			val anim = AnimationUtils.loadLayoutAnimation(context, R.anim.recyclerview_anim)
			adapter = adapterFavorites
			layoutManager = LinearLayoutManager(context)
			addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
			layoutAnimation = anim
			setHasFixedSize(true)
		}

		favoriteViewModel.allUsers.observe(viewLifecycleOwner, {
			adapterFavorites.setListFavorite(it)
		})



		adapterFavorites.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
			override fun onItemClicked(data: Favorites) {
				context?.let {
					AlertDialog.Builder(it)
						.setTitle(getString(R.string.btn_remove))
						.setMessage(getString(R.string.msg_remove_user))
						.setCancelable(false)
						.setPositiveButton(getString(R.string.btn_remove)) { _, _ ->
							favoriteViewModel.unFavorite(data)
							MyWidget.sendBroadcast(context as Context)
							Toast.makeText(
								context,
								getString(R.string.msg_removed),
								Toast.LENGTH_LONG
							).show()
						}
						.setNegativeButton(getString(R.string.btn_negative), null)
						.show()
				}
			}
		})
		adapterFavorites.click {
			DetailsFavoriteFragment.newInstance(it).showBottomModal(childFragmentManager)
		}

	}
}
