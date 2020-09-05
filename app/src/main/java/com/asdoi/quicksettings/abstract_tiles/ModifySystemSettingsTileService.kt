package com.asdoi.quicksettings.abstract_tiles

import com.asdoi.quicksettings.utils.GrantPermissionDialogs

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
