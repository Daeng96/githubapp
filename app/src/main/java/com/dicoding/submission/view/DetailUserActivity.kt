package com.dicoding.submission.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.submission.R
import com.dicoding.submission.adapter.ListFollowerAdapter
import com.dicoding.submission.adapter.ListFollowingAdapter
import com.dicoding.submission.adapter.ViewPagerAdapter
import com.dicoding.submission.databinding.ActivityDetailUserBinding
import com.dicoding.submission.db.Favorites
import com.dicoding.submission.model.DetailUser
import com.dicoding.submission.viewmodel.DetailUsersViewModel
import com.dicoding.submission.viewmodel.FollowViewModel
import com.dicoding.submission.widget.MyWidget
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.following_fragment.*
import kotlinx.android.synthetic.main.fragment_follower.*

class DetailUserActivity : AppCompatActivity() {

	private lateinit var adapterFollowers: ListFollowerAdapter
    private lateinit var adapterFollowing : ListFollowingAdapter
    private var compositeDisposable = CompositeDisposable()
    private lateinit var binding: ActivityDetailUserBinding
	private lateinit var followViewModel: FollowViewModel
    private lateinit var detailViewModel: DetailUsersViewModel
    private lateinit var favorites: Favorites
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var detailUser: DetailUser

    private var checkDb : Boolean = true

    companion object {
        const val EXTRA_PERSON = "extra person"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this).get(DetailUsersViewModel::class.java)
        followViewModel = ViewModelProvider(this).get(FollowViewModel::class.java)

        adapterFollowers = ListFollowerAdapter(this)
        adapterFollowing = ListFollowingAdapter()


        //ambil data
        val username = intent.getStringExtra(EXTRA_PERSON)
        username?.apply {
            detailViewModel.setDetailUsers(this)
            followViewModel.setFollower(this)
            followViewModel.setFollowing(this)
        }

        floatingActionButton = binding.fab

        //mengatur view pager
        val tabsSetup = ViewPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = tabsSetup
        tab_ly.setupWithViewPager(viewPager)



        followViewModel.listFollowers.observe(this, {
            adapterFollowers.setListFollower(it)
            no_followers_tv.visibility = View.GONE
            if (it.size == 0) {
                with(no_followers_tv) {
                    this.visibility = View.VISIBLE
                    val txt = getString(R.string.there_are_no_followers_yet, username)
                    val txt1: Spanned = Html.fromHtml(txt, FROM_HTML_MODE_LEGACY)
                    this.text = txt1
                }
            }
            follower_proggres.visibility = View.GONE
            setFollower()
        })


        followViewModel.listFollowing.observe(this, {
            adapterFollowing.setListFollowing(it)
            no_following_tv.visibility = View.GONE
            if (it.size == 0) {
                with(no_following_tv) {
                    this.visibility = View.VISIBLE
                    val txt = getString(R.string.there_are_no_following_yet, username)
                    val txt2: Spanned = Html.fromHtml(txt, FROM_HTML_MODE_LEGACY)
                    this.text = txt2
                }
            }

            following_proggres.visibility = View.GONE
            setFollowing()
        })


        followViewModel.errorToast.observe(this, {
            myToast(it)
        })
        followViewModel.errorFollowing.observe(this, {
            myToast(it)
        })
        detailUser()

        binding.detailCompanyTv.isSelected = true
        binding.detailFullNameTv.isSelected = true

        binding.refreshDetailUser.setOnRefreshListener {
            refresh()
            refresh_detail_user.isRefreshing = false
        }

        //menampilkan tombol navigasi kembali
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)


        detailViewModel.isExists(username).observe(this, { check ->
            checkDb = check.isEmpty()
            if (checkDb) {
                floatingActionButton.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            } else {
                floatingActionButton.setImageResource(R.drawable.ic_favorite)
            }
        })
    }



    //menampilkan dan menambahka onklik pada navigasi
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun setFollower() {
        val rvFollower = this.findViewById<RecyclerView>(R.id.rv_follower)
        rvFollower.layoutManager = LinearLayoutManager(this)
        rvFollower.adapter = adapterFollowers
        rvFollower.setHasFixedSize(true)
    }

    private fun setFollowing () {
        val rvFollowing = this.findViewById<RecyclerView>(R.id.rv_following)
        rvFollowing.layoutManager = LinearLayoutManager(this)
        rvFollowing.adapter = adapterFollowing
        rvFollowing.setHasFixedSize(true)
    }

    private fun detailUser() {
        detailViewModel.detailUser.observe(this, {
            floatingActionButton.visibility = View.VISIBLE
            Glide.with(this)
                .load(it.avatarUrl)
                .error(ColorDrawable(Color.DKGRAY))
                .apply(RequestOptions.circleCropTransform())
                .into(detail_avatar_iv)

            val name: String? = it.name
            val company = getString(R.string.company) + " " + it.company
            if (name.isNullOrBlank()) {
                supportActionBar?.title = it.login
            } else {
                supportActionBar?.title = it.name
            }

            if (it.company.isNullOrBlank()) {
                binding.detailCompanyTv.text = getString(R.string.no_company)
            } else {
                binding.detailCompanyTv.text = company
            }

            binding.detailLoginTv.text = it.login
            binding.detailFullNameTv.text = it.name
            binding.detailLocationTv.text = it.location
            binding.detailFollowersTv.text = it.followers.toString()
            binding.detailFollowingTv.text = it.following.toString()
            binding.detailRepoTv.text = it.publicRepos.toString()

            detailUser = it
            favorites = Favorites(
                _id = detailUser.id,
                login = detailUser.login,
                avatarUrl = detailUser.avatarUrl,
                name = detailUser.name,
                company = detailUser.company,
                location = detailUser.location,
                publicRepos = detailUser.publicRepos,
                followers = detailUser.followers,
                following = detailUser.following,
                htmlUrl = detailUser.htmlUrl
            )

            floatingActionButton.setOnClickListener {
                if (checkDb) {
                    MyWidget.sendBroadcast(this)
                    detailViewModel.insertAll(favorites)
                    myToast(getString(R.string.btn_favorited, detailUser.login))
                } else {
                    MyWidget.sendBroadcast(this)
                    detailViewModel.unFavorite(favorites)
                    myToast(getString(R.string.btn_removed, detailUser.login))
                }
            }
        })

    }

    private fun refresh() {
        detailUser()
        adapterFollowing.notifyDataSetChanged()
        adapterFollowers.notifyDataSetChanged()
    }


    private fun myToast(msg : String) {
        Toast.makeText(this, msg , Toast.LENGTH_SHORT).show()
    }
}

