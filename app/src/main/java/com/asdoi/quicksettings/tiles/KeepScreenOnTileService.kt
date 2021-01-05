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
import android.os.BatteryManager
import android.provider.Settings
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.WriteSecureSettingsTileService
import com.asdoi.quicksettings.utils.GrantPermissionDialogs
import com.asdoi.quicksettings.utils.SharedPreferencesUtil
import com.asdoi.quicksettings.utils.WriteSystemSettingsUtils

class KeepScreenOnTileService : WriteSecureSettingsTileService<Int>() {
    companion object {
        const val SETTING = Settings.Global.STAY_ON_WHILE_PLUGGED_IN
        const val OFF = 0
        const val PLUGGED_AC = BatteryManager.BATTERY_PLUGGED_AC
        const val PLUGGED_USB = BatteryManager.BATTERY_PLUGGED_USB
        const val PLUGGED_WIRELESS = BatteryManager.BATTERY_PLUGGED_WIRELESS
        const val ANY = 7
    }

    override fun isActive(value: Int): Boolean {
        return value != OFF
    }

    override fun queryValue(): Int {
        val screenTimeout = WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, ScreenTimeoutTileService.SETTING)
        val pluggedState = WriteSystemSettingsUtils.getIntFromGlobalSettings(contentResolver, SETTING)

        if (screenTimeout != Int.MAX_VALUE)
            SharedPreferencesUtil.setScreenTimeout(this, screenTimeout)

        return if (pluggedState == OFF) {
            if (screenTimeout == Int.MAX_VALUE)
                ANY
            else
                pluggedState
        } else
            pluggedState
    }

    override fun reset() {
        saveValue(OFF)
    }

    override fun checkPermission(): Boolean {
        if (GrantPermissionDialogs.hasModifySystemSettingsPermission(this)
                && GrantPermissionDialogs.hasWriteSecureSettingsPermission(this))
            return true
        else if (!GrantPermissionDialogs.hasModifySystemSettingsPermission(this))
            showDialog(GrantPermissionDialogs.getModifySystemSettingsDialog(this))
        else
            showDialog(GrantPermissionDialogs.getWriteSecureSettingsDialog(this))
        return false
    }

    override fun saveValue(value: Int): Boolean {
        val screenTimeout = WriteSystemSettingsUtils.getIntFromSystemSettings(contentResolver, ScreenTimeoutTileService.SETTING)
        return if (value == ANY) {
            SharedPreferencesUtil.setScreenTimeout(this, screenTimeout)
            WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, ScreenTimeoutTileService.SETTING, Int.MAX_VALUE)
            WriteSystemSettingsUtils.setIntToGlobalSettings(contentResolver, SETTING, PLUGGED_AC)
        } else {
            if (screenTimeout == Int.MAX_VALUE)
                WriteSystemSettingsUtils.setIntToSystemSettings(contentResolver, ScreenTimeoutTileService.SETTING, SharedPreferencesUtil.getScreenTimeout(this))
            WriteSystemSettingsUtils.setIntToGlobalSettings(contentResolver, SETTING, value)
        }
    }

    override fun getValueList(): List<Int> {
        val value = when (SharedPreferencesUtil.getCustomizeKeepScreenOn(this)) {
            0 -> PLUGGED_AC
            1 -> PLUGGED_USB
            2 -> PLUGGED_WIRELESS
            3 -> ANY
            else -> ANY
        }
        return listOf(OFF, value)
    }

    override fun getIcon(value: Int): Icon {
        return Icon.createWithResource(applicationContext,
                if (value != OFF) R.drawable.ic_keep_screen_on_enabled else R.drawable.ic_keep_screen_on_disabled)
    }

    override fun updateLabel() = false

    override fun getLabel(value: Int): CharSequence {
        return getString(R.string.keep_screen_on)
    }

}