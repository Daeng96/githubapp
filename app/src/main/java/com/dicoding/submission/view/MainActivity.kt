package com.dicoding.submission.view

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.submission.R
import com.dicoding.submission.adapter.ListUserAdapter
import com.dicoding.submission.model.ItemUser
import com.dicoding.submission.viewmodel.SearchViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var search: SearchViewModel
	private lateinit var adapter: ListUserAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var listUser : List<ItemUser>
    private var disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ListUserAdapter()
        recyclerView = findViewById(R.id.rv_search_user)

        search = ViewModelProvider(this).get(SearchViewModel::class.java)

        search.searchUsers.observe(this,{
            listUser = it.items as List<ItemUser>
            it.items.let { it1 -> adapter.setListUsers(it1) }
	        progressV(false)
            logoVisibility(false)
            errorConnect(false)
        })

        setUsers()

        search.notFound.observe(this, {
            alertDialog(getString(R.string.users_not_found), getString(R.string.not_found_message, it))
	        progressV(false)
            logoVisibility(true)
        })

        search.errorConnection.observe(this, {str->
            adapter.setListUsers(ArrayList(0))
            alertDialog(getString(R.string.connection_failed_title), str)
            progressV(false)
            errorConnect(true)
            logoVisibility(false)
        })

    }

    private fun setUsers() {
        val anim = AnimationUtils.loadLayoutAnimation(this, R.anim.recyclerview_anim)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutAnimation = anim
        errorConnect(false)
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemUser) {
                val i = Intent(this@MainActivity, DetailUserActivity::class.java)
                i.putExtra(DetailUserActivity.EXTRA_PERSON, data.login)
                startActivity(i)

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.item_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(input: String): Boolean {
                search.setUser(input)
                progressV(true)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite_menu -> {
                val menuFav = Intent(this, ListFavoriteActivity::class.java)
                startActivity(menuFav)
            }
            R.id.settings_menu -> {
                val menuSet = Intent(this, SettingsActivity::class.java)
                startActivity(menuSet)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //sembunyikan keyboard
    private fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        view?.let { v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }

    private fun alertDialog(title: String, msg: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle(title)
            setMessage(msg)
            setPositiveButton("Ok", null)
            setCancelable(false)
            show()
        }
    }
    private fun progressV(v: Boolean) {
        if (v) {
            search_progress.visibility = View.VISIBLE
        } else {
            search_progress.visibility = View.GONE
        }
    }

    private fun logoVisibility(v: Boolean) {
        if (v && listUser.isEmpty()) {
            home_logo.visibility = View.VISIBLE
        } else {
            home_logo.visibility = View.GONE
        }

    }
    private fun errorConnect(v: Boolean) {
        if (v) {
            iv_error_connect.visibility = View.VISIBLE
        } else {
            iv_error_connect.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }
}