package org.monora.uprotocol.client.android.service.web.response

import android.content.Context
import com.genonbeta.android.framework.io.DocumentFile
import com.yanzhenjie.andserver.http.ResponseBody
import com.yanzhenjie.andserver.util.MediaType
import java.io.File
import java.io.OutputStream
import java.lang.ref.WeakReference
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FileZipBody(context: Context, private val file: DocumentFile) : ResponseBody {
    private val context = WeakReference(context)

    override fun isRepeatable(): Boolean = false

    override fun contentLength(): Long = -1L

    override fun contentType(): MediaType = MediaType.ALL

    override fun writeTo(output: OutputStream) {
        val buffer = ByteArray(16 * 1024)
        val context = context.get() ?: return
        val zipOutputStream = ZipOutputStream(output)

        zipOutputStream.setLevel(0)
        //zipOutputStream.setMethod(ZipEntry.STORED);

        fun travel(file: DocumentFile, path: String? = null) {
            val childPath = if (path != null) path + File.separator + file.getName() else file.getName()

            if (file.isDirectory()) {
                for (childFile in file.listFiles(context)) {
                    travel(childFile, childPath)
                }

                return
            }

            try {
                context.contentResolver.openInputStream(file.getUri())?.use { inputStream ->
                    val entry = ZipEntry(childPath)
                    entry.time = file.getLastModified()

                    zipOutputStream.putNextEntry(entry)

                    var len: Int
                    while (inputStream.read(buffer).also { len = it } != -1) {
                        if (len > 0) {
                            zipOutputStream.write(buffer, 0, len)
                        }
                    }

                    zipOutputStream.closeEntry()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        travel(file)

        zipOutputStream.finish()
        zipOutputStream.flush()
        zipOutputStream.close()
    }
}
