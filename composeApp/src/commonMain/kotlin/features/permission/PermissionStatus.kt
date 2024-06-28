package features.permission

enum class PermissionStatus {

    GRANTED,
    DENIED,
    UNKNOWN;

    val isGranted: Boolean
        get() = this == PermissionStatus.GRANTED
    val isUnknown: Boolean
        get() = this == PermissionStatus.UNKNOWN
    val isDenied: Boolean
        get() = this == PermissionStatus.DENIED
}


enum class PermissionType {
    GALLERY,
    CAMERA,
}

