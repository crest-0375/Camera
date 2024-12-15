package com.app.vocab.features.home.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.app.vocab.features.home.domain.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val context: Context
) : HomeRepository {

    override suspend fun saveImage(bitmap: Bitmap): Result<Uri> {
        return try {
            withContext(Dispatchers.IO) {
                val folder = File(context.filesDir, "words")
                if (!folder.exists()) {
                    folder.mkdir()
                }

                val timeStamp = Date()
                val fileName = "IMG_$timeStamp.jpg"
                val file = File(folder, fileName)

                FileOutputStream(file).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                }
                Result.success(Uri.fromFile(file))
            }
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Unknown error occurred during sign up"))
        }
    }
}