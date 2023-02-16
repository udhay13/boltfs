package org.monora.android.codescanner

import android.graphics.Rect
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.ReaderException
import com.google.zxing.Result
import org.monora.android.codescanner.Utils.decodeLuminanceSource
import org.monora.android.codescanner.Utils.getImageFrameRect
import org.monora.android.codescanner.Utils.rotateYuv

class DecodeTask(
    private val image: ByteArray,
    private val imageSize: CartesianCoordinate,
    private val previewSize: CartesianCoordinate,
    private val viewSize: CartesianCoordinate,
    private val viewFrameRect: Rect,
    private val orientation: Int,
    private val reverseHorizontal: Boolean,
) {
    @Throws(ReaderException::class)
    fun decode(reader: MultiFormatReader): Result? {
        var imageWidth = imageSize.x
        var imageHeight = imageSize.y
        val orientation = orientation
        val image = rotateYuv(image, imageWidth, imageHeight, orientation)

        if (orientation == 90 || orientation == 270) {
            val width = imageWidth
            imageWidth = imageHeight
            imageHeight = width
        }

        val frameRect = getImageFrameRect(imageWidth, imageHeight, viewFrameRect, previewSize, viewSize)
        val frameWidth: Int = frameRect.width()
        val frameHeight: Int = frameRect.height()

        return if (frameWidth < 1 || frameHeight < 1) {
            null
        } else decodeLuminanceSource(
            reader,
            PlanarYUVLuminanceSource(
                image,
                imageWidth,
                imageHeight,
                frameRect.left,
                frameRect.top,
                frameWidth,
                frameHeight,
                reverseHorizontal
            )
        )
    }
}