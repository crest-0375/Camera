package com.app.vocab.features.home.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toFile
import com.app.vocab.features.home.domain.repository.HomeRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val context: Context,
    private val fireStorage: StorageReference
) : HomeRepository {

    override suspend fun saveImage(bitmap: Bitmap): Result<Uri> {
        return try {
            withContext(Dispatchers.IO) {
                val folder = File(context.filesDir, "words")
                if (!folder.exists()) {
                    folder.mkdir()
                }

                val timeStamp = System.currentTimeMillis()
                val fileName = "IMG_$timeStamp.jpg"
                val file = File(folder, fileName)

                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                }
                Result.success(Uri.fromFile(file))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Unknown error occurred during image save"))
        }
    }

    override suspend fun saveImageToStorage(uri: Uri): Result<String> {
        return try {
            withContext(Dispatchers.IO) {
                val sRef = fireStorage.child(
                    uri.lastPathSegment ?: "IMG_${System.currentTimeMillis()}.jpg"
                )
                val result = sRef.putFile(uri).await()
                val downloadUrl = result.metadata?.reference?.downloadUrl?.await()
                if (downloadUrl != null) {
                    deleteImage(uri)
                    Result.success(downloadUrl.toString())
                } else
                    Result.failure(Exception("Unknown error occurred during image upload"))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Unknown error occurred during image upload"))
        }
    }

    private fun deleteImage(uri: Uri): Boolean {
        return try {
            val file = uri.toFile()
            return file.exists() && file.delete()
        } catch (e: Exception) {
            false
        }
    }
}