package com.mohaberabi.kmpimagepicker.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import features.permission.PermissionType


fun Context.isPermissionGranted(
    permission: String,
): Boolean =
    ContextCompat.checkSelfPermission(
        this,
        permission,
    ) == PackageManager.PERMISSION_GRANTED

fun PermissionType.toAndroidPermission(): String = when (this) {
    else -> Manifest.permission.CAMERA
}

fun Activity.shouldShowRational(permission: String): Boolean =
    shouldShowRequestPermissionRationale(permission)
