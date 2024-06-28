package features.image

import androidx.compose.ui.graphics.ImageBitmap


expect class AppImage {
    fun toByteArray(): ByteArray?
    fun toImageBitmap(): ImageBitmap?
}