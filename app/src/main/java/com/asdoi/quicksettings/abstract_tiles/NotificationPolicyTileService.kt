package com.asdoi.quicksettings.abstract_tiles

import com.asdoi.quicksettings.utils.GrantPermissionDialogs

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
