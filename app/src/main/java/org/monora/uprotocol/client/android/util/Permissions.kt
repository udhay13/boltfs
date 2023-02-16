package org.monora.uprotocol.client.android.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat.*
import org.monora.uprotocol.client.android.R

object Permissions {
    fun checkRunningConditions(context: Context): Boolean {
        for (permission in getAll()) {
            if (checkSelfPermission(context, permission.id) != PERMISSION_GRANTED && permission.isRequired) {
                return false
            }
        }
        return true
    }

    fun getAll(): List<Permission> {
        val permissions: MutableList<Permission> = ArrayList()
        if (Build.VERSION.SDK_INT >= 23) {
            permissions.add(
                Permission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    R.string.storage_permission,
                    R.string.storage_permission_summary
                )
            )
        }
        return permissions
    }

    data class Permission(
        val id: String,
        @StringRes val title: Int,
        @StringRes val description: Int,
        val isRequired: Boolean = true
    )
}
