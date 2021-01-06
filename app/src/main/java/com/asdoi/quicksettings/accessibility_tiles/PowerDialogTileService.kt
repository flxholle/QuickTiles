package com.asdoi.quicksettings.accessibility_tiles

import com.asdoi.quicksettings.abstract_tiles.AccessibilityTileService
import com.asdoi.quicksettings.utils.CustomAccessibilityService

class PowerDialogTileService : AccessibilityTileService() {

    override fun getActionString() = CustomAccessibilityService.POWER_DIALOG
}