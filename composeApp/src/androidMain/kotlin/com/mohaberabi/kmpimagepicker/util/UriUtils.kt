package com.mohaberabi.kmpimagepicker.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri


fun Uri?.getBitmap(
    contentResolver: ContentResolver,
): Bitmap? {

    return try {
        this?.let {
            contentResolver.openInputStream(it).use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
        return null

    }
}