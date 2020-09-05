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
import com.asdoi.quicksettings.abstract_tiles.ModifySystemSettingsTileService
import com.asdoi.quicksettings.utils.WriteSystemSettingsUtils

class AdaptiveBrightnessTileService : ModifySystemSettingsTileService<Int>() {
    companion object {
        const val SETTING = Settings.System.SCREEN_BRIGHTNESS_MODE
        const val AUTO = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        const val MANUAL = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
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