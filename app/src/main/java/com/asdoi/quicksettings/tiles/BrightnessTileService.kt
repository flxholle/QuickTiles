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
import com.asdoi.quicksettings.utils.DevelopmentTileService
import com.asdoi.quicksettings.utils.SettingsUtils

class BrightnessTileService : DevelopmentTileService<Int>() {
    companion object {
        const val SETTING = Settings.System.SCREEN_BRIGHTNESS
    }

    override fun isActive(value: Int): Boolean {
        return value != 0
    }

    override fun queryValue(): Int {
        return SettingsUtils.getIntFromSystemSettings(contentResolver, SETTING)
    }

    override fun reset() {
        saveValue(128)
    }

    override fun saveValue(value: Int): Boolean {
        return SettingsUtils.setIntToSystemSettings(contentResolver, SETTING, value)
    }

    override fun getValueList(): List<Int> {
        return listOf(0, 85, 170, 255)
    }

    override fun getIcon(value: Int): Icon? {
        val iconResource =
                if (value <= 0)
                    R.drawable.ic_brightness_low
                else if (value <= 170)
                    R.drawable.ic_brightness_medium
                else
                    R.drawable.ic_brightness_high

        return Icon.createWithResource(applicationContext, iconResource)
    }

    override fun getLabel(value: Int): CharSequence? {
        return getString(R.string.adjust_brightness)
    }
}