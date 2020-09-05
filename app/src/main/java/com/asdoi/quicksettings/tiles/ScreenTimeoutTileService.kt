/*
 * Copyright (C) 2017 Adrián García
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asdoi.quicksettings.tiles

import android.graphics.drawable.Icon
import android.provider.Settings
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.utils.WriteSystemSettingsTileService
import com.asdoi.quicksettings.utils.WriteSystemSettingsUtils

class ScreenTimeoutTileService : WriteSystemSettingsTileService<Int>() {
    companion object {
        const val SETTING = Settings.System.SCREEN_OFF_TIMEOUT
    }

    override fun isActive(value: Int): Boolean {
        return value != Int.MAX_VALUE
    }

    override fun queryValue(): Int {
        return WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, SETTING)
    }

    override fun reset() {
        saveValue(128)
    }

    override fun saveValue(value: Int): Boolean {
        return WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING, value)
    }

    override fun getValueList(): List<Int> {
        return listOf(15000, 30000, 60000, 120000, 300000, 600000, 1800000, Int.MAX_VALUE)
    }

    override fun getIcon(value: Int): Icon? {
        val iconResource =
                when (value) {
                    15000 -> R.drawable.ic_screen15s
                    30000 -> R.drawable.ic_screen30s
                    60000 -> R.drawable.ic_screen1min
                    120000 -> R.drawable.ic_screen2min
                    300000 -> R.drawable.ic_screen5min
                    600000 -> R.drawable.ic_screen10min
                    1800000 -> R.drawable.ic_screen30min
                    else -> R.drawable.ic_screen_infinity
                }

        return Icon.createWithResource(applicationContext, iconResource)
    }

    override fun getLabel(value: Int): CharSequence? {
        if (value == Int.MAX_VALUE)
            return getString(R.string.infinite)

        val valueInSeconds = value / 1000
        val minutes = valueInSeconds / 60
        val seconds = valueInSeconds - (minutes * 60)

        return if (minutes == 0) {
            return "$seconds ${getString(R.string.seconds)}"
        } else {
            "$minutes ${if (minutes == 1) getString(R.string.minute) else getString(R.string.minutes)}"
        }
    }
}