package features.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual class AppImage(
    private val bitmap: Bitmap?,
) {
    actual fun toByteArray(): ByteArray? {
        return if (bitmap != null) {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.toByteArray()
        } else {
            null
        }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val asByte = toByteArray()
        return if (asByte != null) {
            BitmapFactory.decodeByteArray(asByte, 0, asByte.size).asImageBitmap()
        } else {
            null
        }
    }
}