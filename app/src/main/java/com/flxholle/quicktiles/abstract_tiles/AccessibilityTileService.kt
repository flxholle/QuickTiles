package com.flxholle.quicktiles.abstract_tiles

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.flxholle.quicktiles.R
import com.flxholle.quicktiles.utils.CustomAccessibilityService
import com.flxholle.quicktiles.utils.GrantPermissionDialogs
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


abstract class AccessibilityTileService : BaseTileService() {

    override fun onClick() {
        if (GrantPermissionDialogs.hasAccessibilityServicePermission(this)) {
//                // Use a dummy intent to collapse the panel
//                val dummyIntent = Intent(this, SettingsActivity::class.java)
//                dummyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//                    val pendingIntent = PendingIntent.getActivity(
//                        this,
//                        0,
//                        dummyIntent,
//                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
//                    )
//                    startActivityAndCollapse(pendingIntent)
//                } else {
//                    startActivityAndCollapse(dummyIntent)
//                }

//            setExpandNotificationDrawer(this, true) // Collapse the notification drawer
//            collpasePanel(this)

            try {
                if (getDelay() > 0) {
                    Handler(Looper.myLooper()!!).postDelayed({
                        startService(
                            Intent(
                                this,
                                CustomAccessibilityService::class.java
                            ).setAction(getActionString())
                        )
                    }, getDelay())
                } else {
                    startService(
                        Intent(
                            this,
                            CustomAccessibilityService::class.java
                        ).setAction(getActionString())
                    )
                }
            } catch (e: Exception) {
                Log.e("AccessibilityTileService", "Error starting service", e)
                Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_LONG).show()
            }
        } else {
            showDialog(GrantPermissionDialogs.getAccessibilityServiceDialog(this))
        }
    }

    override fun reset() {}

    abstract fun getActionString(): String

    open fun getDelay(): Long {
        return 0L
    }

    // based on https://gist.github.com/XinyueZ/7bad2c02be425b350b7f
    // requires permission: "android.permission.EXPAND_STATUS_BAR"
    @SuppressLint("WrongConstant", "PrivateApi")
    fun setExpandNotificationDrawer(context: Context, expand: Boolean) {
        try {
            val statusBarService = context.getSystemService("statusbar")
            val methodName =
                if (expand)
                    if (Build.VERSION.SDK_INT >= 17) "expandNotificationsPanel" else "expand"
                else
                    if (Build.VERSION.SDK_INT >= 17) "collapsePanels" else "collapse"
            val statusBarManager: Class<*> = Class.forName("android.app.StatusBarManager")
            val method: Method = statusBarManager.getMethod(methodName)
            method.invoke(statusBarService)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    /*
       For opening and closing the notification-bar programmatically. Here is an example for closing.
       <uses-permission android:name=“android.permission.EXPAND_STATUS_BAR” />
       It might be an unofficial info.
       http://stackoverflow.com/questions/5029354/how-can-i-programmatically-open-close-notifications-in-android http://stackoverflow.com/questions/13766789/android-how-to-collapse-status-bar-on-android-4-2
     */
    private fun collpasePanel(_context: Context) {
        try {
            val sbservice = _context.getSystemService("statusbar")
            val statusbarManager: Class<*>?
            statusbarManager = Class.forName("android.app.StatusBarManager")
            val showsb: Method?
            if (Build.VERSION.SDK_INT >= 17) {
                showsb = statusbarManager.getMethod("collapsePanels")
            } else {
                showsb = statusbarManager.getMethod("collapse")
            }
            showsb.invoke(sbservice)
        } catch (_e: ClassNotFoundException) {
            _e.printStackTrace()
        } catch (_e: NoSuchMethodException) {
            _e.printStackTrace()
        } catch (_e: IllegalArgumentException) {
            _e.printStackTrace()
        } catch (_e: IllegalAccessException) {
            _e.printStackTrace()
        } catch (_e: InvocationTargetException) {
            _e.printStackTrace()
        }
    }
}