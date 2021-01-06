package com.asdoi.quicksettings.accessibility_tiles

import com.asdoi.quicksettings.abstract_tiles.AccessibilityTileService
import com.asdoi.quicksettings.utils.CustomAccessibilityService

class ScreenshotTileService : AccessibilityTileService() {

    override fun getActionString() = CustomAccessibilityService.TAKE_SCREENSHOT
}