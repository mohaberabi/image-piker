package features.settings

import androidx.compose.runtime.Composable


expect class SettingsManager(onLaunch: () -> Unit) {
    fun openDeviceSettings()
}

@Composable
expect fun rememberSettingsManager(): SettingsManager