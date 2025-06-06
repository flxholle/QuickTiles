package com.flxholle.quicktiles.accessibility_tiles

import com.flxholle.quicktiles.abstract_tiles.AccessibilityTileService
import com.flxholle.quicktiles.utils.CustomAccessibilityService

class ToggleSplitScreenTileService : AccessibilityTileService() {

    override fun getActionString() = CustomAccessibilityService.TOGGLE_SPLIT_SCREEN
}