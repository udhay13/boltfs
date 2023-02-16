package org.monora.uprotocol.client.android

import android.text.format.DateFormat
import androidx.core.content.edit
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager
import dagger.hilt.android.HiltAndroidApp
import org.monora.uprotocol.client.android.data.ExtrasRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.util.*

@HiltAndroidApp
class App : MultiDexApplication(), Thread.UncaughtExceptionHandler {
    private lateinit var crashLogFile: File

    private var defaultExceptionHandler: Thread.UncaughtExceptionHandler? = null

    override fun onCreate() {
        super.onCreate()

        crashLogFile = ExtrasRepository.getCrashLogFile(applicationContext)
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        initializeSettings()
    }

    private fun initializeSettings() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val hasReferralVersion = preferences.contains("referral_version")

        PreferenceManager.setDefaultValues(this, R.xml.preferences_defaults_main, false)

        if (!hasReferralVersion) preferences.edit {
            putInt("referral_version", BuildConfig.VERSION_CODE)
        }

        val migratedVersion = preferences.getInt("migrated_version", MIGRATION_NONE)
        if (migratedVersion < BuildConfig.VERSION_CODE) preferences.edit {
            putInt("migrated_version", BuildConfig.VERSION_CODE)
            if (migratedVersion > MIGRATION_NONE) putInt("previously_migrated_version", migratedVersion)
        }
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        try {
            PrintWriter(FileOutputStream(crashLogFile)).use { printWriter ->
                val stackTrace = e.stackTrace

                printWriter.append("--Uprotocol Client Crash Log ---\n")
                    .append("\nException: ${e.javaClass.simpleName}")
                    .append("\nMessage: ${e.message}")
                    .append("\nCause: ${e.cause}")
                    .append("\nDate: ")
                    .append(DateFormat.getLongDateFormat(this).format(Date()))
                    .append("\n\n--Stacktrace--\n")

                if (stackTrace.isNotEmpty()) for (element in stackTrace) with(element) {
                    printWriter.append("\n$className.$methodName:$lineNumber")
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        } finally {
            defaultExceptionHandler?.uncaughtException(t, e)
        }
    }

    companion object {
        private const val TAG = "App"

        private const val MIGRATION_NONE = -1
    }
}
