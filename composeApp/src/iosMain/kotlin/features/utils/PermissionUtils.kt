package features.utils

import features.permission.PermissionStatus
import platform.AVFoundation.AVAuthorizationStatus
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatus
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusNotDetermined

fun AVAuthorizationStatus.mapCameraPermission(
): PermissionStatus {
    return when (this) {
        AVAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
        AVAuthorizationStatusNotDetermined -> PermissionStatus.UNKNOWN
        AVAuthorizationStatusDenied -> PermissionStatus.DENIED
        else -> PermissionStatus.UNKNOWN
    }
}

fun PHAuthorizationStatus.mapGalleryPermission(
): PermissionStatus {
    return when (this) {
        PHAuthorizationStatusAuthorized -> PermissionStatus.GRANTED
        PHAuthorizationStatusNotDetermined -> PermissionStatus.UNKNOWN
        PHAuthorizationStatusDenied -> PermissionStatus.DENIED
        else -> PermissionStatus.UNKNOWN
    }
}
