package com.arteneta.githubapp.model

import kotlinx.serialization.Serializable

@Serializable
sealed class WidgetStateData {

	@Serializable
	object Loading : WidgetStateData()

	@Serializable
	data class Data(
		val list: List<WidgetData>
	) : WidgetStateData()

	@Serializable
	data class Unavailable(val message: String) : WidgetStateData()

}

@Serializable
data class WidgetData(
	val _ID: Long,
	val login: String ,
	val avatarUri: String
)