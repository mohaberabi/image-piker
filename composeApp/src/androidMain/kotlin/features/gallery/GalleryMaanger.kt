package features.gallery

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.mohaberabi.kmpimagepicker.util.getBitmap
import features.image.AppImage


@Composable
actual fun rememberGalleryManager(
    onResult: (AppImage?) -> Unit,
): GalleryManager {

    val context = LocalContext.current
    val resolver: ContentResolver = context.contentResolver
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
        ) { uri ->
            onResult.invoke(AppImage(uri.getBitmap(contentResolver = resolver)))
        }

    val galleryManager = object : GalleryManager {
        override fun launch() {
            launcher.launch(
                PickVisualMediaRequest(
                    mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }
    return remember {
        galleryManager
    }
}
