package com.flxholle.quicktiles.accessibility_tiles

import com.flxholle.quicktiles.abstract_tiles.AccessibilityTileService
import com.flxholle.quicktiles.utils.CustomAccessibilityService

class ScreenshotTileService : AccessibilityTileService() {

    override fun getActionString() = CustomAccessibilityService.TAKE_SCREENSHOT
}