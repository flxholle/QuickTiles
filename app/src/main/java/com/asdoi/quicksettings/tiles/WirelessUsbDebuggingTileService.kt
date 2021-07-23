/*
 * Copyright (C) 2021 Saúl Gutiérrez
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

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.net.wifi.WifiManager
import android.provider.Settings
import android.widget.Toast
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.RootTileService
import com.asdoi.quicksettings.utils.WriteSystemSettingsUtils
import java.io.DataOutputStream
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder


class WirelessUsbDebuggingTileService : RootTileService<Int>() {
    companion object {
        const val ADB_SETTING = Settings.Global.ADB_ENABLED
        const val WIRELESS_ADB_PORT = "5555"
    }

    override fun isActive(value: Int): Boolean {
        return value != 0
    }

    @SuppressLint("PrivateApi")
    override fun queryValue(): Int {
        val kClass = Class.forName("android.os.SystemProperties")
        val method = kClass.getMethod("get", String::class.java)
        val reader = method.invoke(null, "service.adb.tcp.port")
        return if (reader == WIRELESS_ADB_PORT && queryAdbEnabled()) 1 else 0
    }

    private fun queryAdbEnabled(): Boolean {
        return WriteSystemSettingsUtils.getIntFromGlobalSettings(contentResolver, ADB_SETTING) == 1
    }

    override fun reset() {
        saveValue(0)
    }

    override fun saveValue(value: Int): Boolean {
        val adbEnabled = queryAdbEnabled()
        val finalAdbdState = value == 1 || adbEnabled

        val portCommand = "setprop service.adb.tcp.port " + if (value == 1) WIRELESS_ADB_PORT else "-1"
        val adbCommand = if(!adbEnabled && finalAdbdState) "settings put global adb_enabled 1" else ""
        val adbdCommand = "stop adbd" + if(finalAdbdState) " && start adbd" else ""
        var retVal: Boolean
        try {
            val su = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(su.outputStream)
            os.writeBytes(portCommand + "\n")
            os.writeBytes(adbCommand + "\n")
            os.writeBytes(adbdCommand + "\n")
            os.writeBytes("exit\n")
            os.flush()
            os.close()
            retVal = su.waitFor() != 255
            su.destroy()
        }
        catch (e : Exception) {
            Toast.makeText(applicationContext, R.string.root_failure, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            retVal = false
        }
        return retVal
    }

    override fun getValueList(): List<Int> {
        return listOf(0, 1)
    }

    override fun getIcon(value: Int): Icon {
        return Icon.createWithResource(applicationContext,
                if (value != 0) R.drawable.ic_usb_debugging_enabled else R.drawable.ic_usb_debugging_disabled)
    }

    private fun getLocalWifiIpAddress(): String {
        val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        var ipAddress: Int
        val errorGettingIPAddress = resources.getString(R.string.unable_to_get_ip_address);
        ipAddress = wifiManager.connectionInfo?.ipAddress ?: return errorGettingIPAddress
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipAddress = Integer.reverseBytes(ipAddress)
        }
        val ipByteArray: ByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
        return try {
            InetAddress.getByAddress(ipByteArray).hostAddress + ":" + WIRELESS_ADB_PORT
        } catch (ex: UnknownHostException) {
            errorGettingIPAddress
        }
    }

    override fun getLabel(value: Int): CharSequence {
        return if (value == 0) getString(R.string.wireless_usb_debugging) else getLocalWifiIpAddress()
    }
}
