package com.asdoi.quicksettings.tiles

import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.Icon
import android.media.AudioManager
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.NotificationPolicyTileService

class SilenceLoudSwitchTileService : NotificationPolicyTileService<Int>() {
    companion object {
        const val NONE = NotificationManager.INTERRUPTION_FILTER_ALL
        const val DND_TOTAL_SILENCE = NotificationManager.INTERRUPTION_FILTER_NONE
        const val AM_NORMAL_MODE = AudioManager.RINGER_MODE_NORMAL

        const val NORMAL_MODE = 13
        const val TOTAL_SILENCE = 3
    }

    override fun isActive(value: Int): Boolean {
        return value != NORMAL_MODE
    }

    override fun queryValue(): Int {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val currentInterruptionFilter = notificationManager.currentInterruptionFilter
        return if (currentInterruptionFilter != NONE)
            TOTAL_SILENCE
        else
            NORMAL_MODE
    }


    override fun reset() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        notificationManager.setInterruptionFilter(NONE)
        audioManager.ringerMode = AM_NORMAL_MODE
    }

    override fun saveValue(value: Int): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        if (value == TOTAL_SILENCE)
            notificationManager.setInterruptionFilter(DND_TOTAL_SILENCE)
        else
            audioManager.ringerMode = AM_NORMAL_MODE
        return true
    }

    override fun getValueList(): List<Int> {
        return listOf(NORMAL_MODE, TOTAL_SILENCE)
    }

    override fun getIcon(value: Int): Icon? {
        val iconResource =
                when (value) {
                    NORMAL_MODE -> R.drawable.ic_ring_volume
                    TOTAL_SILENCE -> R.drawable.ic_remove_circle
                    else -> R.drawable.ic_ring_volume
                }

        return Icon.createWithResource(applicationContext, iconResource)
    }

    override fun getLabel(value: Int): CharSequence? {
        return getString(when (value) {
            NORMAL_MODE -> R.string.loud
            TOTAL_SILENCE -> R.string.silence
            else -> R.string.loud
        })
    }

}