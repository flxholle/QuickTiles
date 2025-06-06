package com.flxholle.quicktiles.tiles

import android.content.Context
import android.graphics.drawable.Icon
import android.provider.Settings
import android.view.Surface
import android.view.WindowManager
import com.flxholle.quicktiles.R
import com.flxholle.quicktiles.abstract_tiles.WriteSecureSettingsTileService
import com.flxholle.quicktiles.utils.WriteSystemSettingsUtils


class FixRotationTileService : WriteSecureSettingsTileService<Int>() {
    companion object {
        const val SETTING_AUTO_ROTATION = Settings.System.ACCELEROMETER_ROTATION
        const val SETTING_ROTATION = Settings.System.USER_ROTATION

        const val PORTRAIT = Surface.ROTATION_0
        const val LANDSCAPE = Surface.ROTATION_90
        const val PORTRAIT_REVERSED = Surface.ROTATION_180
        const val LANDSCAPE_REVERSED = Surface.ROTATION_270

        const val AUTO_ROTATION = 10
        const val FIXED_ROTATION = 11
    }

    override fun isActive(value: Int): Boolean {
        return value != AUTO_ROTATION
    }

    override fun queryValue(): Int {
        return if (WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, SETTING_AUTO_ROTATION) == 1)
            AUTO_ROTATION
        else
            FIXED_ROTATION
    }

    override fun reset() {
        WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING_AUTO_ROTATION, 1)
    }

    private fun getRotation(): Int {
        return (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
    }

    override fun saveValue(value: Int): Boolean {
        return if (value == AUTO_ROTATION) {
            reset()
            true
        } else {
            WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING_ROTATION, getRotation())
            WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING_AUTO_ROTATION, 0)
        }
    }

    override fun getValueList(): List<Int> {
        return listOf(AUTO_ROTATION, FIXED_ROTATION)
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
        return if (value == AUTO_ROTATION) {
            getString(R.string.auto_rotation)
        } else {
            val rotation = getString(
                    when (getRotation()) {
                        PORTRAIT -> R.string.zero_degrees
                        PORTRAIT_REVERSED -> R.string.ninety_degrees
                        LANDSCAPE -> R.string.onehundredeighty_degrees
                        LANDSCAPE_REVERSED -> R.string.twohundredseventy_degrees
                        else -> R.string.zero_degrees
                    }
            )
            getString(R.string.fixed_rotation, rotation)
        }
    }
}