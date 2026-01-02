package com.daxen.mydancekmpsharedui.core.firebase.user

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import platform.Foundation.NSDate
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.timeIntervalSince1970

actual suspend fun uploadImageToStorage(byteArray: ByteArray, path: String): String {
    val tempDir = NSTemporaryDirectory()
    val fileName = "upload_${NSDate().timeIntervalSince1970}.jpg"
    val filePath = tempDir + fileName

    val fileUrl = NSURL.fileURLWithPath(filePath)

    val file = File(fileUrl)
    val ref = Firebase.storage.reference.child(path)
    ref.putFile(file)
    return ref.getDownloadUrl()
}