package com.daxen.mydancekmpsharedui

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform