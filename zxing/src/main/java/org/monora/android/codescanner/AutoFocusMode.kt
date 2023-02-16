package org.monora.android.codescanner

/**
 * Code scanner auto focus mode.
 *
 * @see CodeScanner.safeAutoFocusInterval
 */
enum class AutoFocusMode {
    /**
     * Auto focus camera with the specified interval.
     *
     * @see CodeScanner.safeAutoFocusInterval
     */
    SAFE,

    /**
     * Continuous auto focus, may not work on some devices.
     */
    CONTINUOUS
}