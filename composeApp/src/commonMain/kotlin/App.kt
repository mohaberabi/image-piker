import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import features.camera.CameraManager
import features.camera.rememberCameraManager
import features.gallery.rememberGalleryManager
import features.permission.PermissionCallBack
import features.permission.PermissionHandler
import features.permission.PermissionType
import features.permission.rememberPermissionResult
import features.settings.rememberSettingsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun App() {

    val coroutineScope = rememberCoroutineScope()
    var showRational by remember { mutableStateOf(false) }
    var showImage by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val settingsManager = rememberSettingsManager()

    val cameraManager = rememberCameraManager(
        onResult = { image ->
            coroutineScope.launch {
                val converted = withContext(Dispatchers.Default) {
                    image?.toImageBitmap()
                }
                bitmap = converted
                showImage = bitmap != null
            }
        }
    )
    val galleryManager = rememberGalleryManager(
        onResult = { image ->
            coroutineScope.launch {
                val converted = withContext(Dispatchers.Default) {
                    image?.toImageBitmap()
                }
                bitmap = converted
                showImage = bitmap != null
            }
        }
    )
    val cameraPermission = rememberPermissionResult(
        permission = PermissionType.CAMERA,
        permissionCallback = PermissionCallBack(
            onDenied = {
                showRational = true
            },
            onGranted = {
                cameraManager.launch()
            },
            onUnknown = {
                showRational = true

            },
        ),
    )

    val galleryPermission = rememberPermissionResult(
        permission = PermissionType.GALLERY,
        permissionCallback = PermissionCallBack(
            onDenied = {
                showRational = true

            },
            onGranted = {
                galleryManager.launch()
            },
            onUnknown = {
                showRational = true
            },
        ),
    )


    MaterialTheme {

        if (!showImage) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {

                Button(
                    onClick = {
                        val grantedGallery = galleryPermission.isGranted()
                        if (grantedGallery) {
                            galleryManager.launch()
                        } else {
                            galleryPermission.requestPermission()
                        }
                    },
                ) {
                    Text("Open Gallery")
                }
                Button(
                    onClick = {
                        val grantedCamera = cameraPermission.isGranted()
                        if (grantedCamera) {
                            cameraManager.launch()
                        } else {
                            cameraPermission.requestPermission()
                        }
                    },
                ) {
                    Text("Open Camera")
                }
            }
        } else {
            ImageBody(
                onClose = {
                    showImage = false
                },
                bitmap = bitmap,
            )
        }

        if (showRational) {
            AlertDialog(
                onDismissRequest = { showRational = false },
                title = { Text("Permission Required ") },
                buttons = {
                    TextButton(
                        onClick = {
                            settingsManager.openDeviceSettings()
                            showRational = false
                        },
                    ) { Text("Enable ") }
                    TextButton(
                        onClick = { showRational = false },
                    ) { Text("Not now  ") }

                }
            )
        }
    }
}

@Composable
fun ImageBody(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    bitmap: ImageBitmap?
) {

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
        ) {
            IconButton(
                onClick = onClose,
            ) {
                Icon(Icons.Default.Clear, contentDescription = "")
            }
        }
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                modifier = Modifier.fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                imageVector = Icons.Default.Info,
                contentDescription = "",
            )
        }
    }
}