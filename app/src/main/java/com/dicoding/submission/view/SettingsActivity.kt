package com.dicoding.submission.view

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.dicoding.submission.R
import com.dicoding.submission.alarmnotifications.AlarmReceiver

class SettingsActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.settings_activity)
		if (savedInstanceState == null) {
			supportFragmentManager
				.beginTransaction()
				.replace(R.id.settings, SettingsFragment())
				.commit()
		}

		supportActionBar?.setDisplayHomeAsUpEnabled(true)
	}

	override fun onSupportNavigateUp(): Boolean {
		onBackPressed()
		return true
	}
}

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

	private lateinit var alarmReceiver: AlarmReceiver
	private lateinit var isRepeat : SwitchPreferenceCompat
	private lateinit var repeatAlarm : String

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.root_preferences, rootKey)
		alarmReceiver = AlarmReceiver()
		init()
	}

	private fun init() {
		repeatAlarm = context?.getString(R.string.alarm_key) ?: String()
		isRepeat = findPreference<SwitchPreferenceCompat>(repeatAlarm) as SwitchPreferenceCompat
	}

	override fun onResume() {
		super.onResume()
		preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
	}

	override fun onPause() {
		super.onPause()
		preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
	}

	override fun onSharedPreferenceChanged(p0: SharedPreferences, key: String) {
		if (key == repeatAlarm) {
			val isRepeat = p0.getBoolean(repeatAlarm, false)
			if (isRepeat) {
				context?.let { alarmReceiver.setRepeater(it) }
			} else {
				context?.let { alarmReceiver.cancelRepeater(it) }
			}
		}
	}

}