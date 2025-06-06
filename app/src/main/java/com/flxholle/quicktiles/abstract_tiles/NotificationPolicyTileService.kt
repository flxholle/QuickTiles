package com.flxholle.quicktiles.abstract_tiles

import com.flxholle.quicktiles.utils.GrantPermissionDialogs

abstract class NotificationPolicyTileService<T : Any> : SelectionTileService<T>() {

    override fun checkPermission(): Boolean {
        return if (GrantPermissionDialogs.hasNotificationPolicyPermission(this)) {
            true
        } else {
            showDialog(GrantPermissionDialogs.getNotificationPolicyDialog(this))
            false
        }
    }
}
