package org.monora.android.codescanner

import android.hardware.Camera
import android.hardware.Camera.CameraInfo

class DecoderWrapper(
    val camera: Camera,
    val cameraInfo: CameraInfo,
    val decoder: Decoder,
    val imageSize: CartesianCoordinate,
    val previewSize: CartesianCoordinate,
    val viewSize: CartesianCoordinate,
    val displayOrientation: Int,
    val autoFocusSupported: Boolean,
    val flashSupported: Boolean,
) {
    val reverseHorizontal: Boolean = cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT

    fun release() {
        camera.release()
        decoder.shutdown()
    }
}