package features.gallery

import androidx.compose.runtime.Composable
import features.image.AppImage


@Composable
expect fun rememberGalleryManager(onResult: (AppImage?) -> Unit): GalleryManager

interface GalleryManager {
    fun launch()
}