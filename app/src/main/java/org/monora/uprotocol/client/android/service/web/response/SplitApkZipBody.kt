package org.monora.uprotocol.client.android.service.web.response

import com.yanzhenjie.andserver.http.ResponseBody
import com.yanzhenjie.andserver.util.MediaType
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class SplitApkZipBody(private val contents: List<String>) : ResponseBody {
    override fun isRepeatable(): Boolean = false

    override fun contentLength(): Long = -1L

    override fun contentType(): MediaType = MediaType.ALL

    override fun writeTo(output: OutputStream) {
        val buffer = ByteArray(16 * 1024)
        val zipOutputStream = ZipOutputStream(output)

        zipOutputStream.setLevel(0)

        contents.forEach { content ->
            val file = File(content)

            FileInputStream(file).use { inputStream ->
                val entry = ZipEntry(file.name)
                entry.time = file.lastModified()
                zipOutputStream.putNextEntry(entry)

                var len: Int
                while (inputStream.read(buffer).also { len = it } != -1) {
                    if (len > 0) {
                        zipOutputStream.write(buffer, 0, len)
                    }
                }

                zipOutputStream.closeEntry()
            }
        }

        zipOutputStream.finish()
        zipOutputStream.flush()
        zipOutputStream.close()
    }
}
