package features.camera

import androidx.compose.runtime.Composable
import features.image.AppImage


interface CameraManager {
    fun launch()
}

@Composable
expect fun rememberCameraManager(
    onResult: (AppImage?) -> Unit,
): CameraManager