package features.camera

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider.getUriForFile
import com.mohaberabi.kmpimagepicker.AppFileProvider
import com.mohaberabi.kmpimagepicker.util.getBitmap
import features.image.AppImage
import java.io.File
import java.util.Objects


@Composable
actual fun rememberCameraManager(onResult: (AppImage?) -> Unit): CameraManager {
    val context = LocalContext.current
    val contentResolver: ContentResolver = context.contentResolver
    var resultUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                onResult.invoke(
                    AppImage(
                        resultUri.getBitmap(contentResolver)
                    )
                )
            }
        }
    )
    val manager = object : CameraManager {
        override fun launch() {
            resultUri = AppFileProvider.createFile(context)
            cameraLauncher.launch(resultUri)
        }
    }
    return remember {
        manager
    }
}


