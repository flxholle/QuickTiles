package com.flxholle.quicktiles.tiles

import android.graphics.drawable.Icon
import android.provider.Settings
import android.view.Surface
import com.flxholle.quicktiles.R
import com.flxholle.quicktiles.abstract_tiles.WriteSecureSettingsTileService
import com.flxholle.quicktiles.utils.WriteSystemSettingsUtils

class RotationSwitchTileService : WriteSecureSettingsTileService<Int>() {
    companion object {
        const val SETTING_AUTO_ROTATION = Settings.System.ACCELEROMETER_ROTATION
        const val SETTING_ROTATION = Settings.System.USER_ROTATION

        const val PORTRAIT = Surface.ROTATION_0
        const val PORTRAIT_REVERSED = Surface.ROTATION_180
        const val LANDSCAPE = Surface.ROTATION_90
        const val LANDSCAPE_REVERSED = Surface.ROTATION_270
        const val AUTO_ROTATION = 10
    }

    override fun isActive(value: Int): Boolean {
        return value != AUTO_ROTATION
    }

    override fun queryValue(): Int {
        return if (WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, SETTING_AUTO_ROTATION) == 1)
            AUTO_ROTATION
        else
            WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, SETTING_ROTATION)
    }

    override fun reset() {
        WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING_AUTO_ROTATION, 1)
    }

    override fun saveValue(value: Int): Boolean {
        return if (value == AUTO_ROTATION) {
            reset()
            true
        } else {
            WriteSystemSettingsUtils.setIntToSystemSettings(
                contentResolver,
                SETTING_ROTATION,
                value
            )
            WriteSystemSettingsUtils.setIntToSystemSettings(
                contentResolver,
                SETTING_AUTO_ROTATION,
                0
            )
        }
    }

    override fun getValueList(): List<Int> {
        return listOf(PORTRAIT, LANDSCAPE, PORTRAIT_REVERSED, LANDSCAPE_REVERSED, AUTO_ROTATION)
    }

    override fun getIcon(value: Int): Icon {
        val resource =
            if (value == AUTO_ROTATION)
                R.drawable.ic_screen_rotation
            else
                R.drawable.ic_screen_lock_rotation

        return Icon.createWithResource(applicationContext, resource)
    }

    override fun getLabel(value: Int): CharSequence {
        return getString(
            when (value) {
                PORTRAIT -> R.string.zero_degrees
                LANDSCAPE -> R.string.ninety_degrees
                PORTRAIT_REVERSED -> R.string.onehundredeighty_degrees
                LANDSCAPE_REVERSED -> R.string.twohundredseventy_degrees
                AUTO_ROTATION -> R.string.auto_rotation
                else -> R.string.zero_degrees
            }
        )
    }
}