package com.asdoi.quicksettings.abstract_tiles

import com.asdoi.quicksettings.utils.GrantPermissionDialogs

abstract class RootTileService<T : Any> : SelectionTileService<T>() {

    override fun checkPermission(): Boolean {
        return if (GrantPermissionDialogs.hasRootPermission(this)) {
            true
        } else {
            showDialog(GrantPermissionDialogs.getRootDialog(this))
            false
        }
    }
}
