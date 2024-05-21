package com.ak.jotit.core.navigation

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