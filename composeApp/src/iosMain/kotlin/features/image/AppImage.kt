package features.image

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.reinterpret
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.darwin.ByteVar
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import org.jetbrains.skia.Image
import platform.UIKit.UIImageJPEGRepresentation

actual class AppImage(
    val image: UIImage?,
) {


    @OptIn(ExperimentalForeignApi::class)
    actual fun toByteArray(): ByteArray? {
        return if (image != null) {
            val data: NSData? = UIImageJPEGRepresentation(image, 0.99)
            data?.let { nsData ->
                val bytes = nsData.bytes ?: throw IllegalArgumentException("image bytes is null")
                val length = nsData.length
                val cPointer: CPointer<ByteVar> = bytes.reinterpret()
                ByteArray(length.toInt()) { index -> cPointer[index].toByte() }
            }

        } else {
            null
        }
    }

    actual fun toImageBitmap(): ImageBitmap? {
        val byteArray = toByteArray()
        return if (byteArray != null) {
            Image.makeFromEncoded(byteArray).toComposeImageBitmap()
        } else {
            null
        }
    }


}