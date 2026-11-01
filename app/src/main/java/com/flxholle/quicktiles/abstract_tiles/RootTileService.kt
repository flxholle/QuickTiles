package com.flxholle.quicktiles.abstract_tiles

import com.flxholle.quicktiles.utils.GrantPermissionDialogs

abstract class RootTileService<T : Any> : SelectionTileService<T>() {
    final override fun onClick() {
        runWhenUnlocked {
            super.onClick()
        }
    }

    override fun checkPermission(): Boolean {
        return if (GrantPermissionDialogs.hasRootPermission()) {
            true
        } else {
            showPermissionDialog(GrantPermissionDialogs.getRootPermissionDialog(this))
            false
        }
    }
}
