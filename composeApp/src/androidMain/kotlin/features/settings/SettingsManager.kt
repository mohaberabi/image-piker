package features.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext


actual class SettingsManager actual constructor(
    private val onLaunch: () -> Unit,
) {
    actual fun openDeviceSettings() {
        onLaunch()
    }
}

@Composable
actual fun rememberSettingsManager(): SettingsManager {
    val context = LocalContext.current
    val settingsManager = SettingsManager(
        onLaunch = {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", context.packageName, null)
            ).also {
                context.startActivity(it)
            }
        },
    )
    return remember {
        settingsManager
    }
}