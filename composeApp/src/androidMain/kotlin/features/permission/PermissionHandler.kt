package features.permission

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.mohaberabi.kmpimagepicker.util.isPermissionGranted
import com.mohaberabi.kmpimagepicker.util.shouldShowRational
import com.mohaberabi.kmpimagepicker.util.toAndroidPermission


@Composable
actual fun rememberPermissionResult(
    permission: PermissionType,
    permissionCallback: PermissionCallBack
): PermissionHandler {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val shouldShowRational by remember {
        mutableStateOf(activity.shouldShowRational(permission.toAndroidPermission()))
    }
    var isGranted by
    remember { mutableStateOf(context.isPermissionGranted(permission.toAndroidPermission())) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        if (granted) {
            permissionCallback.onGranted.invoke()
        } else {
            permissionCallback.onDenied.invoke()

        }
        isGranted = granted
    }

    LaunchedEffect(
        key1 = context,
        key2 = activity,
        key3 = permission,
    ) {
        isGranted = context.isPermissionGranted(permission.toAndroidPermission())

    }

    val permissionHandler = object : PermissionHandler {
        override fun requestPermission() {
            when (permission.alwaysAllowed()) {
                true -> permissionCallback.onGranted.invoke()
                else -> {
                    if (!isGranted) {
                        if (shouldShowRational) {
                            permissionCallback.onUnknown.invoke()
                        } else {
                            launcher.launch(permission.toAndroidPermission())
                        }
                    } else {
                        permissionCallback.onGranted.invoke()
                    }
                }
            }

        }

        override fun isGranted(): Boolean =
            if (permission.alwaysAllowed()) true
            else {
                isGranted =
                    context.isPermissionGranted(permission.toAndroidPermission())
                isGranted
            }


    }
    return remember(
        permissionHandler,
    ) {
        permissionHandler
    }
}

private fun PermissionType.alwaysAllowed(): Boolean = when (this) {
    PermissionType.GALLERY -> true
    else -> false
}