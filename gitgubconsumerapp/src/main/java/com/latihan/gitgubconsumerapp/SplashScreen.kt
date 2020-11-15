package com.latihan.gitgubconsumerapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

	private val delayMillis : Long  = 2000

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash_screen)

		supportActionBar?.hide()
		motionLayout.startLayoutAnimation()

		Looper.myLooper()?.let { Handler(it).postDelayed({
			startActivity(Intent(this, ListFavoriteActivity::class.java))
			finish()
		}, delayMillis) }

	}
}