package org.monora.android.codescanner

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.annotation.WorkerThread
import org.monora.android.codescanner.Utils.SuppressErrorCallback
import java.lang.Exception

/**
 * Code scanner error callback.
 */
fun interface ErrorCallback {
    /**
     * Called when error has occurred.
     *
     * Note that this method always called on a worker thread.
     *
     * @param error Exception that has been thrown.
     * @see Handler
     * @see Looper.getMainLooper
     * @see Activity.runOnUiThread
     */
    @WorkerThread
    fun onError(error: Exception)

    companion object {
        /**
         * Callback to suppress errors.
         */
        @JvmField
        val SUPPRESS: ErrorCallback = SuppressErrorCallback()
    }
}