package com.latihan.gitgubconsumerapp

import android.annotation.SuppressLint
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class
ListFavoriteActivity : AppCompatActivity() {

	private lateinit var uriWithId: Uri

	private lateinit var adapter: FavoriteAdapter
	private lateinit var recyclerView: RecyclerView

    companion object {
	    const val EXTRA_KEY = "EXTRA KEY"
	    private const val AUTHORITY = "com.dicoding.submission"
		private const val TABLE_NAME = "favorite"

		val CONTENT_URI: Uri = Uri.Builder().scheme("content")
			.authority(AUTHORITY)
			.appendPath(TABLE_NAME)
			.build()
	}

    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
	    super.onCreate(savedInstanceState)
	    setContentView(R.layout.activity_list_favorite)

	    recyclerView = rv_favorite
	    adapter = FavoriteAdapter(this)
	    recyclerView.setHasFixedSize(true)
	    recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

	   // loadData()
	    adapterClick()

		val handleThread = HandlerThread("DataObserver")
	    handleThread.start()
	    val handler = Handler(handleThread.looper)
	    val observer = object : ContentObserver(handler) {
		    override fun onChange(selfChange: Boolean) {
			    loadData()
			    super.onChange(selfChange)
		    }
	    }
	    contentResolver.registerContentObserver(CONTENT_URI, true, observer)
	    if (savedInstanceState == null) {
		    loadData()
	    } else {
		    val list = savedInstanceState.getParcelableArrayList<Favorites>(EXTRA_KEY)
		    if (list != null) {
			    adapter.setListFavorite(list)
		    }
	    }
	    swipe_refresh.setOnRefreshListener {
		    refreshList()
		    swipe_refresh.isRefreshing = false
	    }
    }

	private fun  adapterClick(){
		val f = supportFragmentManager
		adapter.click {
			DetailsFavoriteFragment.newInstance(it).showBottomModal(f)
		}

		adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
			override fun onItemClicked(data: Favorites) {

				uriWithId = Uri.parse((CONTENT_URI.toString() +"/"+ data._ID))
				AlertDialog.Builder(this@ListFavoriteActivity)
					.setTitle(getString(R.string.btn_remove))
					.setMessage(getString(R.string.msg_remove_user))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.btn_remove)) { _, _ ->
						contentResolver.delete(uriWithId, null, null)
						Toast.makeText(this@ListFavoriteActivity, getString(R.string.msg_removed), Toast.LENGTH_LONG).show()
						refreshList()
					}
					.setNegativeButton(getString(R.string.btn_negative), null)
					.show()
			}
		})
	}
	private fun loadData() {
		GlobalScope.launch(Dispatchers.Main) {
			val sync = async(Dispatchers.IO) {
				val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
				MappingHelper.mapCursor(cursor)
			}
			val list = sync.await()
			recyclerView.layoutManager = LinearLayoutManager(applicationContext)
			recyclerView.adapter = adapter
			if (list.size > 0) {
				adapter.setListFavorite(list)
			} else {
				adapter.setListFavorite(ArrayList())
			}
		}
	}

	@SuppressLint("Recycle")
	private fun refreshList() {
		loadData()
		adapter.notifyDataSetChanged()
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		val d = contentResolver?.query(CONTENT_URI, null, null, null, null)
		val e = MappingHelper.mapCursor(d)
		outState.putParcelableArrayList(EXTRA_KEY, e)
	}

	override fun onResume() {
		super.onResume()
		refreshList()
	}
}