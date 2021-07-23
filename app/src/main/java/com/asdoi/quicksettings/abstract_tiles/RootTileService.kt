package com.asdoi.quicksettings.abstract_tiles

import com.asdoi.quicksettings.utils.GrantPermissionDialogs

abstract class RootTileService<T : Any> : SelectionTileService<T>() {
    final override fun onClick() {
        unlockAndRun {
            super.onClick()
        }
    }

    override fun checkPermission(): Boolean {
        return if (GrantPermissionDialogs.hasRootPermission()) {
            true
        } else {
            showDialog(GrantPermissionDialogs.getRootPermissionDialog(this))
            false
        }
    }
}