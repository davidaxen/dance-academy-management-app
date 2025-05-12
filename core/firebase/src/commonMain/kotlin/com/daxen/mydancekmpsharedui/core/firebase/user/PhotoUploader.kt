package com.daxen.mydancekmpsharedui.core.firebase.user

expect suspend fun uploadImageToStorage(byteArray: ByteArray, path: String): String