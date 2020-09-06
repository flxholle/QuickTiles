package com.asdoi.quicksettings.tiles

import android.graphics.drawable.Icon
import android.media.AudioManager
import com.asdoi.quicksettings.R
import com.asdoi.quicksettings.abstract_tiles.SelectionTileService

class MediaVolumeTileService : SelectionTileService<Int>() {
    companion object {
        const val SETTING = AudioManager.STREAM_MUSIC

        const val MUTE = 0
        const val MAX_VOLUME = 1
    }

    override fun isActive(value: Int): Boolean {
        return value != MUTE
    }

    override fun queryValue(): Int {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        return if (audioManager.getStreamVolume(SETTING) == 0)
            MUTE
        else
            MAX_VOLUME
    }

    override fun reset() {
        saveValue(MAX_VOLUME)
    }

    override fun saveValue(value: Int): Boolean {
        val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(SETTING, if (value == MUTE) 0 else audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
        return true
    }

    override fun getValueList(): List<Int> {
        return listOf(MUTE, MAX_VOLUME)
    }

    override fun getIcon(value: Int): Icon? {
        return Icon.createWithResource(applicationContext, R.drawable.ic_music_video)
    }

    override fun getLabel(value: Int): CharSequence? {
        return getString(R.string.media_volume)
    }

}