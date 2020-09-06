package com.asdoi.quicksettings.tiles

import android.app.NotificationManager
import android.content.Context
import android.graphics.drawable.Icon
import android.media.AudioManager
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.NotificationPolicyTileService

class DoNotDisturbSwitchTileService : NotificationPolicyTileService<Int>() {
    companion object {
        const val NONE = NotificationManager.INTERRUPTION_FILTER_ALL

        const val NORMAL_MODE = 13
        const val VIBRATE_MODE = 12

        //        const val SILENCE_MODE = 11
        const val PRIORITY_ONLY = 1
        const val ALARMS_ONLY = 2
        const val TOTAL_SILENCE = 3

        const val DND_PRIORITY_ONLY = NotificationManager.INTERRUPTION_FILTER_PRIORITY
        const val DND_ALARMS_ONLY = NotificationManager.INTERRUPTION_FILTER_ALARMS
        const val DND_TOTAL_SILENCE = NotificationManager.INTERRUPTION_FILTER_NONE

        //        const val AM_SILENCE_MODE = AudioManager.RINGER_MODE_SILENT
        const val AM_VIBRATE_MODE = AudioManager.RINGER_MODE_VIBRATE
        const val AM_NORMAL_MODE = AudioManager.RINGER_MODE_NORMAL
    }

    override fun isActive(value: Int): Boolean {
        return value != NORMAL_MODE
    }

    override fun queryValue(): Int {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        val currentInterruptionFilter = notificationManager.currentInterruptionFilter
        return if (currentInterruptionFilter != NONE) {
            when (currentInterruptionFilter) {
                DND_PRIORITY_ONLY -> PRIORITY_ONLY
                DND_ALARMS_ONLY -> ALARMS_ONLY
                DND_TOTAL_SILENCE -> TOTAL_SILENCE
                else -> PRIORITY_ONLY
            }
        } else {
            when (audioManager.ringerMode) {
                AM_NORMAL_MODE -> NORMAL_MODE
                AM_VIBRATE_MODE -> VIBRATE_MODE
//                AM_SILENCE_MODE -> SILENCE_MODE
                else -> NORMAL_MODE
            }
        }
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

        if (value <= TOTAL_SILENCE) {
            val dndMode =
                    when (value) {
                        PRIORITY_ONLY -> DND_PRIORITY_ONLY
                        ALARMS_ONLY -> DND_ALARMS_ONLY
                        TOTAL_SILENCE -> DND_TOTAL_SILENCE
                        else -> DND_PRIORITY_ONLY
                    }
            notificationManager.setInterruptionFilter(dndMode)
        } else {
            val amMode =
                    when (value) {
                        NORMAL_MODE -> AM_NORMAL_MODE
                        VIBRATE_MODE -> AM_VIBRATE_MODE
//                        SILENCE_MODE -> AM_SILENCE_MODE
                        else -> AM_NORMAL_MODE
                    }
            audioManager.ringerMode = amMode
        }
        return true
    }

    override fun getValueList(): List<Int> {
        return listOf(NORMAL_MODE, VIBRATE_MODE, PRIORITY_ONLY, ALARMS_ONLY, TOTAL_SILENCE)
    }

    override fun getIcon(value: Int): Icon? {
        val iconResource =
                when (value) {
                    NORMAL_MODE -> R.drawable.ic_ring_volume
                    VIBRATE_MODE -> R.drawable.ic_vibration
//                    SILENCE_MODE -> R.drawable.ic_notifications_off
                    PRIORITY_ONLY -> R.drawable.ic_priority_high
                    ALARMS_ONLY -> R.drawable.ic_remove_circle
                    TOTAL_SILENCE -> R.drawable.ic_exit_circle_left
                    else -> R.drawable.ic_ring_volume
                }

        return Icon.createWithResource(applicationContext, iconResource)
    }

    override fun getLabel(value: Int): CharSequence? {
        return getString(when (value) {
            NORMAL_MODE -> R.string.normal
            VIBRATE_MODE -> R.string.vibrate
//            SILENCE_MODE -> "Silence"
            PRIORITY_ONLY -> R.string.priority_only
            ALARMS_ONLY -> R.string.alarms_only
            TOTAL_SILENCE -> R.string.total_silence
            else -> R.string.normal
        })
    }

}