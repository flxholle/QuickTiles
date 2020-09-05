package com.asdoi.quicksettings.tiles

import android.graphics.drawable.Icon
import android.provider.Settings
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.utils.DevelopmentTileService

class ToggleAnimationTileService : DevelopmentTileService<Float>() {
    override fun isActive(value: Float): Boolean {
        return value != 1f
    }

    override fun getValueList(): List<Float> {
        return listOf(0f, 1f)
    }

    override fun queryValue(): Float {
        var scale = 1f
        try {
            scale = maxOf(
                    Settings.Global.getFloat(contentResolver,
                            Settings.Global.ANIMATOR_DURATION_SCALE),
                    Settings.Global.getFloat(contentResolver,
                            Settings.Global.WINDOW_ANIMATION_SCALE),
                    Settings.Global.getFloat(contentResolver,
                            Settings.Global.TRANSITION_ANIMATION_SCALE))
        } catch (e: Settings.SettingNotFoundException) {
        }

        return if (scale >= 1) 1.0f else 0.0f
    }

    override fun reset() {
        saveValue(1f)
    }

    override fun saveValue(value: Float): Boolean {
        return try {
            Settings.Global.putFloat(
                    contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, value)
            Settings.Global.putFloat(
                    contentResolver, Settings.Global.WINDOW_ANIMATION_SCALE, value)
            Settings.Global.putFloat(
                    contentResolver, Settings.Global.TRANSITION_ANIMATION_SCALE, value)
            true
        } catch (se: SecurityException) {
            false
        }
    }

    override fun getIcon(value: Float): Icon? {
        val icon = when {
            value <= 0f -> R.drawable.ic_animation_off
            else -> R.drawable.ic_animation_on
        }
        return Icon.createWithResource(applicationContext, icon)
    }

    override fun getLabel(value: Float): CharSequence? {
        val string = when {
            value <= 0f -> R.string.enable_all_animations
            else -> R.string.disable_all_animations
        }
        return getString(string)
    }

}