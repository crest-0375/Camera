package com.app.vocab.features.home.domain.repository

import android.graphics.Bitmap
import android.net.Uri

interface HomeRepository {
    suspend fun saveImage(bitmap: Bitmap): Result<Uri>
    suspend fun saveImageToStorage(uri: Uri): Result<String>
}