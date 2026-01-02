package com.daxen.mydancekmpsharedui.core.firebase.user

import android.net.Uri
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.android
import dev.gitlive.firebase.app
import dev.gitlive.firebase.storage.File as FirebaseFile
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File as JavaFile
import java.io.FileOutputStream

actual suspend fun uploadImageToStorage(byteArray: ByteArray, path: String): String {
    return withContext(Dispatchers.IO) {
        val context = Firebase.app.android.applicationContext
        val tempFile = JavaFile.createTempFile("upload_", ".jpg", context.cacheDir)
        
        try {
            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(byteArray)
            }

            val uri = Uri.fromFile(tempFile)
            val ref = Firebase.storage.reference.child(path)
            ref.putFile(FirebaseFile(uri))
            ref.getDownloadUrl()
        } catch (e: Exception) {
            println("Error uploading image: ${e.message}")
            throw IllegalStateException(e)
        } finally {
            tempFile.delete()
        }
    }
}