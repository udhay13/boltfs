package org.monora.uprotocol.client.android.data

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.liveData
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import dagger.hilt.android.qualifiers.ApplicationContext
import io.noties.markwon.Markwon
import kotlinx.coroutines.Dispatchers
import org.monora.uprotocol.client.android.BuildConfig
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.model.LibraryLicense
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExtrasRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val crashLogFile by lazy {
        context.getFileStreamPath(FILE_UNHANDLED_CRASH_LOG)
    }

    fun clearCrashLog() {
        crashLogFile.delete()
    }

    fun declareLatestChangelogAsShown() = preferences.edit {
        putInt(KEY_CHANGELOG_SEEN_VERSION, BuildConfig.VERSION_CODE)
    }

    fun getChangelog() = liveData(Dispatchers.IO) {
        context.resources.openRawResource(R.raw.changelog).use {
            emit(Markwon.create(context).toMarkdown(String(it.readBytes())))
        }
    }

    fun getCrashLog() = liveData(Dispatchers.IO) {
        if (crashLogFile.exists()) {
            try {
                FileReader(crashLogFile).use {
                    emit(it.readText())
                }
            } catch (ignored: Exception) {
            }
        }
    }

    fun getLicenses() = liveData(Dispatchers.IO) {
        val list = mutableListOf<LibraryLicense>()

        context.assets.open("licenses.json").use { inputStream ->
            val jsonReader = JsonReader(InputStreamReader(inputStream))
            val gson = Gson()

            try {
                // skip to the "libraries" index
                jsonReader.beginObject()

                if (!jsonReader.hasNext()) {
                    return@use
                } else if (jsonReader.nextName() != "libraries") {
                    Log.d(TAG, "getLicenses: 'libraries' does not exist")
                    return@use
                }

                jsonReader.beginArray()

                while (jsonReader.hasNext()) {
                    list.add(gson.fromJson(jsonReader, LibraryLicense::class.java))
                }

                list.sortBy { it.artifactId.group }

                jsonReader.endArray()
                jsonReader.endObject()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        emit(list)
    }

    fun shouldShowCrashLog(): Boolean {
        return crashLogFile.exists()
    }

    fun shouldShowLatestChangelog(): Boolean {
        val lastSeenChangelog = preferences.getInt(KEY_CHANGELOG_SEEN_VERSION, -1)
        return BuildConfig.VERSION_CODE != lastSeenChangelog
    }

    companion object {
        private const val TAG = "ExtrasRepository"

        private const val KEY_CHANGELOG_SEEN_VERSION = "changelog_seen_version"

        private const val FILE_UNHANDLED_CRASH_LOG = "unhandled_crash_log.txt"

        fun getCrashLogFile(context: Context): File {
            return context.getFileStreamPath(FILE_UNHANDLED_CRASH_LOG)
        }
    }
}
