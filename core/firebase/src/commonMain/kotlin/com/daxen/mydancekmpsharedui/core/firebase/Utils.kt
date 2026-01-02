package com.daxen.mydancekmpsharedui.core.firebase

import kotlinx.datetime.Clock

fun getCurrentTimestamp(): String {
    return Clock.System.now().toString()
}