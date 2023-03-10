package org.monora.uprotocol.client.android.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.genonbeta.android.framework.io.DocumentFile
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.activity.PackageInstallerActivity

object Activities {
    private const val MIME_APK = "application/vnd.android.package-archive"

    fun startLocationServiceSettings(context: Context) {
        context.startActivity(
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    fun view(context: Context, documentFile: DocumentFile) {
        view(
            context,
            documentFile.getSecureUri(context, context.getString(R.string.file_provider)),
            documentFile.getType()
        )
    }

    fun view(context: Context, uri: Uri, type: String) {
        try {
            if (MIME_APK == type) {
                if (Build.VERSION.SDK_INT >= 29) {
                    context.startActivity(Intent(context, PackageInstallerActivity::class.java).setData(uri))
                } else {
                    @Suppress("DEPRECATION")
                    context.startActivity(
                        Intent(Intent.ACTION_INSTALL_PACKAGE)
                            .setDataAndType(uri, type)
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    )
                }
            } else {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .setDataAndType(uri, type)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                )
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, R.string.error_no_activity_to_view, Toast.LENGTH_LONG).show()
        } catch (e: SecurityException) {
            Toast.makeText(context, R.string.error_content_not_found, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, R.string.error_unknown, Toast.LENGTH_LONG).show()
        }
    }
}
