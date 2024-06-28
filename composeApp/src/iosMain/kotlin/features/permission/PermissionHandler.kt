package features.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import features.permission.PermissionType.*
import features.utils.mapCameraPermission
import features.utils.mapGalleryPermission
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSURL
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHPhotoLibrary
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString


class IOSPermissionHandler(
    private val permission: PermissionType,
    private val permissionCallback: PermissionCallBack
) : PermissionHandler {
    override fun requestPermission() {
        when (permission) {
            GALLERY -> requestGalleryPermission()
            CAMERA -> requestCameraPermission()
        }
    }

    private fun requestGalleryPermission(
    ) {

        when (galleryStatus().mapGalleryPermission()) {
            PermissionStatus.GRANTED -> permissionCallback.onGranted.invoke()
            PermissionStatus.DENIED -> permissionCallback.onDenied.invoke()
            PermissionStatus.UNKNOWN -> {
                requestGallery()
            }
        }

    }

    private fun requestGallery() {
        PHPhotoLibrary.Companion.requestAuthorization { newStatus ->
            val appPermission = newStatus.mapGalleryPermission()
            if (appPermission.isGranted) {
                permissionCallback.onGranted.invoke()
            } else {
                permissionCallback.onDenied.invoke()
            }
        }


    }

    private fun requestCameraPermission(
    ) {
        when (cameraStatus().mapCameraPermission()) {
            PermissionStatus.GRANTED -> permissionCallback.onGranted.invoke()
            PermissionStatus.DENIED -> permissionCallback.onDenied.invoke()
            PermissionStatus.UNKNOWN -> {
                requestCamera()
            }
        }
    }


    private fun requestCamera() {
        AVCaptureDevice.Companion.requestAccessForMediaType(AVMediaTypeVideo) { isGranted ->
            if (isGranted) {
                permissionCallback.onGranted.invoke()
            } else {
                permissionCallback.onDenied.invoke()
            }
        }
    }


    override fun isGranted(
    ): Boolean {
        return when (permission) {
            GALLERY -> galleryStatus().mapGalleryPermission().isGranted
            CAMERA -> cameraStatus().mapCameraPermission().isGranted
        }
    }


    private fun cameraStatus(): AVAuthorizationStatus =
        AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)


    private fun galleryStatus(): PHAuthorizationStatus =
        PHPhotoLibrary.authorizationStatus()


}

@Composable
actual fun rememberPermissionResult(
    permission: PermissionType,
    permissionCallback: PermissionCallBack
): PermissionHandler {
    val permissionHandler = IOSPermissionHandler(
        permission = permission,
        permissionCallback = permissionCallback,
    )
    return remember { permissionHandler }
}