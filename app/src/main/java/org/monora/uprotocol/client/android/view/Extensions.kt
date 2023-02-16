package org.monora.uprotocol.client.android.view

import android.app.Activity
import android.content.ContextWrapper
import android.view.View
import java.lang.IllegalStateException

fun View.findActivity(): Activity {
    var context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }

    throw IllegalStateException()
}
