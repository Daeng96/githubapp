package com.arteneta.githubapp.widget

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import com.arteneta.githubapp.model.WidgetStateData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream


object WidgetDataDefinition: GlanceStateDefinition<WidgetStateData> {
	private const val DATA_STORE_FILENAME = "widgetInfo"
	/**
	 * Use the same file name regardless of the widget instance to share data between them
	 *
	 * If you need different state/data for each instance, create a store using the provided fileKey
	 */
	private val Context.datastore by dataStore(DATA_STORE_FILENAME, WidgetDataSerializer)

	object WidgetDataSerializer: Serializer<WidgetStateData> {
		override val defaultValue: WidgetStateData
			get() = WidgetStateData.Unavailable("Data Not Found")

		override suspend fun readFrom(input: InputStream): WidgetStateData = try {
			Json.decodeFromString(
				WidgetStateData.serializer(),
				input.readBytes().decodeToString()
			)
		} catch (exception: SerializationException) {
			throw CorruptionException("Could not read widget data: ${exception.message}")
		}

		override suspend fun writeTo(t: WidgetStateData, output: OutputStream) {
			output.use {
				it.write(
					Json.encodeToString(WidgetStateData.serializer(), t).encodeToByteArray()
				)
			}
		}
	}

	override suspend fun getDataStore(
		context: Context,
		fileKey: String
	): DataStore<WidgetStateData> {
		return context.datastore
	}

	override fun getLocation(context: Context, fileKey: String): File {
		return context.dataStoreFile(DATA_STORE_FILENAME)
	}
}