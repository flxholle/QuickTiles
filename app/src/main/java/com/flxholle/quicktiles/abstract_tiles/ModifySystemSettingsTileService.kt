package com.flxholle.quicktiles.abstract_tiles

import com.flxholle.quicktiles.utils.GrantPermissionDialogs

abstract class ModifySystemSettingsTileService<T : Any> : SelectionTileService<T>() {

    override fun checkPermission(): Boolean {
        return if (GrantPermissionDialogs.hasModifySystemSettingsPermission(this)) {
            true
        } else {
            showDialog(GrantPermissionDialogs.getModifySystemSettingsDialog(this))
            false
        }
    }

}
