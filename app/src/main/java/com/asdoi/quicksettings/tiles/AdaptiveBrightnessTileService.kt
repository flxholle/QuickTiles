package com.asdoi.quicksettings.tiles

import android.graphics.drawable.Icon
import android.provider.Settings
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.ModifySystemSettingsTileService
import com.asdoi.quicksettings.job_services.AdaptiveBrightnessTileJobService
import com.asdoi.quicksettings.utils.WriteSystemSettingsUtils

class AdaptiveBrightnessTileService : ModifySystemSettingsTileService<Int>() {
    companion object {
        const val SETTING = Settings.System.SCREEN_BRIGHTNESS_MODE
        const val AUTO = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        const val MANUAL = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
    }

    override fun onTileAdded() {
        AdaptiveBrightnessTileJobService.scheduleUpdateJob(this)
    }

    override fun onTileRemoved() {
        AdaptiveBrightnessTileJobService.cancelJob(this)
    }

    override fun isActive(value: Int): Boolean {
        return value == AUTO
    }

    override fun queryValue(): Int {
        return WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, SETTING)
    }

    override fun reset() {
        saveValue(MANUAL)
    }

    override fun saveValue(value: Int): Boolean {
        return WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING, value)
    }

    override fun getValueList(): List<Int> {
        return listOf(AUTO, MANUAL)
    }

    override fun getIcon(value: Int): Icon? {
        val iconResource =
                if (value == AUTO)
                    R.drawable.ic_brightness_auto
                else
                    R.drawable.ic_brightness_auto_off

        return Icon.createWithResource(applicationContext, iconResource)
    }

    override fun getLabel(value: Int): CharSequence? {
        return getString(R.string.adaptive_brightness)
    }
}