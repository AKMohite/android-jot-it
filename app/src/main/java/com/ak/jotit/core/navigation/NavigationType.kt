package com.ak.jotit.core.navigation

import android.graphics.Rect
import androidx.window.layout.FoldingFeature

/**
 * [CLOSED_DRAWER] -> for compact devices such as mobile phones
 * [NAVIGATION_RAIL] -> for medium devices such as tablets and foldables
 * [PERMANENT_DRAWER] -> for large devices such as tablets and desktops
 */
internal enum class NavigationType {
    CLOSED_DRAWER,
    NAVIGATION_RAIL,
    PERMANENT_DRAWER
}

/**
 * [SINGLE_PANE] -> show single content/screen at a time
 * [DUAL_PANE] -> show list-detail content side by side
 */
internal enum class JotItContentType {
    SINGLE_PANE,
    DUAL_PANE
}

/**
 * Information about the posture of the device
 */
internal sealed interface DevicePosture {
    object NormalPosture : DevicePosture

    data class BookPosture(
        val hingePosition: Rect
    ) : DevicePosture

    data class Separating(
        val hingePosition: Rect,
        var orientation: FoldingFeature.Orientation
    ) : DevicePosture
}