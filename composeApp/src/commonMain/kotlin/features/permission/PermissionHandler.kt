package features.permission

import androidx.compose.runtime.Composable

data class PermissionCallBack(
    val onGranted: () -> Unit = {},
    val onDenied: () -> Unit = {},
    val onUnknown: () -> Unit = {}
)


@Composable
expect fun rememberPermissionResult(
    permission: PermissionType,
    permissionCallback: PermissionCallBack
): PermissionHandler

interface PermissionHandler {
    fun requestPermission()

    fun isGranted(
    ): Boolean
}
