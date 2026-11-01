package com.flxholle.quicktiles.abstract_tiles

import com.flxholle.quicktiles.utils.GrantPermissionDialogs

abstract class WriteSecureSettingsTileService<T : Any> : SelectionTileService<T>() {

    final override fun onTileClicked() {
        runWhenUnlocked {
            super.onTileClicked()
        }
    }

    override fun checkPermission(): Boolean {
        return if (GrantPermissionDialogs.hasWriteSecureSettingsPermission(this)) {
            true
        } else {
            showPermissionDialog(GrantPermissionDialogs.getWriteSecureSettingsDialog(this))
            false
        }
    }
}
