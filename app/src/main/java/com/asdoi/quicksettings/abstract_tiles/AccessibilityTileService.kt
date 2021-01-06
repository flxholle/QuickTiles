package com.asdoi.quicksettings.abstract_tiles

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.utils.CustomAccessibilityService
import com.asdoi.quicksettings.utils.GrantPermissionDialogs

abstract class AccessibilityTileService : BaseTileService() {

    override fun onClick() {
        if (GrantPermissionDialogs.hasAccessibilityServicePermission(this)) {
            try {
                sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

                Handler(Looper.myLooper()!!).postDelayed({
                    startService(Intent(this, CustomAccessibilityService::class.java).setAction(getActionString()))
                }, 500)

            } catch (e: Exception) {
                Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_LONG).show()
            }
        } else {
            showDialog(GrantPermissionDialogs.getAccessibilityServiceDialog(this))
        }
    }

    override fun reset() {}

    abstract fun getActionString(): String
}