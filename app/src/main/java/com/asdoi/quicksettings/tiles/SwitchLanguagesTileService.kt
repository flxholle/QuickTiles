package com.asdoi.quicksettings.tiles

import android.content.Intent
import android.graphics.drawable.Icon
import android.os.LocaleList
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.ModifySystemSettingsTileService
import java.util.*

class SwitchLanguagesTileService : ModifySystemSettingsTileService<Locale>() {
    companion object {
        private const val TAG = "Locale"
    }

    override fun onClick() {
        unlockAndRun { super.onClick() }
    }

    override fun isActive(value: Locale) = false

    override fun getValueList(): List<Locale> {
        try {
            val localePickerClass = Class.forName("com.android.internal.app.LocalePicker")
            val getLocalesMethod = localePickerClass.getMethod("getLocales")
            val userLocales = getLocalesMethod.invoke(localePickerClass) as LocaleList
            return toList(userLocales)
        } catch (e: Exception) {
            try {
                val res = baseContext.resources
                return toList(res.configuration.locales)
            } catch (e: Exception) {
                Log.d(TAG, "Failed to get Languages")
            }
        }
        return listOf(queryValue())
    }

    override fun queryValue(): Locale {
        val res = baseContext.resources
        val locales = res.configuration.locales
        return locales[0]
    }

    private fun toList(locales: LocaleList): List<Locale> {
        val localeList = mutableListOf<Locale>()
        for (i in 0 until locales.size()) {
            localeList.add(locales[i])
        }
        return localeList
    }

    override fun reset() {
    }

    override fun saveValue(value: Locale): Boolean {
        val currentLocales = getValueList().toMutableList()
        val index = currentLocales.indexOf(value)
        if (index >= 0) {
            for (i in 0 until index) {
                currentLocales.add(currentLocales[0])
                currentLocales.removeAt(0)
            }
        }
        val newLocales = LocaleList(*currentLocales.toTypedArray())

        return try {
            val localePickerClass = Class.forName("com.android.internal.app.LocalePicker")
            val updateLocaleMethod = localePickerClass.getMethod("updateLocales", LocaleList::class.java)
            updateLocaleMethod.invoke(null, newLocales)
            true
        } catch (e: Exception) {
            Log.d(TAG, "Failed to set language")
            Toast.makeText(baseContext, R.string.failed_to_update_language, Toast.LENGTH_LONG).show()

            //Fallback: Open language settings
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (intent.resolveActivity(packageManager) != null) {
                startActivityAndCollapse(intent)
            } else {
                Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG).show()
            }
            false
        }
    }

    override fun getIcon(value: Locale): Icon {
        return Icon.createWithResource(applicationContext, R.drawable.ic_language)
    }

    override fun getLabel(value: Locale): CharSequence {
        return value.getDisplayName(value)
    }
}