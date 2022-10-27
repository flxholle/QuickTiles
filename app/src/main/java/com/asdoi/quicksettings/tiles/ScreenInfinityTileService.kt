package com.asdoi.quicksettings.tiles

import android.graphics.drawable.Icon
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.ModifySystemSettingsTileService
import com.asdoi.quicksettings.utils.SharedPreferencesUtil
import com.asdoi.quicksettings.utils.WriteSystemSettingsUtils

class ScreenInfinityTileService : ModifySystemSettingsTileService<Int>() {
    companion object {
        const val OFF = 0
    }

    override fun isActive(value: Int): Boolean {
        return value == Int.MAX_VALUE
    }

    override fun queryValue(): Int {
        val screenTimeout = WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, ScreenTimeoutTileService.SETTING)
        if (screenTimeout != Int.MAX_VALUE) return OFF
        return Int.MAX_VALUE
    }

    override fun reset() {
        saveValue(128)
    }

    override fun saveValue(value: Int): Boolean {
        return if (value == Int.MAX_VALUE) {
            val screenTimeout = WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, ScreenTimeoutTileService.SETTING)
            SharedPreferencesUtil.setScreenTimeout(this, screenTimeout)
            WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, ScreenTimeoutTileService.SETTING, Int.MAX_VALUE)
        } else {
            var savedTimeout = SharedPreferencesUtil.getScreenTimeout(this)
            if (savedTimeout == Int.MAX_VALUE) savedTimeout = 60000
            WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, ScreenTimeoutTileService.SETTING, savedTimeout)
        }
    }

    override fun getValueList(): List<Int> {
        return listOf(OFF, Int.MAX_VALUE)
    }

    override fun getIcon(value: Int): Icon {
        return Icon.createWithResource(applicationContext, R.drawable.ic_screen_infinity)
    }

    override fun getLabel(value: Int): CharSequence {
        return getString(R.string.infinite)
    }
}