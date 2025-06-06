package com.flxholle.quicktiles.tiles

import android.graphics.drawable.Icon
import android.provider.Settings
import com.flxholle.quicktiles.R
import com.flxholle.quicktiles.abstract_tiles.ModifySystemSettingsTileService
import com.flxholle.quicktiles.utils.WriteSystemSettingsUtils

class VibrateCallsTileService : ModifySystemSettingsTileService<Int>() {
    companion object {
        const val SETTING = Settings.System.VIBRATE_WHEN_RINGING
    }

    override fun isActive(value: Int): Boolean {
        return value != 0
    }

    override fun queryValue(): Int {
        return WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, SETTING)
    }

    override fun reset() {
        saveValue(0)
    }

    override fun saveValue(value: Int): Boolean {
        return WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING, value)
    }

    override fun getValueList(): List<Int> {
        return listOf(0, 1)
    }

    override fun updateIcon() = false

    override fun getIcon(value: Int): Icon {
        return Icon.createWithResource(applicationContext, R.drawable.ic_vibration)
    }

    override fun updateLabel() = false

    override fun getLabel(value: Int): CharSequence {
        return getString(R.string.vibrate_on_calls)
    }

}