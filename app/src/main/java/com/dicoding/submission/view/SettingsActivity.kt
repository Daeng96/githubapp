package com.dicoding.submission.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.dicoding.submission.R
import com.dicoding.submission.alarmnotifications.AlarmReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

@Immutable
data class GithubAppPref(
	val alarmEnable: Boolean = false
)

class GithubDataStore(private val dataStore: DataStore<Preferences>) {

	object PrefKey {
		val ALARM_ENABLED = booleanPreferencesKey("alarm")
	}

	val githubDataStore = dataStore.data.catch { ex ->
		if (ex is IOException) emit(emptyPreferences()) else throw ex
	}.map { mapPref(it) }

	suspend fun alarmEnable(enable: Boolean) {
		dataStore.edit {
			it[PrefKey.ALARM_ENABLED] = enable
		}
	}

	private fun mapPref(preferences: Preferences): GithubAppPref {
		return GithubAppPref(preferences[PrefKey.ALARM_ENABLED] ?: false)
	}
}


@Composable
fun SettingScreen() {

	val context = LocalContext.current
	val preferences = GithubDataStore(context.githubPref)
	val alarmEnable = preferences.githubDataStore.collectAsState(initial = GithubAppPref()).value
	val coroutineScope = rememberCoroutineScope()
	val alarmReceiver = AlarmReceiver()

	LaunchedEffect(key1 = alarmEnable.alarmEnable, block = {
		if (alarmEnable.alarmEnable) {
			alarmReceiver.setRepeater(context)
		} else {
			alarmReceiver.cancelRepeater(context)
		}
	})

	Column(modifier = Modifier.fillMaxSize()) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Switch(checked = alarmEnable.alarmEnable, onCheckedChange = {
				coroutineScope.launch(Dispatchers.IO) { preferences.alarmEnable(it) }
			})
			Text(text = stringResource(id = R.string.alarm_title_pref))
		}
	}
}