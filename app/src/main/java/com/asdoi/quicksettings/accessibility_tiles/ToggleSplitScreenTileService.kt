package com.asdoi.quicksettings.accessibility_tiles

import com.asdoi.quicksettings.abstract_tiles.AccessibilityTileService
import com.asdoi.quicksettings.utils.CustomAccessibilityService

class ToggleSplitScreenTileService : AccessibilityTileService() {

    override fun getActionString() = CustomAccessibilityService.TOGGLE_SPLIT_SCREEN
}