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
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.utils.WriteSecureSettingsTileService
import com.asdoi.quicksettings.utils.WriteSystemSettingsUtils

//Not working on Newer API
class ShowTapsTileService : WriteSecureSettingsTileService<Int>() {
    companion object {
        const val SETTING = "show_touches" // This is hidden for developers, so we use the string resource
    }

    override fun isActive(value: Int): Boolean {
        return value != 0
    }

    override fun queryValue(): Int {
        var value = WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, SETTING)
        if (value > 1) value = 1
        return value
    }

    override fun reset() {
        saveValue(0)
    }

    override fun saveValue(value: Int): Boolean {
        /*
         * The proper way to do this would be to check Settings.System.canWrite().
         * If we can write there then write the setting and if we can't, then launch an Intent to
         * Settings.ACTION_MANAGE_WRITE_SETTINGS to enable the app to write system settings.
         *
         * The problem is that from API 23+ we can't do that with the "show_touches" setting
         * (and others I suppose) as it throws an IllegalArgumentException:
         * You cannot change private secure settings.
         *
         * So we have to fall back to use a targetSdkVersion of 22 so we can write the setting.
         * Kinda hacky, but it works ¯\_(ツ)_/¯.
         */

        return WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, SETTING, value)
    }

    override fun getValueList(): List<Int> {
        return listOf(0, 1)
    }

    override fun getIcon(value: Int): Icon? {
        return Icon.createWithResource(applicationContext,
                if (value != 0) R.drawable.ic_show_taps_enabled else R.drawable.ic_show_taps_disabled)
    }

    override fun getLabel(value: Int): CharSequence? {
        return getString(R.string.show_taps)
    }
}
