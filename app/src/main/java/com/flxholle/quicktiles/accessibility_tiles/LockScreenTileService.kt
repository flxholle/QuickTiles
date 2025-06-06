package com.flxholle.quicktiles.accessibility_tiles

import com.flxholle.quicktiles.abstract_tiles.AccessibilityTileService
import com.flxholle.quicktiles.utils.CustomAccessibilityService

class LockScreenTileService : AccessibilityTileService() {

    override fun getActionString() = CustomAccessibilityService.LOCK_SCREEN
}