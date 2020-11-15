package com.dicoding.submission.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.submission.R
import com.dicoding.submission.widget.MyWidget


class ListFavoriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_favorite)
        MyWidget.sendBroadcast(this)
        initView()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_favorite, ListFavoritesFragment())
            .commit()

    }
    private fun initView() {
        supportActionBar?.let { tb ->
            tb.setDisplayHomeAsUpEnabled(true)
            tb.title = getString(R.string.list_favorite)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}



