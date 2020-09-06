package com.asdoi.quicksettings.abstract_tiles

import com.asdoi.quicksettings.utils.GrantPermissionDialogs

abstract class WriteSecureSettingsTileService<T : Any> : SelectionTileService<T>() {

    final override fun onClick() {
        unlockAndRun {
            super.onClick()
        }
    }

    override fun checkPermission(): Boolean {
        return if (GrantPermissionDialogs.hasWriteSecureSettingsPermission(this)) {
            true
        } else {
            showDialog(GrantPermissionDialogs.getWriteSecureSettingsDialog(this))
            false
        }
    }
}
