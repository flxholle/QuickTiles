package com.asdoi.quicksettings.tiles

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.service.quicksettings.TileService
import com.asdoi.quicksettings.tilesUtils.ScreenShareActivity

class ScreenShareTileService : TileService() {
    override fun onClick() {
        super.onClick()
        val intent = Intent(this, ScreenShareActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        startActivityAndCollapse(intent)
    }
}