package com.asdoi.quicksettings.tiles

import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.provider.Settings
import android.view.Surface
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.WriteSecureSettingsTileService
import com.asdoi.quicksettings.utils.WriteSystemSettingsUtils


class SwitchFixedRotationTileService : WriteSecureSettingsTileService<Int>() {
    companion object {
        const val SETTING_AUTO_ROTATION = Settings.System.ACCELEROMETER_ROTATION
        const val SETTING_ROTATION = Settings.System.USER_ROTATION

        const val NORMAL = Surface.ROTATION_0
        const val LANDSCAPE = Surface.ROTATION_90

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
        WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING_ROTATION, NORMAL)
    }

    private fun getRotation(): Int {
        return when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> NORMAL
            Configuration.ORIENTATION_LANDSCAPE -> LANDSCAPE
            else -> NORMAL
        }
    }

    override fun saveValue(value: Int): Boolean {
        return if (value == AUTO_ROTATION) {
            reset()
            true
        } else {
            WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING_AUTO_ROTATION, 0)
            WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING_ROTATION, getRotation())
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
                        NORMAL -> R.string.portrait
                        LANDSCAPE -> R.string.landscape
                        else -> R.string.portrait
                    })
            getString(R.string.fixed_rotation, rotation)
        }
    }
}