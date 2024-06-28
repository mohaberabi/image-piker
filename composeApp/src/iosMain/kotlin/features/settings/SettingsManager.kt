package features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString


actual class SettingsManager actual constructor(private val onLaunch: () -> Unit) {
    actual fun openDeviceSettings() {
        onLaunch()
    }
}


@Composable
actual fun rememberSettingsManager(): SettingsManager {
    return remember {
        SettingsManager {
            NSURL.URLWithString(UIApplicationOpenSettingsURLString)?.let {
                UIApplication.sharedApplication.openURL(it)
            }
        }
    }
}