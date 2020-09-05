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
import android.os.Build
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.tilesUtils.DemoMode
import com.asdoi.quicksettings.utils.DevelopmentTileService
import com.asdoi.quicksettings.utils.GrantPermissionDialogs
import com.asdoi.quicksettings.utils.SettingsUtils

/**
 * Code adapted from AOSP:
 * https://github.com/android/platform_frameworks_base/blob/marshmallow-mr3-release/packages/SystemUI/src/com/android/systemui/tuner/DemoModeFragment.java
 *
 * Check protocol here: https://github.com/android/platform_frameworks_base/blob/master/packages/SystemUI/docs/demo_mode.md
 */
class DemoModeTileService : DevelopmentTileService<Int>() {

    override fun isActive(value: Int): Boolean {
        return value != 0
    }

    override fun getValueList(): List<Int> {
        return listOf(0, 1)
    }

    override fun queryValue(): Int {
        return listOf(DemoMode.DEMO_MODE_ALLOWED, DemoMode.DEMO_MODE_ON)
                .fold(1, { current, key -> SettingsUtils.getIntFromGlobalSettings(contentResolver, key) and current })
    }

    override fun reset() {
        stopDemoMode()
    }

    override fun onClick() {
        if (GrantPermissionDialogs.hasDumpPermission(this)
                && GrantPermissionDialogs.hasWriteSecureSettingsPermission(this))
            super.onClick()
        else if (!GrantPermissionDialogs.hasDumpPermission(this))
            showDialog(GrantPermissionDialogs.getDumpDialog(this))
        else if (!GrantPermissionDialogs.hasWriteSecureSettingsPermission(this))
            showDialog(GrantPermissionDialogs.getWriteSecureSettingsDialog(this))
        else
            showDialog(GrantPermissionDialogs.getWriteSecureSettingsAndDumpDialog(this));
    }

    override fun saveValue(value: Int): Boolean {
        val isSettingEnabled =
                listOf(DemoMode.DEMO_MODE_ALLOWED, DemoMode.DEMO_MODE_ON)
                        .fold(true) { initial, setting ->
                            initial && SettingsUtils.setIntToGlobalSettings(contentResolver, setting, value)
                        }
        if (isSettingEnabled) {
            if (value != 0) {
                startDemoMode()
            } else {
                stopDemoMode()
            }
            return true
        } else {
            return false
        }
    }

    override fun getIcon(value: Int): Icon? {
        return Icon.createWithResource(applicationContext,
                if (value != 0) R.drawable.ic_demo_mode_enabled else R.drawable.ic_demo_mode_disabled)
    }

    override fun getLabel(value: Int): CharSequence? {
        return getString(R.string.demo_mode)
    }

    private fun startDemoMode() {
        // Enable Demo mode (as per documentation, this is optional)
        DemoMode.sendCommand(applicationContext, DemoMode.COMMAND_ENTER)

        // Set fixed time (use Android's major version for hours)
        DemoMode.sendCommand(applicationContext, DemoMode.COMMAND_CLOCK) { intent ->
            intent.putExtra("hhmm", "0${Build.VERSION.RELEASE.split(".")[0]}00")
        }

        // Set fixed network-related notification icons
        DemoMode.sendCommand(applicationContext, DemoMode.COMMAND_NETWORK) { intent ->
            intent.putExtra("wifi", "show")
            intent.putExtra("mobile", "show")
            intent.putExtra("sims", "1")
            intent.putExtra("nosim", "false")
            intent.putExtra("level", "4")
            intent.putExtra("datatype", "")
        }

        // Sets MCS state to fully connected (true, false)
        // Need to send this after so that the sim controller already exists.
        DemoMode.sendCommand(applicationContext, DemoMode.COMMAND_NETWORK) { intent ->
            intent.putExtra("fully", "true")
        }

        // Set fixed battery options
        DemoMode.sendCommand(applicationContext, DemoMode.COMMAND_BATTERY) { intent ->
            intent.putExtra("level", "100")
            intent.putExtra("plugged", "false")
        }

        // Hide other icons
        DemoMode.sendCommand(applicationContext, DemoMode.COMMAND_STATUS) { intent ->
            DemoMode.STATUS_ICONS.forEach { icon ->
                intent.putExtra(icon, "hide")
            }
        }

        // Hide notifications
        DemoMode.sendCommand(applicationContext, DemoMode.COMMAND_NOTIFICATIONS) { intent ->
            intent.putExtra("visible", "false")
        }
    }

    private fun stopDemoMode() {
        // Exit demo mode
        DemoMode.sendCommand(applicationContext, DemoMode.COMMAND_EXIT)
    }
}