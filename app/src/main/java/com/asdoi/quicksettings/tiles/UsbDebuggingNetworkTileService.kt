package com.asdoi.quicksettings.tiles

import android.graphics.drawable.Icon
import android.widget.Toast
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.RootTileService
import com.asdoi.quicksettings.utils.SharedPreferencesUtil
import java.io.DataOutputStream

class UsbDebuggingNetworkTileService : RootTileService<Int>() {
    companion object {
        const val ON = 0
        const val OFF = 1
    }

    override fun isActive(value: Int): Boolean {
        return value != OFF
    }

    override fun queryValue(): Int {
        return if (SharedPreferencesUtil.isADBNetwork(this))
            ON
        else
            OFF
    }

    override fun reset() {
        saveValue(OFF)
    }

    override fun saveValue(value: Int): Boolean {
        SharedPreferencesUtil.switchADBNetwork(this, value == ON)
        if (value == ON) {
            return try {
                val command = "setprop service.adb.tcp.port 5555\n" +
                        "stop adbd\n" +
                        "start adbd"

                val su = Runtime.getRuntime().exec("su")
                val os = DataOutputStream(su.outputStream)
                os.writeBytes(command + "\n")
                os.writeBytes("exit\n")
                os.close()
                su.waitFor()
                os.close()
                su.destroy()
                true
            } catch (e: Exception) {
                Toast.makeText(this, R.string.root_failure, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                false
            }
        } else {
            return try {
                val command = "stop adbd\n" +
                        "start adbd"

                val su = Runtime.getRuntime().exec("su")
                val os = DataOutputStream(su.outputStream)
                os.writeBytes(command + "\n")
                os.writeBytes("exit\n")
                os.close()
                su.waitFor()
                os.close()
                su.destroy()
                true
            } catch (e: Exception) {
                Toast.makeText(this, R.string.root_failure, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                false
            }
        }
    }

    override fun getValueList(): List<Int> {
        return listOf(OFF, ON)
    }

    override fun getIcon(value: Int): Icon? {
        return Icon.createWithResource(applicationContext,
                if (value == ON) R.drawable.ic_usb_debugging_enabled else R.drawable.ic_usb_debugging_disabled)
    }

    override fun getLabel(value: Int): CharSequence? {
        return getString(R.string.adb_over_network)
    }

}