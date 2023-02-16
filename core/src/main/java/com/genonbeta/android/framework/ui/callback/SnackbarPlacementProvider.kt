package com.genonbeta.android.framework.ui.callback

import com.google.android.material.snackbar.Snackbar

/**
 * created by: veli
 * date: 15/04/18 18:45
 */
fun interface SnackbarPlacementProvider {
    fun createSnackbar(resId: Int, vararg objects: Any?): Snackbar?
}