package com.flxholle.quicktiles.abstract_tiles

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.flxholle.quicktiles.R
import com.flxholle.quicktiles.utils.CustomAccessibilityService
import com.flxholle.quicktiles.utils.GrantPermissionDialogs

abstract class AccessibilityTileService : BaseTileService() {

    override fun onClick() {
        if (!GrantPermissionDialogs.hasAccessibilityServicePermission(this)) {
            showPermissionDialog(GrantPermissionDialogs.getAccessibilityServiceDialog(this))
            return
        }

        // The accessibility service lives in the same process as the tile, so it can be used
        // directly. This keeps the tile working when the app is not running in the foreground,
        // where starting a background service is no longer allowed.
        val accessibilityService = CustomAccessibilityService.getInstance()
        if (accessibilityService != null) {
            accessibilityService.performAction(getActionString())
            return
        }

        try {
            startService(
                Intent(this, CustomAccessibilityService::class.java).setAction(getActionString())
            )
        } catch (e: Exception) {
            Log.e(BaseTileService.LOG_TAG, "Error starting accessibility service", e)
            Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_LONG).show()
        }
    }

    override fun reset() {}

    abstract fun getActionString(): String
}
