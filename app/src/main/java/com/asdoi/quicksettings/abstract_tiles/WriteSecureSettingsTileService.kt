package com.asdoi.quicksettings.abstract_tiles

import com.asdoi.quicksettings.utils.GrantPermissionDialogs

abstract class WriteSecureSettingsTileService<T : Any> : SelectionTileService<T>() {

    override fun checkPermission(): Boolean {
        return if (GrantPermissionDialogs.hasWriteSecureSettingsPermission(this)) {
            true
        } else {
            showDialog(GrantPermissionDialogs.getWriteSecureSettingsDialog(this))
            false
        }
    }
}
